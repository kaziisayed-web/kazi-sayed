package com.example.data.repository

import android.content.Context
import com.example.data.local.MonkhuleDatabase
import com.example.data.local.SeedData
import com.example.data.model.CommentEntity
import com.example.data.model.MoodTag
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.PostType
import com.example.data.model.ReportEntity
import com.example.data.model.VentMode
import com.example.data.remote.GeminiAiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.example.data.remote.FirestoreSyncService
import java.util.UUID

class MonkhuleRepository(context: Context) {
    private val db = MonkhuleDatabase.getDatabase(context)
    private val postDao = db.postDao()
    private val firestoreSync = FirestoreSyncService(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Seed initial data if empty
        scope.launch {
            if (postDao.getPostCount() == 0) {
                postDao.insertPosts(SeedData.getInitialPosts())
                postDao.insertComments(SeedData.getInitialComments())
                postDao.insertNotifications(SeedData.getInitialNotifications())
            }
        }
    }

    fun getAllPosts(): Flow<List<PostEntity>> = postDao.getAllPosts()
    fun getTrendingPosts(): Flow<List<PostEntity>> = postDao.getTrendingPosts()
    fun getPostsByType(type: PostType): Flow<List<PostEntity>> = postDao.getPostsByType(type)
    fun getBookmarkedPosts(): Flow<List<PostEntity>> = postDao.getBookmarkedPosts()
    fun getPostFlow(id: String): Flow<PostEntity?> = postDao.getPostByIdFlow(id)
    fun searchPosts(query: String): Flow<List<PostEntity>> = postDao.searchPosts(query)

    fun getComments(postId: String): Flow<List<CommentEntity>> = postDao.getCommentsForPost(postId)
    fun getNotifications(): Flow<List<NotificationEntity>> = postDao.getAllNotifications()
    fun getReports(): Flow<List<ReportEntity>> = postDao.getAllReports()

    suspend fun createPost(
        title: String,
        content: String,
        postType: PostType,
        category: String,
        moodTag: MoodTag = MoodTag.NONE,
        ventMode: VentMode = VentMode.JUST_LISTEN,
        isAnonymous: Boolean = true,
        pseudonym: String = "",
        priceQuoted: Double = 0.0,
        priceFair: Double = 0.0,
        priceLocation: String = "Dhaka",
        priceCondition: String = "New",
        brandModel: String = "",
        pollQuestion: String = "",
        pollOptions: List<String> = emptyList()
    ): String {
        val id = "post-${UUID.randomUUID()}"
        val finalPseudonym = if (isAnonymous) {
            if (pseudonym.isNotBlank()) pseudonym else generateRandomPseudonym()
        } else ""

        val post = PostEntity(
            id = id,
            title = title,
            content = content,
            authorName = if (isAnonymous) "Anonymous" else "Kazi Sayed",
            authorHandle = if (isAnonymous) "anon_${id.takeLast(5)}" else "kazi_sayed",
            isAnonymous = isAnonymous,
            pseudonym = finalPseudonym,
            category = category,
            postType = postType,
            moodTag = moodTag,
            ventMode = ventMode,
            timestamp = System.currentTimeMillis(),
            supportiveCount = 0,
            helpfulCount = 0,
            commentCount = 0,
            priceQuoted = priceQuoted,
            priceFair = priceFair,
            priceLocation = priceLocation,
            priceCondition = priceCondition,
            brandModel = brandModel,
            pollQuestion = pollQuestion,
            pollOptions = pollOptions.joinToString(","),
            pollVotes = pollOptions.map { 0 }.joinToString(",")
        )

        postDao.insertPost(post)
        if (firestoreSync.isUserSignedIn()) {
            scope.launch { firestoreSync.syncPostToFirestore(post) }
        }
        return id
    }

    suspend fun updatePost(postId: String, newTitle: String, newContent: String): Boolean {
        val post = postDao.getPostById(postId) ?: return false
        postDao.updatePost(post.copy(title = newTitle, content = newContent))
        if (firestoreSync.isUserSignedIn()) {
            scope.launch { firestoreSync.updatePostInFirestore(postId, newTitle, newContent) }
        }
        return true
    }

    suspend fun deletePost(postId: String): Boolean {
        postDao.deletePost(postId)
        if (firestoreSync.isUserSignedIn()) {
            scope.launch { firestoreSync.deletePostInFirestore(postId) }
        }
        return true
    }

    suspend fun toggleSupport(postId: String) {
        val post = postDao.getPostById(postId) ?: return
        val newSupported = !post.isUserSupported
        val newCount = if (newSupported) post.supportiveCount + 1 else (post.supportiveCount - 1).coerceAtLeast(0)
        postDao.updatePost(post.copy(isUserSupported = newSupported, supportiveCount = newCount))
        if (newSupported && firestoreSync.isUserSignedIn()) {
            scope.launch { firestoreSync.syncReactionToFirestore(postId, isSupportive = true) }
        }
    }

    suspend fun toggleHelpful(postId: String) {
        val post = postDao.getPostById(postId) ?: return
        val newHelpful = !post.isUserHelpful
        val newCount = if (newHelpful) post.helpfulCount + 1 else (post.helpfulCount - 1).coerceAtLeast(0)
        postDao.updatePost(post.copy(isUserHelpful = newHelpful, helpfulCount = newCount))
        if (newHelpful && firestoreSync.isUserSignedIn()) {
            scope.launch { firestoreSync.syncReactionToFirestore(postId, isSupportive = false) }
        }
    }

    suspend fun toggleBookmark(postId: String) {
        val post = postDao.getPostById(postId) ?: return
        postDao.updatePost(post.copy(isBookmarked = !post.isBookmarked))
    }

    suspend fun votePoll(postId: String, optionIndex: Int) {
        val post = postDao.getPostById(postId) ?: return
        if (post.userVotedOptionIndex >= 0) return // Already voted

        val currentVotes = post.pollVotes.split(",").map { it.trim().toIntOrNull() ?: 0 }.toMutableList()
        if (optionIndex in currentVotes.indices) {
            currentVotes[optionIndex] = currentVotes[optionIndex] + 1
            postDao.updatePost(
                post.copy(
                    pollVotes = currentVotes.joinToString(","),
                    userVotedOptionIndex = optionIndex
                )
            )
        }
    }

    suspend fun addComment(
        postId: String,
        content: String,
        isAnonymous: Boolean = true,
        pseudonym: String = "",
        isAiResponse: Boolean = false
    ) {
        val comment = CommentEntity(
            id = "comment-${UUID.randomUUID()}",
            postId = postId,
            content = content,
            authorName = if (isAnonymous) "Anonymous" else "Community Member",
            isAnonymous = isAnonymous,
            pseudonym = if (isAnonymous) (if (pseudonym.isNotBlank()) pseudonym else generateRandomPseudonym()) else "",
            timestamp = System.currentTimeMillis(),
            supportiveCount = 0,
            isAiResponse = isAiResponse
        )
        postDao.insertComment(comment)
        postDao.incrementCommentCount(postId)
        if (firestoreSync.isUserSignedIn()) {
            scope.launch { firestoreSync.syncCommentToFirestore(comment) }
        }

        // Generate a notification for the author
        val notif = NotificationEntity(
            id = "notif-${UUID.randomUUID()}",
            title = if (isAiResponse) "AI সহচরের প্রতিক্রিয়া 🤖" else "নতুন মন্তব্য এসেছে 💬",
            message = if (isAiResponse) "আপনার পোস্টে AI সহচর একটি মমতাময়ী প্রতিক্রিয়া জানিয়েছেন।" else "আপনার পোস্টে নতুন সহমর্মী বার্তা যোগ হয়েছে: \"${content.take(40)}...\"",
            type = if (isAiResponse) "AI" else "COMMENT",
            relatedPostId = postId,
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        postDao.insertNotification(notif)
    }

    suspend fun requestAiVentAssistant(postId: String, isBengali: Boolean) {
        val post = postDao.getPostById(postId) ?: return
        val reflection = GeminiAiService.generateVentReflection(
            userVentTitle = post.title,
            userVentContent = post.content,
            mood = post.moodTag.name,
            isBengali = isBengali
        )
        addComment(
            postId = postId,
            content = reflection,
            isAnonymous = false,
            pseudonym = "AI Vent Assistant",
            isAiResponse = true
        )
    }

    suspend fun reportPost(postId: String, reason: String, details: String) {
        val post = postDao.getPostById(postId) ?: return
        val report = ReportEntity(
            id = "report-${UUID.randomUUID()}",
            postId = postId,
            postTitle = post.title,
            reportedAuthor = if (post.isAnonymous) post.pseudonym else post.authorName,
            reason = reason,
            details = details,
            timestamp = System.currentTimeMillis(),
            status = "PENDING"
        )
        postDao.insertReport(report)
    }

    suspend fun resolveReport(reportId: String, removePost: Boolean, postId: String) {
        postDao.updateReportStatus(reportId, if (removePost) "RESOLVED" else "DISMISSED")
        if (removePost) {
            postDao.hidePost(postId)
        }
    }

    suspend fun markAllNotificationsAsRead() {
        postDao.markAllNotificationsAsRead()
    }

    fun generateRandomPseudonym(): String {
        val adjectives = listOf("মেঘবালিকা 🌧️", "শান্ত নদী 🌊", "নিভৃতচারী 🌿", "অচেনা পথিক 🚶", "গোধূলির আলো 🌇", "মন খারাপ পাখি 🕊️", "আঁধার আকাশ 🌌", "Silent Willow 🍃", "Midnight Echo 🌙", "Wandering Soul 🧭")
        return adjectives.random()
    }

    fun getUserProfileFlow(userId: String): Flow<com.example.data.model.UserProfile?> =
        firestoreSync.getUserProfileFlow(userId)

    suspend fun getUserProfile(userId: String): com.example.data.model.UserProfile? =
        firestoreSync.getUserProfile(userId)

    suspend fun updateUserProfile(displayName: String, bio: String, avatar: String): Boolean =
        firestoreSync.updateUserProfile(displayName, bio, avatar)
}
