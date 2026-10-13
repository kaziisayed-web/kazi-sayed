package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PostEntity
import com.example.ui.components.PostCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings

@Composable
fun DiscussionsScreen(
    discussions: List<PostEntity>,
    language: AppLanguage,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    onPostClick: (String) -> Unit,
    onSupportClick: (String) -> Unit,
    onHelpfulClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onVotePoll: (String, Int) -> Unit,
    onReportClick: (String) -> Unit,
    onCreateDiscussionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        if (language == AppLanguage.BANGLA) "সব" else "All",
        AppStrings.catFamily(language),
        AppStrings.catWork(language),
        AppStrings.catStudy(language),
        AppStrings.catFriendship(language),
        AppStrings.catDailyLife(language)
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("discussions_list"),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Header Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF1E1B4B),
                                    Color(0xFF312E81),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Forum,
                                contentDescription = null,
                                tint = CyanAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) "উন্মুক্ত সামাজিক আলোচনা" else "Public Discussions & Q&A",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == AppLanguage.BANGLA)
                                "যেকোনো বিষয়ে সুস্থ বিতর্ক, সমাজের অভিজ্ঞতা, ক্যারিয়ার পরামর্শ ও পোল-এ মতামত প্রকাশ করুন।"
                            else
                                "Engage in constructive debates, ask thought-provoking questions, and cast votes in community polls.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2E8F0),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Categories
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isAll = cat == (if (language == AppLanguage.BANGLA) "সব" else "All")
                        val isSelected = (isAll && selectedCategory == null) || (cat == selectedCategory)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) IndigoAccent
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable {
                                    if (isAll) onCategorySelected(null)
                                    else onCategorySelected(if (selectedCategory == cat) null else cat)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Discussion items
            val filtered = discussions.filter {
                selectedCategory == null || it.category.contains(selectedCategory, ignoreCase = true)
            }

            if (filtered.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.BANGLA) "কোনো আলোচনা নেই" else "No discussions found",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(filtered, key = { it.id }) { post ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        PostCard(
                            post = post,
                            language = language,
                            onCardClick = { onPostClick(post.id) },
                            onSupportClick = { onSupportClick(post.id) },
                            onHelpfulClick = { onHelpfulClick(post.id) },
                            onBookmarkClick = { onBookmarkClick(post.id) },
                            onVotePoll = { optionIndex -> onVotePoll(post.id, optionIndex) },
                            onReportClick = { onReportClick(post.id) }
                        )
                    }
                }
            }
        }

        // FAB to Create Discussion
        FloatingActionButton(
            onClick = onCreateDiscussionClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 24.dp)
                .testTag("fab_create_discussion"),
            containerColor = IndigoAccent,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create Discussion")
        }
    }
}
