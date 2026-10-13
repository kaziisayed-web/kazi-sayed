package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PostType {
    VENT,
    DISCUSSION,
    PRICE_TALK
}

enum class MoodTag(val labelEn: String, val labelBn: String, val emoji: String) {
    ANGRY("Angry", "রাগ", "😠"),
    FRUSTRATED("Frustrated", "হতাশ", "😤"),
    SAD("Sad", "মন খারাপ", "😢"),
    CONFUSED("Confused", "বিভ্রান্ত", "🤔"),
    NEED_SUPPORT("Need Support", "পাশে চাই", "🫂"),
    NONE("Neutral", "স্বাভাবিক", "💬")
}

enum class VentMode {
    JUST_LISTEN,
    ADVICE_WELCOME
}

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val authorName: String,
    val authorHandle: String,
    val isAnonymous: Boolean,
    val pseudonym: String,
    val category: String,
    val postType: PostType,
    val moodTag: MoodTag = MoodTag.NONE,
    val ventMode: VentMode = VentMode.JUST_LISTEN,
    val timestamp: Long,
    val supportiveCount: Int = 0,
    val helpfulCount: Int = 0,
    val commentCount: Int = 0,
    val isBookmarked: Boolean = false,
    val isUserSupported: Boolean = false,
    val isUserHelpful: Boolean = false,
    // Price talk specific
    val priceQuoted: Double = 0.0,
    val priceFair: Double = 0.0,
    val priceCurrency: String = "BDT (৳)",
    val priceLocation: String = "Dhaka",
    val priceCondition: String = "New",
    val brandModel: String = "",
    // Discussion & Poll specific
    val pollQuestion: String = "",
    val pollOptions: String = "", // Comma-separated or JSON
    val pollVotes: String = "",   // Comma-separated vote counts
    val userVotedOptionIndex: Int = -1,
    // Moderation
    val isHidden: Boolean = false
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val content: String,
    val authorName: String,
    val isAnonymous: Boolean,
    val pseudonym: String,
    val timestamp: Long,
    val supportiveCount: Int = 0,
    val isUserSupported: Boolean = false,
    val isAiResponse: Boolean = false
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val type: String, // SUPPORT, COMMENT, MENTION, ADMIN
    val relatedPostId: String = "",
    val timestamp: Long,
    val isRead: Boolean = false
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val postTitle: String,
    val reportedAuthor: String,
    val reason: String,
    val details: String,
    val timestamp: Long,
    val status: String = "PENDING" // PENDING, RESOLVED, DISMISSED
)

data class PollData(
    val question: String,
    val options: List<String>,
    val votes: List<Int>,
    val totalVotes: Int,
    val userVotedIndex: Int
)

data class UserProfile(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val bio: String = "",
    val avatar: String = "👤",
    val avatarUrl: String = "",
    val pseudonym: String = "",
    val role: String = "user",
    val language: String = "bn"
)
