package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MoodTag
import com.example.data.model.PostEntity
import com.example.data.model.PostType
import com.example.data.model.VentMode
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSupport
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseEmotional
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostCard(
    post: PostEntity,
    language: AppLanguage,
    onCardClick: () -> Unit,
    onSupportClick: () -> Unit,
    onHelpfulClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onVotePoll: (Int) -> Unit = {},
    onReportClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onCardClick() }
            .testTag("post_card_${post.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Author Avatar & Metadata & Dropdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Avatar icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (post.isAnonymous) VioletPrimary.copy(alpha = 0.2f)
                                else IndigoAccent.copy(alpha = 0.2f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (post.isAnonymous) "🎭" else post.authorName.take(1).uppercase(),
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (post.isAnonymous) post.pseudonym else post.authorName,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (post.isAnonymous) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CyanAccent.copy(alpha = 0.15f))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.BANGLA) "বেনামী" else "Anonymous",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyanAccent
                                    )
                                }
                            }
                        }

                        Text(
                            text = formatTimeAgo(post.timestamp, language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Options menu (Report)
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(AppStrings.report(language)) },
                            onClick = {
                                showMenu = false
                                onReportClick()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges & Tags Row
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Post Type Badge
                val typeColor = when (post.postType) {
                    PostType.VENT -> RoseEmotional
                    PostType.DISCUSSION -> IndigoAccent
                    PostType.PRICE_TALK -> EmeraldSupport
                }
                val typeLabel = when (post.postType) {
                    PostType.VENT -> if (language == AppLanguage.BANGLA) "মন খোলা" else "Vent"
                    PostType.DISCUSSION -> if (language == AppLanguage.BANGLA) "আলোচনা" else "Discuss"
                    PostType.PRICE_TALK -> if (language == AppLanguage.BANGLA) "দরদাম" else "Fair Price"
                }
                TagPill(text = typeLabel, color = typeColor)

                // Category Tag
                TagPill(text = post.category, color = MaterialTheme.colorScheme.onSurfaceVariant)

                // Mood Pill for Vents
                if (post.postType == PostType.VENT && post.moodTag != MoodTag.NONE) {
                    val moodLabel = if (language == AppLanguage.BANGLA) post.moodTag.labelBn else post.moodTag.labelEn
                    TagPill(text = "${post.moodTag.emoji} $moodLabel", color = VioletPrimary)
                }

                // "Just Listen" Badge
                if (post.postType == PostType.VENT && post.ventMode == VentMode.JUST_LISTEN) {
                    TagPill(
                        text = if (language == AppLanguage.BANGLA) "👂 শুধু শুনুন" else "👂 Just Listen",
                        color = SkyAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Post Title
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Post Content Preview
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            // Price Talk Summary Widget
            if (post.postType == PostType.PRICE_TALK && post.priceQuoted > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                PriceSummaryCard(
                    post = post,
                    language = language
                )
            }

            // Discussion Poll Widget
            if (post.postType == PostType.DISCUSSION && post.pollQuestion.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                PollWidget(
                    question = post.pollQuestion,
                    options = post.pollOptions.split(",").map { it.trim() },
                    votes = post.pollVotes.split(",").map { it.trim().toIntOrNull() ?: 0 },
                    userVotedIndex = post.userVotedOptionIndex,
                    onVote = onVotePoll
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Actions: Support, Helpful, Comments, Bookmark, Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Support reaction
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSupportClick() }
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isUserSupported) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Support",
                            tint = if (post.isUserSupported) RoseEmotional else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.supportiveCount}",
                            fontSize = 12.sp,
                            color = if (post.isUserSupported) RoseEmotional else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (post.isUserSupported) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    // Helpful reaction (for discussions or price reports)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onHelpfulClick() }
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isUserHelpful) Icons.Default.Lightbulb else Icons.Outlined.Lightbulb,
                            contentDescription = "Helpful",
                            tint = if (post.isUserHelpful) AmberWarningLight else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.helpfulCount}",
                            fontSize = 12.sp,
                            color = if (post.isUserHelpful) AmberWarningLight else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (post.isUserHelpful) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    // Comments count
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Comments",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.commentCount}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Bookmark action
                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (post.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (post.isBookmarked) VioletPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

val AmberWarningLight = Color(0xFFF59E0B)

@Composable
fun TagPill(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
fun PriceSummaryCard(
    post: PostEntity,
    language: AppLanguage
) {
    val diff = post.priceQuoted - post.priceFair
    val savingsPercent = if (post.priceQuoted > 0) ((diff / post.priceQuoted) * 100).toInt() else 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(1.dp, EmeraldSupport.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.BANGLA) "দোকানের দর" else "Quoted Price",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "৳${post.priceQuoted.toInt()}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseEmotional
                    )
                }

                Text(text = "➔", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)

                Column {
                    Text(
                        text = if (language == AppLanguage.BANGLA) "ন্যায্য প্রস্তাবিত দর" else "Fair Price",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "৳${post.priceFair.toInt()}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSupport
                    )
                }

                if (diff > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldSupport.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.BANGLA) "সাশ্রয় ৳${diff.toInt()} ($savingsPercent%)" else "Save ৳${diff.toInt()} ($savingsPercent%)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSupport
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📍 ${post.priceLocation}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "🏷️ ${post.priceCondition}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PollWidget(
    question: String,
    options: List<String>,
    votes: List<Int>,
    userVotedIndex: Int,
    onVote: (Int) -> Unit
) {
    val totalVotes = votes.sum().coerceAtLeast(1)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = "📊 $question",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            options.forEachIndexed { index, option ->
                val voteCount = votes.getOrElse(index) { 0 }
                val percentage = ((voteCount.toFloat() / totalVotes) * 100).toInt()
                val isSelected = userVotedIndex == index
                val hasVoted = userVotedIndex >= 0

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(enabled = !hasVoted) { onVote(index) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Voted",
                                    tint = VioletPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = option,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (hasVoted) {
                            Text(
                                text = "$percentage% ($voteCount)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (hasVoted) {
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { voteCount.toFloat() / totalVotes },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isSelected) VioletPrimary else IndigoAccent,
                            trackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

fun formatTimeAgo(timestamp: Long, language: AppLanguage): String {
    val diff = System.currentTimeMillis() - timestamp
    val minute = 60 * 1000L
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        diff < 5 * minute -> if (language == AppLanguage.BANGLA) "এইমাত্র" else "Just now"
        diff < hour -> {
            val mins = diff / minute
            if (language == AppLanguage.BANGLA) "$mins মিনিট আগে" else "${mins}m ago"
        }
        diff < day -> {
            val hours = diff / hour
            if (language == AppLanguage.BANGLA) "$hours ঘণ্টা আগে" else "${hours}h ago"
        }
        else -> {
            val days = diff / day
            if (language == AppLanguage.BANGLA) "$days দিন আগে" else "${days}d ago"
        }
    }
}
