package com.example.ui.navigation

sealed class AppScreen(val route: String) {
    object Home : AppScreen("home")
    object VentZone : AppScreen("vent_zone")
    object Discussions : AppScreen("discussions")
    object PriceTalk : AppScreen("price_talk")
    object Notifications : AppScreen("notifications")
    object Profile : AppScreen("profile")

    data class PostDetail(val postId: String) : AppScreen("post_detail")
    data class CreatePost(val defaultType: String = "VENT") : AppScreen("create_post")
    object Search : AppScreen("search")
    object SavedPosts : AppScreen("saved_posts")
    object AdminDashboard : AppScreen("admin_dashboard")
    object Guidelines : AppScreen("guidelines")
    object PremiumPlans : AppScreen("premium_plans")
    object CloudSyncInfo : AppScreen("cloud_sync_info")
}
