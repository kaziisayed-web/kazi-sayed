package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommentEntity
import com.example.data.model.MoodTag
import com.example.data.model.PostEntity
import com.example.data.model.PostType
import com.example.data.model.VentMode
import com.example.ui.components.PollWidget
import com.example.ui.components.PriceSummaryCard
import com.example.ui.components.TagPill
import com.example.ui.components.formatTimeAgo
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSupport
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.PurpleSurface
import com.example.ui.theme.RoseEmotional
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostDetailScreen(
    post: PostEntity?,
    comments: List<CommentEntity>,
    language: AppLanguage,
    isAiThinking: Boolean,
    onBack: () -> Unit,
    onSupportClick: () -> Unit,
    onHelpfulClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onVotePoll: (Int) -> Unit,
    onAddComment: (content: String, isAnonymous: Boolean, pseudonym: String) -> Unit,
    onRequestAiAssistant: () -> Unit,
    onReportPost: (reason: String, details: String) -> Unit,
    onEditPost: ((newTitle: String, newContent: String) -> Unit)? = null,
    onDeletePost: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var commentText by remember { mutableStateOf("") }
    var isCommentAnonymous by remember { mutableStateOf(true) }
    var showMenu by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var editTitle by remember(post) { mutableStateOf(post?.title ?: "") }
    var editContent by remember(post) { mutableStateOf(post?.content ?: "") }
    var reportReason by remember { mutableStateOf("Harassment / Abuse") }
    var reportDetails by remember { mutableStateOf("") }

    if (post == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = VioletPrimary)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (post.postType) {
                            PostType.VENT -> AppStrings.ventPost(language)
                            PostType.DISCUSSION -> AppStrings.discussionPost(language)
                            PostType.PRICE_TALK -> AppStrings.pricePost(language)
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onBookmarkClick) {
                        Icon(
                            imageVector = if (post.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (post.isBookmarked) VioletPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        if (onEditPost != null) {
                            DropdownMenuItem(
                                text = { Text(AppStrings.editPost(language)) },
                                onClick = {
                                    showMenu = false
                                    editTitle = post.title
                                    editContent = post.content
                                    showEditDialog = true
                                }
                            )
                        }
                        if (onDeletePost != null) {
                            DropdownMenuItem(
                                text = { Text(AppStrings.deletePost(language), color = RoseEmotional) },
                                onClick = {
                                    showMenu = false
                                    showDeleteDialog = true
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text(AppStrings.report(language)) },
                            onClick = {
                                showMenu = false
                                showReportDialog = true
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            // Comment input bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isCommentAnonymous) VioletPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { isCommentAnonymous = !isCommentAnonymous }
                            .padding(8.dp)
                    ) {
                        Text(
                            text = if (isCommentAnonymous) "🎭" else "👤",
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text(AppStrings.writeComment(language), fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_comment"),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                onAddComment(commentText, isCommentAnonymous, "")
                                commentText = ""
                            }
                        },
                        enabled = commentText.isNotBlank(),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (commentText.isNotBlank()) VioletPrimary else MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("send_comment_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = if (commentText.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // "Just Listen" Empathy reminder banner if ventMode is JUST_LISTEN
            if (post.postType == PostType.VENT && post.ventMode == VentMode.JUST_LISTEN) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SkyAccent.copy(alpha = 0.12f))
                            .border(1.dp, SkyAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Hearing,
                                contentDescription = "Just Listen",
                                tint = SkyAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "লেখক চেয়েছেন: শুধু শুনুন 👂" else "Author requested: Just Listen 👂",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SkyAccent
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = AppStrings.justListenNotice(language),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Post Card Detail
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Author header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(VioletPrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = if (post.isAnonymous) "🎭" else post.authorName.take(1).uppercase(), fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (post.isAnonymous) post.pseudonym else post.authorName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = formatTimeAgo(post.timestamp, language) + " • " + post.category,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tags
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (post.postType == PostType.VENT && post.moodTag != MoodTag.NONE) {
                                val moodLabel = if (language == AppLanguage.BANGLA) post.moodTag.labelBn else post.moodTag.labelEn
                                TagPill(text = "${post.moodTag.emoji} $moodLabel", color = VioletPrimary)
                            }
                            TagPill(
                                text = when (post.postType) {
                                    PostType.VENT -> if (language == AppLanguage.BANGLA) "মন খোলা" else "Vent"
                                    PostType.DISCUSSION -> if (language == AppLanguage.BANGLA) "আলোচনা" else "Discuss"
                                    PostType.PRICE_TALK -> if (language == AppLanguage.BANGLA) "দরদাম" else "Price Talk"
                                },
                                color = IndigoAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Title
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Content
                        Text(
                            text = post.content,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )

                        // If Price Talk
                        if (post.postType == PostType.PRICE_TALK && post.priceQuoted > 0) {
                            Spacer(modifier = Modifier.height(14.dp))
                            PriceSummaryCard(post = post, language = language)
                        }

                        // If Poll
                        if (post.postType == PostType.DISCUSSION && post.pollQuestion.isNotBlank()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            PollWidget(
                                question = post.pollQuestion,
                                options = post.pollOptions.split(",").map { it.trim() },
                                votes = post.pollVotes.split(",").map { it.trim().toIntOrNull() ?: 0 },
                                userVotedIndex = post.userVotedOptionIndex,
                                onVote = onVotePoll
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Interaction Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onSupportClick() }
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (post.isUserSupported) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Support",
                                        tint = if (post.isUserSupported) RoseEmotional else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${post.supportiveCount} " + AppStrings.support(language),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (post.isUserSupported) RoseEmotional else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onHelpfulClick() }
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (post.isUserHelpful) Icons.Default.Lightbulb else Icons.Outlined.Lightbulb,
                                        contentDescription = "Helpful",
                                        tint = if (post.isUserHelpful) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${post.helpfulCount} " + AppStrings.helpful(language),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (post.isUserHelpful) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // AI Vent Assistant Trigger Button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(VioletPrimary, IndigoAccent)
                                        )
                                    )
                                    .clickable(enabled = !isAiThinking) { onRequestAiAssistant() }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("btn_request_ai_assistant")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isAiThinking) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(14.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Psychology,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.BANGLA) "AI সহচর ডাকুন" else "AI Vent Companion",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Comments Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${AppStrings.comments(language)} (${comments.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Comments List
            if (comments.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.BANGLA) "এখনো কোনো মন্তব্য নেই। আপনিই প্রথম সহমর্মী বার্তা পাঠান।" else "No replies yet. Be the first to offer support.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(comments, key = { it.id }) { comment ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (comment.isAiResponse)
                                PurpleSurface.copy(alpha = 0.7f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(14.dp),
                        border = if (comment.isAiResponse)
                            androidx.compose.foundation.BorderStroke(1.dp, VioletPrimary.copy(alpha = 0.5f))
                        else null
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (comment.isAiResponse) "🤖" else if (comment.isAnonymous) "🎭" else "👤",
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (comment.isAiResponse) "MONKHULE AI" else if (comment.isAnonymous) comment.pseudonym else comment.authorName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (comment.isAiResponse) CyanAccent else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = formatTimeAgo(comment.timestamp, language),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = comment.content,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    // Report Dialog
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text(AppStrings.report(language)) },
            text = {
                Column {
                    Text(
                        text = if (language == AppLanguage.BANGLA) "রিপোর্টের কারণ উল্লেখ করুন:" else "Select report reason:",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf(
                        "Harassment / Abuse",
                        "Threats / Harm",
                        "Doxxing / Private Info",
                        "Spam / Scams",
                        "Misleading Prices"
                    ).forEach { reason ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { reportReason = reason }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = if (reportReason == reason) "🔘 " else "⚪ ",
                                fontSize = 14.sp
                            )
                            Text(text = reason, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReportPost(reportReason, reportDetails)
                        showReportDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseEmotional)
                ) {
                    Text(AppStrings.report(language), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Post Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text(AppStrings.editPost(language), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text(if (language == AppLanguage.BANGLA) "শিরোনাম" else "Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        label = { Text(if (language == AppLanguage.BANGLA) "বিস্তারিত বর্ণনা" else "Content") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        maxLines = 8,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editTitle.isNotBlank() && editContent.isNotBlank()) {
                            onEditPost?.invoke(editTitle.trim(), editContent.trim())
                            showEditDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
                ) {
                    Text(AppStrings.save(language), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text(AppStrings.cancel(language))
                }
            }
        )
    }

    // Delete Post Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(AppStrings.deletePost(language), fontWeight = FontWeight.Bold, color = RoseEmotional) },
            text = {
                Text(
                    text = AppStrings.confirmDelete(language),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeletePost?.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseEmotional)
                ) {
                    Text(AppStrings.deletePost(language), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(AppStrings.cancel(language))
                }
            }
        )
    }
}
