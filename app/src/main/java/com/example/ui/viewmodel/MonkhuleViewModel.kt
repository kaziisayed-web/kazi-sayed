package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CommentEntity
import com.example.data.model.MoodTag
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.PostType
import com.example.data.model.ReportEntity
import com.example.data.model.VentMode
import com.example.data.remote.GeminiAiService
import com.example.data.repository.MonkhuleRepository
import com.example.ui.navigation.AppScreen
import com.example.util.AppLanguage
import com.example.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FeedFilterTab {
    LATEST,
    TRENDING,
    FOLLOWING
}

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MonkhuleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MonkhuleRepository(application)

    // Navigation Stack
    private val _screenStack = MutableStateFlow<List<AppScreen>>(listOf(AppScreen.Home))
    val currentScreen: StateFlow<AppScreen> = _screenStack
        .combine(MutableStateFlow(Unit)) { stack, _ -> stack.lastOrNull() ?: AppScreen.Home }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppScreen.Home)

    // Language & Theme State
    private val _language = MutableStateFlow(AppLanguage.BANGLA)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Feed Tabs & Filters
    private val _feedTab = MutableStateFlow(FeedFilterTab.LATEST)
    val feedTab: StateFlow<FeedFilterTab> = _feedTab.asStateFlow()

    private val _selectedMood = MutableStateFlow<MoodTag?>(null)
    val selectedMood: StateFlow<MoodTag?> = _selectedMood.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Loading & AI State
    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun showStatusMessage(msg: String) {
        _statusMessage.value = msg
    }

    private val _aiWritingSuggestion = MutableStateFlow<String?>(null)
    val aiWritingSuggestion: StateFlow<String?> = _aiWritingSuggestion.asStateFlow()

    // User Profile Firestore State
    private val _isSavingProfile = MutableStateFlow(false)
    val isSavingProfile: StateFlow<Boolean> = _isSavingProfile.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    init {
        // Observe Firebase Auth changes and dynamically listen to Firestore 'users' document
        try {
            Firebase.auth.addAuthStateListener { authInstance ->
                val user = authInstance.currentUser
                if (user != null) {
                    viewModelScope.launch {
                        repository.getUserProfileFlow(user.uid).collect { profile ->
                            _userProfile.value = profile ?: UserProfile(
                                userId = user.uid,
                                displayName = user.displayName ?: "MONKHULE Member",
                                email = user.email ?: "",
                                bio = "",
                                avatar = "👤"
                            )
                        }
                    }
                } else {
                    _userProfile.value = null
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MonkhuleVM", "Error setting auth listener: ${e.message}")
        }
    }

    // Detail Post State
    private val _selectedPostId = MutableStateFlow<String?>(null)
    val selectedPost: StateFlow<PostEntity?> = _selectedPostId
        .flatMapLatest { id ->
            if (id == null) MutableStateFlow(null)
            else repository.getPostFlow(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentComments: StateFlow<List<CommentEntity>> = _selectedPostId
        .flatMapLatest { id ->
            if (id == null) MutableStateFlow(emptyList())
            else repository.getComments(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Feeds
    val allPosts: StateFlow<List<PostEntity>> = repository.getAllPosts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingPosts: StateFlow<List<PostEntity>> = repository.getTrendingPosts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ventPosts: StateFlow<List<PostEntity>> = repository.getPostsByType(PostType.VENT)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val discussionPosts: StateFlow<List<PostEntity>> = repository.getPostsByType(PostType.DISCUSSION)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pricePosts: StateFlow<List<PostEntity>> = repository.getPostsByType(PostType.PRICE_TALK)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedPosts: StateFlow<List<PostEntity>> = repository.getBookmarkedPosts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.getNotifications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports: StateFlow<List<ReportEntity>> = repository.getReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Navigation Actions ---
    fun navigateTo(screen: AppScreen) {
        val current = _screenStack.value
        // If navigating to a main bottom tab, reset stack to that tab
        if (screen is AppScreen.Home || screen is AppScreen.VentZone ||
            screen is AppScreen.Discussions || screen is AppScreen.PriceTalk ||
            screen is AppScreen.Notifications || screen is AppScreen.Profile
        ) {
            _screenStack.value = listOf(screen)
        } else {
            _screenStack.value = current + screen
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        return if (current.size > 1) {
            _screenStack.value = current.dropLast(1)
            true
        } else {
            false
        }
    }

    fun openPostDetail(postId: String) {
        _selectedPostId.value = postId
        navigateTo(AppScreen.PostDetail(postId))
    }

    // --- Language & Theme Actions ---
    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.BANGLA) AppLanguage.ENGLISH else AppLanguage.BANGLA
    }

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // --- Filter Actions ---
    fun setFeedTab(tab: FeedFilterTab) {
        _feedTab.value = tab
    }

    fun setMoodFilter(mood: MoodTag?) {
        _selectedMood.value = if (_selectedMood.value == mood) null else mood
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // --- Community Actions ---
    fun toggleSupport(postId: String) {
        viewModelScope.launch {
            repository.toggleSupport(postId)
        }
    }

    fun toggleHelpful(postId: String) {
        viewModelScope.launch {
            repository.toggleHelpful(postId)
        }
    }

    fun toggleBookmark(postId: String) {
        viewModelScope.launch {
            repository.toggleBookmark(postId)
        }
    }

    fun votePoll(postId: String, optionIndex: Int) {
        viewModelScope.launch {
            repository.votePoll(postId, optionIndex)
        }
    }

    fun addComment(postId: String, content: String, isAnonymous: Boolean, pseudonym: String) {
        viewModelScope.launch {
            repository.addComment(postId, content, isAnonymous, pseudonym, isAiResponse = false)
        }
    }

    fun requestAiVentAssistant(postId: String) {
        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                repository.requestAiVentAssistant(postId, _language.value == AppLanguage.BANGLA)
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun assistWriting(draftText: String) {
        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                val suggestion = GeminiAiService.assistWriting(draftText, _language.value == AppLanguage.BANGLA)
                _aiWritingSuggestion.value = suggestion
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun clearWritingSuggestion() {
        _aiWritingSuggestion.value = null
    }

    fun createPost(
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
    ) {
        viewModelScope.launch {
            val newId = repository.createPost(
                title = title,
                content = content,
                postType = postType,
                category = category,
                moodTag = moodTag,
                ventMode = ventMode,
                isAnonymous = isAnonymous,
                pseudonym = pseudonym,
                priceQuoted = priceQuoted,
                priceFair = priceFair,
                priceLocation = priceLocation,
                priceCondition = priceCondition,
                brandModel = brandModel,
                pollQuestion = pollQuestion,
                pollOptions = pollOptions
            )
            navigateBack()
            openPostDetail(newId)
        }
    }

    fun reportPost(postId: String, reason: String, details: String) {
        viewModelScope.launch {
            repository.reportPost(postId, reason, details)
        }
    }

    fun resolveReport(reportId: String, removePost: Boolean, postId: String) {
        viewModelScope.launch {
            repository.resolveReport(reportId, removePost, postId)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun editPost(postId: String, newTitle: String, newContent: String) {
        viewModelScope.launch {
            repository.updatePost(postId, newTitle, newContent)
            _statusMessage.value = "পোস্ট সফলভাবে আপডেট হয়েছে। / Post updated successfully."
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.deletePost(postId)
            _statusMessage.value = "পোস্ট মুছে ফেলা হয়েছে। / Post deleted successfully."
            navigateBack()
        }
    }

    fun getRandomPseudonym(): String = repository.generateRandomPseudonym()

    fun updateUserProfile(
        displayName: String,
        bio: String,
        avatar: String,
        onComplete: ((Boolean) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _isSavingProfile.value = true
            try {
                val success = repository.updateUserProfile(displayName, bio, avatar)
                if (success) {
                    _statusMessage.value = if (_language.value == AppLanguage.BANGLA)
                        "প্রোফাইল সফলভাবে আপডেট হয়েছে! 🎉"
                    else
                        "Profile updated successfully in Firestore! 🎉"
                    _userProfile.value = _userProfile.value?.copy(
                        displayName = displayName,
                        bio = bio,
                        avatar = avatar
                    ) ?: UserProfile(
                        userId = Firebase.auth.currentUser?.uid ?: "",
                        displayName = displayName,
                        bio = bio,
                        avatar = avatar
                    )
                } else {
                    _statusMessage.value = if (_language.value == AppLanguage.BANGLA)
                        "প্রোফাইল সংরক্ষণ ব্যর্থ হয়েছে। অনুগ্রহ করে আবার চেষ্টা করুন।"
                    else
                        "Failed to update profile. Please try again."
                }
                onComplete?.invoke(success)
            } finally {
                _isSavingProfile.value = false
            }
        }
    }
}
