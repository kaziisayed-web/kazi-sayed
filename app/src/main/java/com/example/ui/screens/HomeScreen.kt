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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.data.model.MoodTag
import com.example.data.model.PostEntity
import com.example.ui.components.PostCard
import com.example.ui.components.TagPill
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.PurpleSurface
import com.example.ui.theme.RoseEmotional
import com.example.ui.theme.VioletPrimary
import com.example.ui.viewmodel.FeedFilterTab
import com.example.util.AppLanguage
import com.example.util.AppStrings

@Composable
fun HomeScreen(
    posts: List<PostEntity>,
    language: AppLanguage,
    currentTab: FeedFilterTab,
    selectedCategory: String?,
    selectedMood: MoodTag?,
    onTabSelected: (FeedFilterTab) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onMoodSelected: (MoodTag?) -> Unit,
    onPostClick: (String) -> Unit,
    onSupportClick: (String) -> Unit,
    onHelpfulClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onVotePoll: (String, Int) -> Unit,
    onReportClick: (String) -> Unit,
    onCreatePostClick: () -> Unit,
    onOpenVentZone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        if (language == AppLanguage.BANGLA) "সব" else "All",
        AppStrings.catWork(language),
        AppStrings.catStudy(language),
        AppStrings.catFamily(language),
        AppStrings.catFriendship(language),
        AppStrings.catDailyLife(language),
        AppStrings.catElectronics(language),
        AppStrings.catServices(language)
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_feed_list"),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Hero Welcome Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF2E1065),
                                    Color(0xFF1E1B4B),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(CyanAccent)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "মন খুলে নিরাপদ ঘর" else "Safe Expression Space",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(VioletPrimary.copy(alpha = 0.3f))
                                    .clickable { onOpenVentZone() }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "মন খারাপ? 🌧️" else "Feeling Heavy? 🌧️",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == AppLanguage.BANGLA)
                                "মন খুলে বলো, এখানে বিচার নেই, শুধু শোনার মানুষ।"
                            else
                                "Speak. Share. Feel Better. No judgment, just empathy.",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 22.sp
                            ),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == AppLanguage.BANGLA)
                                "ব্যক্তিগত হতাশা, ক্যাম্পাসের ক্ষোভ, উন্মুক্ত আলোচনা কিংবা পণ্যের ন্যায্য দর—সবকিছু আলোচনা করুন নিশ্চিন্তে।"
                            else
                                "Express personal burnout, discuss open topics, or discover fair market prices anonymously.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Tabs: Latest, Trending, Following
            item {
                TabRow(
                    selectedTabIndex = currentTab.ordinal,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = VioletPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[currentTab.ordinal]),
                            color = VioletPrimary,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Tab(
                        selected = currentTab == FeedFilterTab.LATEST,
                        onClick = { onTabSelected(FeedFilterTab.LATEST) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.latest(language), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    )
                    Tab(
                        selected = currentTab == FeedFilterTab.TRENDING,
                        onClick = { onTabSelected(FeedFilterTab.TRENDING) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.trending(language), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    )
                    Tab(
                        selected = currentTab == FeedFilterTab.FOLLOWING,
                        onClick = { onTabSelected(FeedFilterTab.FOLLOWING) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.following(language), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    )
                }
            }

            // Category Chips Row
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
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
                                    if (isSelected) VioletPrimary
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

            // Post Items Feed
            val filteredPosts = posts.filter { post ->
                val matchesCategory = selectedCategory == null || post.category.contains(selectedCategory, ignoreCase = true)
                val matchesMood = selectedMood == null || post.moodTag == selectedMood
                matchesCategory && matchesMood
            }

            if (filteredPosts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🍃", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) "কোনো পোস্ট পাওয়া যায়নি" else "No posts yet in this section",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) "প্রথম মন খুলে লিখুন!" else "Be the first to speak your heart!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredPosts, key = { it.id }) { post ->
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

        // Floating Action Button to Create Post
        FloatingActionButton(
            onClick = onCreatePostClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 24.dp)
                .testTag("fab_create_post"),
            containerColor = VioletPrimary,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create Post")
        }
    }
}
