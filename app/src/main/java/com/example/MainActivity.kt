package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.MonkhuleBottomBar
import com.example.ui.components.MonkhuleTopBar
import com.example.ui.navigation.AppScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CloudSyncScreen
import com.example.ui.screens.CreatePostScreen
import com.example.ui.screens.DiscussionsScreen
import com.example.ui.screens.GuidelinesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PostDetailScreen
import com.example.ui.screens.PremiumPlansScreen
import com.example.ui.screens.PriceTalkScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedPostsScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.VentZoneScreen
import com.example.ui.theme.MonkhuleTheme
import com.example.ui.viewmodel.FeedFilterTab
import com.example.ui.viewmodel.MonkhuleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MonkhuleViewModel = viewModel()
            val language by viewModel.language.collectAsState()
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()

            MonkhuleTheme(darkTheme = isDarkMode) {
                MonkhuleApp(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun MonkhuleApp(
    viewModel: MonkhuleViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val feedTab by viewModel.feedTab.collectAsState()
    val selectedMood by viewModel.selectedMood.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val allPosts by viewModel.allPosts.collectAsState()
    val trendingPosts by viewModel.trendingPosts.collectAsState()
    val ventPosts by viewModel.ventPosts.collectAsState()
    val discussionPosts by viewModel.discussionPosts.collectAsState()
    val pricePosts by viewModel.pricePosts.collectAsState()
    val bookmarkedPosts by viewModel.bookmarkedPosts.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val reports by viewModel.reports.collectAsState()

    val selectedPost by viewModel.selectedPost.collectAsState()
    val currentComments by viewModel.currentComments.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()
    val aiWritingSuggestion by viewModel.aiWritingSuggestion.collectAsState()

    val statusMessage by viewModel.statusMessage.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val isSavingProfile by viewModel.isSavingProfile.collectAsState()
    val snackbarHostState = androidx.compose.runtime.remember { androidx.compose.material3.SnackbarHostState() }

    androidx.compose.runtime.LaunchedEffect(statusMessage) {
        statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    val unreadNotifCount = notifications.count { !it.isRead }

    val isMainTab = currentScreen is AppScreen.Home ||
            currentScreen is AppScreen.VentZone ||
            currentScreen is AppScreen.Discussions ||
            currentScreen is AppScreen.PriceTalk ||
            currentScreen is AppScreen.Notifications ||
            currentScreen is AppScreen.Profile

    Scaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        snackbarHost = { androidx.compose.material3.SnackbarHost(snackbarHostState) },
        topBar = {
            if (isMainTab) {
                MonkhuleTopBar(
                    language = language,
                    isDarkMode = isDarkMode,
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onToggleTheme = { viewModel.toggleTheme() },
                    onSearchClick = { viewModel.navigateTo(AppScreen.Search) },
                    onCreatePostClick = { viewModel.navigateTo(AppScreen.CreatePost()) }
                )
            }
        },
        bottomBar = {
            if (isMainTab) {
                MonkhuleBottomBar(
                    currentScreen = currentScreen,
                    language = language,
                    unreadNotifCount = unreadNotifCount,
                    onTabSelected = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is AppScreen.Home -> {
                    val postsToShow = when (feedTab) {
                        FeedFilterTab.LATEST -> allPosts
                        FeedFilterTab.TRENDING -> trendingPosts
                        FeedFilterTab.FOLLOWING -> allPosts.take(3)
                    }
                    HomeScreen(
                        posts = postsToShow,
                        language = language,
                        currentTab = feedTab,
                        selectedCategory = selectedCategory,
                        selectedMood = selectedMood,
                        onTabSelected = { viewModel.setFeedTab(it) },
                        onCategorySelected = { viewModel.setCategoryFilter(it) },
                        onMoodSelected = { viewModel.setMoodFilter(it) },
                        onPostClick = { viewModel.openPostDetail(it) },
                        onSupportClick = { viewModel.toggleSupport(it) },
                        onHelpfulClick = { viewModel.toggleHelpful(it) },
                        onBookmarkClick = { viewModel.toggleBookmark(it) },
                        onVotePoll = { postId, opt -> viewModel.votePoll(postId, opt) },
                        onReportClick = { viewModel.reportPost(it, "Harassment", "") },
                        onCreatePostClick = { viewModel.navigateTo(AppScreen.CreatePost()) },
                        onOpenVentZone = { viewModel.navigateTo(AppScreen.VentZone) }
                    )
                }

                is AppScreen.VentZone -> {
                    VentZoneScreen(
                        ventPosts = ventPosts,
                        language = language,
                        selectedMood = selectedMood,
                        onMoodSelected = { viewModel.setMoodFilter(it) },
                        onPostClick = { viewModel.openPostDetail(it) },
                        onSupportClick = { viewModel.toggleSupport(it) },
                        onHelpfulClick = { viewModel.toggleHelpful(it) },
                        onBookmarkClick = { viewModel.toggleBookmark(it) },
                        onReportClick = { viewModel.reportPost(it, "Harmful Vent", "") },
                        onCreateVentClick = { viewModel.navigateTo(AppScreen.CreatePost("VENT")) }
                    )
                }

                is AppScreen.Discussions -> {
                    DiscussionsScreen(
                        discussions = discussionPosts,
                        language = language,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.setCategoryFilter(it) },
                        onPostClick = { viewModel.openPostDetail(it) },
                        onSupportClick = { viewModel.toggleSupport(it) },
                        onHelpfulClick = { viewModel.toggleHelpful(it) },
                        onBookmarkClick = { viewModel.toggleBookmark(it) },
                        onVotePoll = { postId, opt -> viewModel.votePoll(postId, opt) },
                        onReportClick = { viewModel.reportPost(it, "Inappropriate Topic", "") },
                        onCreateDiscussionClick = { viewModel.navigateTo(AppScreen.CreatePost("DISCUSSION")) }
                    )
                }

                is AppScreen.PriceTalk -> {
                    PriceTalkScreen(
                        pricePosts = pricePosts,
                        language = language,
                        onPostClick = { viewModel.openPostDetail(it) },
                        onSupportClick = { viewModel.toggleSupport(it) },
                        onHelpfulClick = { viewModel.toggleHelpful(it) },
                        onBookmarkClick = { viewModel.toggleBookmark(it) },
                        onReportClick = { viewModel.reportPost(it, "Fake Price", "") },
                        onCreatePriceReportClick = { viewModel.navigateTo(AppScreen.CreatePost("PRICE_TALK")) }
                    )
                }

                is AppScreen.Notifications -> {
                    NotificationsScreen(
                        notifications = notifications,
                        language = language,
                        onNotificationClick = { postId -> viewModel.openPostDetail(postId) },
                        onMarkAllRead = { viewModel.markAllNotificationsAsRead() }
                    )
                }

                is AppScreen.Profile -> {
                    ProfileScreen(
                        userPosts = allPosts.take(2),
                        savedPosts = bookmarkedPosts,
                        language = language,
                        isDarkMode = isDarkMode,
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onToggleTheme = { viewModel.toggleTheme() },
                        onOpenSavedPosts = { viewModel.navigateTo(AppScreen.SavedPosts) },
                        onOpenAdminDashboard = { viewModel.navigateTo(AppScreen.AdminDashboard) },
                        onOpenGuidelines = { viewModel.navigateTo(AppScreen.Guidelines) },
                        onOpenPremiumPlans = { viewModel.navigateTo(AppScreen.PremiumPlans) },
                        onOpenCloudSync = { viewModel.navigateTo(AppScreen.CloudSyncInfo) },
                        onGeneratePseudonym = { viewModel.getRandomPseudonym() },
                        userProfile = userProfile,
                        isSavingProfile = isSavingProfile,
                        onSaveProfile = { name, bio, avatar ->
                            viewModel.updateUserProfile(name, bio, avatar)
                        }
                    )
                }

                is AppScreen.PostDetail -> {
                    PostDetailScreen(
                        post = selectedPost,
                        comments = currentComments,
                        language = language,
                        isAiThinking = isAiThinking,
                        onBack = { viewModel.navigateBack() },
                        onSupportClick = { selectedPost?.id?.let { viewModel.toggleSupport(it) } },
                        onHelpfulClick = { selectedPost?.id?.let { viewModel.toggleHelpful(it) } },
                        onBookmarkClick = { selectedPost?.id?.let { viewModel.toggleBookmark(it) } },
                        onVotePoll = { opt -> selectedPost?.id?.let { viewModel.votePoll(it, opt) } },
                        onAddComment = { content, isAnon, pseudo ->
                            selectedPost?.id?.let { viewModel.addComment(it, content, isAnon, pseudo) }
                        },
                        onRequestAiAssistant = {
                            selectedPost?.id?.let { viewModel.requestAiVentAssistant(it) }
                        },
                        onReportPost = { reason, details ->
                            selectedPost?.id?.let { viewModel.reportPost(it, reason, details) }
                        },
                        onEditPost = { newTitle, newContent ->
                            selectedPost?.id?.let { viewModel.editPost(it, newTitle, newContent) }
                        },
                        onDeletePost = {
                            selectedPost?.id?.let { viewModel.deletePost(it) }
                        }
                    )
                }

                is AppScreen.CreatePost -> {
                    CreatePostScreen(
                        initialType = screen.defaultType,
                        language = language,
                        isAiThinking = isAiThinking,
                        aiSuggestion = aiWritingSuggestion,
                        onBack = { viewModel.navigateBack() },
                        onGeneratePseudonym = { viewModel.getRandomPseudonym() },
                        onAssistWriting = { viewModel.assistWriting(it) },
                        onClearSuggestion = { viewModel.clearWritingSuggestion() },
                        onSubmitPost = { title, content, type, cat, mood, mode, isAnon, pseudo, quoted, fair, loc, cond, brand, pollQ, pollOpts ->
                            viewModel.createPost(
                                title = title,
                                content = content,
                                postType = type,
                                category = cat,
                                moodTag = mood,
                                ventMode = mode,
                                isAnonymous = isAnon,
                                pseudonym = pseudo,
                                priceQuoted = quoted,
                                priceFair = fair,
                                priceLocation = loc,
                                priceCondition = cond,
                                brandModel = brand,
                                pollQuestion = pollQ,
                                pollOptions = pollOpts
                            )
                        }
                    )
                }

                is AppScreen.Search -> {
                    SearchScreen(
                        allPosts = allPosts,
                        language = language,
                        onBack = { viewModel.navigateBack() },
                        onPostClick = { viewModel.openPostDetail(it) },
                        onSupportClick = { viewModel.toggleSupport(it) },
                        onHelpfulClick = { viewModel.toggleHelpful(it) },
                        onBookmarkClick = { viewModel.toggleBookmark(it) }
                    )
                }

                is AppScreen.SavedPosts -> {
                    SavedPostsScreen(
                        savedPosts = bookmarkedPosts,
                        language = language,
                        onBack = { viewModel.navigateBack() },
                        onPostClick = { viewModel.openPostDetail(it) },
                        onSupportClick = { viewModel.toggleSupport(it) },
                        onHelpfulClick = { viewModel.toggleHelpful(it) },
                        onBookmarkClick = { viewModel.toggleBookmark(it) }
                    )
                }

                is AppScreen.AdminDashboard -> {
                    AdminDashboardScreen(
                        reports = reports,
                        allPosts = allPosts,
                        language = language,
                        onBack = { viewModel.navigateBack() },
                        onResolveReport = { rId, remove, pId -> viewModel.resolveReport(rId, remove, pId) }
                    )
                }

                is AppScreen.Guidelines -> {
                    GuidelinesScreen(
                        language = language,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.PremiumPlans -> {
                    PremiumPlansScreen(
                        language = language,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is AppScreen.CloudSyncInfo -> {
                    CloudSyncScreen(
                        language = language,
                        onBack = { viewModel.navigateBack() }
                    )
                }
            }
        }
    }
}
