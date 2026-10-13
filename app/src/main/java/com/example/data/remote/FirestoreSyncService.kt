package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.model.CommentEntity
import com.example.data.model.MoodTag
import com.example.data.model.PostEntity
import com.example.data.model.PostType
import com.example.data.model.VentMode
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

import com.example.data.model.UserProfile
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreSyncService(private val context: Context) {
    val db: FirebaseFirestore by lazy {
        val dbId = context.getString(R.string.firestore_database_id)
        FirebaseFirestore.getInstance(dbId)
    }

    private val auth get() = Firebase.auth

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun getUserProfileFlow(userId: String): Flow<UserProfile?> = callbackFlow {
        val listener = db.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreSync", "Listen failed for user $userId: ${error.message}")
                    trySend(null)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val profile = UserProfile(
                        userId = snapshot.getString("userId") ?: userId,
                        displayName = snapshot.getString("displayName") ?: "",
                        email = snapshot.getString("email") ?: "",
                        bio = snapshot.getString("bio") ?: "",
                        avatar = snapshot.getString("avatar") ?: "👤",
                        avatarUrl = snapshot.getString("avatarUrl") ?: "",
                        pseudonym = snapshot.getString("pseudonym") ?: "",
                        role = snapshot.getString("role") ?: "user",
                        language = snapshot.getString("language") ?: "bn"
                    )
                    trySend(profile)
                } else {
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val snapshot = db.collection("users").document(userId).get().await()
            if (snapshot.exists()) {
                UserProfile(
                    userId = snapshot.getString("userId") ?: userId,
                    displayName = snapshot.getString("displayName") ?: "",
                    email = snapshot.getString("email") ?: "",
                    bio = snapshot.getString("bio") ?: "",
                    avatar = snapshot.getString("avatar") ?: "👤",
                    avatarUrl = snapshot.getString("avatarUrl") ?: "",
                    pseudonym = snapshot.getString("pseudonym") ?: "",
                    role = snapshot.getString("role") ?: "user",
                    language = snapshot.getString("language") ?: "bn"
                )
            } else null
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error getting user profile: ${e.message}")
            null
        }
    }

    suspend fun updateUserProfile(displayName: String, bio: String, avatar: String): Boolean {
        val user = auth.currentUser ?: return false
        val userDoc = db.collection("users").document(user.uid)
        return try {
            val snapshot = userDoc.get().await()
            val validName = if (displayName.isNotBlank()) displayName.trim() else (user.displayName ?: "MONKHULE Member")
            val data = hashMapOf<String, Any>(
                "userId" to user.uid,
                "displayName" to validName,
                "email" to (user.email ?: ""),
                "bio" to bio.trim(),
                "avatar" to avatar,
                "avatarUrl" to if (avatar.startsWith("http")) avatar else "",
                "updatedAt" to FieldValue.serverTimestamp()
            )

            if (!snapshot.exists()) {
                data["role"] = "user"
                data["createdAt"] = FieldValue.serverTimestamp()
                userDoc.set(data).await()
            } else {
                userDoc.set(data, SetOptions.merge()).await()
            }

            // Sync with Firebase Auth user profile
            try {
                val profileChange = UserProfileChangeRequest.Builder()
                    .setDisplayName(validName)
                    .build()
                user.updateProfile(profileChange).await()
            } catch (e: Exception) {
                Log.w("FirestoreSync", "Warning updating auth profile: ${e.message}")
            }

            Log.d("FirestoreSync", "User profile updated successfully in 'users' collection: ${user.uid}")
            true
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error updating user profile in 'users' collection: ${e.message}")
            false
        }
    }

    suspend fun syncUserToFirestore(pseudonym: String = "", bio: String = "") {
        val user = auth.currentUser ?: return
        val userDoc = db.collection("users").document(user.uid)
        val data = hashMapOf<String, Any>(
            "userId" to user.uid,
            "displayName" to (user.displayName ?: "MONKHULE Member"),
            "email" to (user.email ?: ""),
            "pseudonym" to pseudonym,
            "bio" to bio,
            "avatar" to "👤",
            "role" to "user",
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            userDoc.set(data, SetOptions.merge()).await()
            Log.d("FirestoreSync", "User synced: ${user.uid}")
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error syncing user: ${e.message}")
        }
    }

    suspend fun syncPostToFirestore(post: PostEntity) {
        val user = auth.currentUser ?: return
        val postDoc = db.collection("posts").document(post.id)
        val payload = hashMapOf<String, Any>(
            "id" to post.id,
            "authorId" to user.uid,
            "authorName" to if (post.isAnonymous) "Anonymous" else (user.displayName ?: post.authorName),
            "isAnonymous" to post.isAnonymous,
            "pseudonym" to post.pseudonym,
            "title" to post.title,
            "content" to post.content,
            "postType" to post.postType.name,
            "category" to post.category,
            "moodTag" to post.moodTag.name,
            "ventMode" to post.ventMode.name,
            "supportiveCount" to post.supportiveCount,
            "helpfulCount" to post.helpfulCount,
            "commentCount" to post.commentCount,
            "priceQuoted" to post.priceQuoted,
            "priceFair" to post.priceFair,
            "priceLocation" to post.priceLocation,
            "priceCondition" to post.priceCondition,
            "brandModel" to post.brandModel,
            "pollQuestion" to post.pollQuestion,
            "pollOptions" to post.pollOptions,
            "pollVotes" to post.pollVotes,
            "isHidden" to post.isHidden,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            postDoc.set(payload, SetOptions.merge()).await()
            Log.d("FirestoreSync", "Post synced: ${post.id}")
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error syncing post: ${e.message}")
        }
    }

    suspend fun updatePostInFirestore(postId: String, newTitle: String, newContent: String): Boolean {
        val user = auth.currentUser ?: return false
        val postDoc = db.collection("posts").document(postId)
        return try {
            val snapshot = postDoc.get().await()
            val authorId = snapshot.getString("authorId")
            if (authorId != null && authorId != user.uid) {
                Log.w("FirestoreSync", "Ownership check failed: $authorId != ${user.uid}")
                return false
            }
            postDoc.update(
                mapOf(
                    "title" to newTitle,
                    "content" to newContent,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error updating post: ${e.message}")
            false
        }
    }

    suspend fun deletePostInFirestore(postId: String): Boolean {
        val user = auth.currentUser ?: return false
        val postDoc = db.collection("posts").document(postId)
        return try {
            val snapshot = postDoc.get().await()
            val authorId = snapshot.getString("authorId")
            if (authorId != null && authorId != user.uid) {
                Log.w("FirestoreSync", "Ownership check failed: cannot delete someone else's post")
                return false
            }
            postDoc.delete().await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error deleting post: ${e.message}")
            false
        }
    }

    suspend fun syncCommentToFirestore(comment: CommentEntity) {
        val user = auth.currentUser ?: return
        val commentDoc = db.collection("posts").document(comment.postId)
            .collection("comments").document(comment.id)
        val payload = hashMapOf<String, Any>(
            "id" to comment.id,
            "postId" to comment.postId,
            "authorId" to user.uid,
            "authorName" to if (comment.isAnonymous) "Anonymous" else (user.displayName ?: comment.authorName),
            "isAnonymous" to comment.isAnonymous,
            "pseudonym" to comment.pseudonym,
            "content" to comment.content,
            "supportiveCount" to comment.supportiveCount,
            "isAiResponse" to comment.isAiResponse,
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            commentDoc.set(payload).await()
            db.collection("posts").document(comment.postId)
                .update("commentCount", FieldValue.increment(1)).await()
            Log.d("FirestoreSync", "Comment synced: ${comment.id}")
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error syncing comment: ${e.message}")
        }
    }

    suspend fun syncReactionToFirestore(postId: String, isSupportive: Boolean) {
        if (!isUserSignedIn()) return
        val postDoc = db.collection("posts").document(postId)
        try {
            val field = if (isSupportive) "supportiveCount" else "helpfulCount"
            postDoc.update(field, FieldValue.increment(1)).await()
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error syncing reaction: ${e.message}")
        }
    }
}
