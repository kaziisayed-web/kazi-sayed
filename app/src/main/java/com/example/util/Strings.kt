package com.example.util

enum class AppLanguage(val code: String, val label: String) {
    ENGLISH("en", "English"),
    BANGLA("bn", "বাংলা")
}

object AppStrings {
    fun appName() = "MONKHULE"
    fun tagline(lang: AppLanguage) = if (lang == AppLanguage.BANGLA)
        "মন খুলে বলো, এখানে বিচার নেই, শুধু শোনার মানুষ।"
    else
        "Speak. Share. Feel Better."

    fun home(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "হোম" else "Home"
    fun ventZone(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মন খোলা ঘর" else "Vent Zone"
    fun discussions(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আলোচনা" else "Discussions"
    fun priceTalk(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ন্যায্য দর" else "Price Talk"
    fun notifications(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিজ্ঞপ্তি" else "Alerts"
    fun profile(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রোফাইল" else "Profile"

    // Feed tabs
    fun latest(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নতুন" else "Latest"
    fun trending(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আলোচিত" else "Trending"
    fun following(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ফলো করছেন" else "Following"

    // Post types
    fun ventPost(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মনের কথা / ক্ষোভ" else "Emotional Vent"
    fun discussionPost(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "উন্মুক্ত আলোচনা" else "Public Discussion"
    fun pricePost(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পণ্যের দর যাচাই" else "Fair Price Talk"

    // Vent modes
    fun justListen(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "শুধু শুনুন 👂" else "Just Listen 👂"
    fun adviceWelcome(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পরামর্শ চাই 💡" else "Advice Welcome 💡"
    fun justListenNotice(lang: AppLanguage) = if (lang == AppLanguage.BANGLA)
        "লেখক চান শুধু মন দিয়ে শুনতে। অনুগ্রহ করে অযাচিত উপদেশ না দিয়ে সহমর্মিতা জানান।"
    else
        "The author chose 'Just Listen'. Please offer empathy and emotional presence instead of unsolicited advice."

    // Moods
    fun moodAngry(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্রুদ্ধ / রাগ 😠" else "Angry 😠"
    fun moodFrustrated(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "হতাশ / বিরক্ত 😤" else "Frustrated 😤"
    fun moodSad(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মন খারাপ 😢" else "Sad 😢"
    fun moodConfused(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিভ্রান্ত 🤔" else "Confused 🤔"
    fun moodNeedSupport(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পাশে চাই 🫂" else "Need Support 🫂"

    // Categories
    fun catWork(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "চাকরি ও ক্যারিয়ার" else "Work & Career"
    fun catStudy(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পড়াশোনা ও ক্যাম্পাস" else "Study & Campus"
    fun catFamily(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পরিবার" else "Family"
    fun catFriendship(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বন্ধুত্ব ও সম্পর্ক" else "Friendship"
    fun catDailyLife(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রতিদিনের জীবন" else "Daily Life"
    fun catElectronics(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইলেকট্রনিক্স ও গ্যাজেট" else "Electronics & Tech"
    fun catServices(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সেবা ও সার্ভিস" else "Services & Repairs"
    fun catOther(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অন্যান্য" else "Other"

    // Actions
    fun createPost(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পোস্ট করুন" else "Create Post"
    fun share(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "শেয়ার" else "Share"
    fun bookmark(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সংরক্ষিত" else "Bookmark"
    fun report(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "রিপোর্ট" else "Report"
    fun support(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পাশে আছি" else "Support"
    fun helpful(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "উপকারী" else "Helpful"
    fun comments(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মন্তব্য" else "Comments"
    fun writeComment(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সহমর্মী মন্তব্য লিখুন..." else "Write a supportive reply..."
    fun postReply(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পাঠান" else "Send"
    fun anonymous(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বেনামী পরিচয়ে প্রকাশ" else "Post Anonymously"
    fun anonymousDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA)
        "আপনার আসল পরিচয় ও ইউজারনেম কেউ দেখতে পাবে না।"
    else
        "Your real account identity will be hidden. A pseudonym will be assigned."

    // AI Vent Assistant
    fun aiAssistant(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "AI সহানুভূতিশীল সহচর" else "AI Vent Assistant"
    fun aiReflect(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "AI দিয়ে সহমর্মী পরামর্শ নিন" else "Get Empathetic AI Reflection"
    fun aiWritingHelper(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "AI দিয়ে লেখা গুছিয়ে দিন" else "Organize with AI"
    fun aiDisclaimer(lang: AppLanguage) = if (lang == AppLanguage.BANGLA)
        "AI সহচর একজন সহমর্মী শ্রোতা মাত্র; এটি পেশাদার থেরাপি বা চিকিৎসকের বিকল্প নয়।"
    else
        "AI Vent Assistant is an empathetic listener and reflective companion, not a therapist or licensed professional."

    // Price Talk
    fun quotedPrice(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "দোকানের চাওয়া দর" else "Quoted Price"
    fun fairPrice(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ন্যায্য প্রস্তাবিত দর" else "Suggested Fair Price"
    fun priceDiff(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সাশ্রয় / ব্যবধান" else "Price Difference"
    fun location(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "এলাকা / শহর" else "Region / City"
    fun condition(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পণ্যের অবস্থা" else "Condition"

    // Search
    fun searchPlaceholder(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মন খুলে খুঁজুন (পোস্ট, ক্ষোভ, দরদাম)..." else "Search vents, discussions, prices..."

    // Guidelines & Safety
    fun communityRules(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কমিউনিটি নীতিমালা" else "Community Guidelines"
    fun adminDashboard(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডমিন ড্যাশবোর্ড" else "Admin Dashboard"
    fun cloudSync(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্লাউড ও ফায়ারবেস ব্যাকআপ" else "Cloud & Firebase Sync"

    // Authentication
    fun login(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লগইন" else "Sign In"
    fun register(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "রেজিস্ট্রেশন" else "Create Account"
    fun logout(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লগআউট" else "Sign Out"
    fun email(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইমেইল ঠিকানা" else "Email Address"
    fun password(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পাসওয়ার্ড" else "Password"
    fun forgotPassword(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পাসওয়ার্ড ভুলে গেছেন?" else "Forgot Password?"
    fun resetPassword(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পাসওয়ার্ড রিসেট" else "Reset Password"
    fun sendResetLink(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "রিসেট লিংক পাঠান" else "Send Reset Link"
    fun fullName(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার নাম" else "Display Name"

    // Post edit & delete
    fun editPost(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পোস্ট সম্পাদনা" else "Edit Post"
    fun deletePost(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পোস্ট মুছুন" else "Delete Post"
    fun confirmDelete(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনি কি নিশ্চিতভাবে এই পোস্টটি মুছে ফেলতে চান?" else "Are you sure you want to delete this post?"
    fun cancel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বাতিল" else "Cancel"
    fun save(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সংরক্ষণ করুন" else "Save Changes"

    // Profile Edit Strings
    fun editProfile(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রোফাইল সম্পাদনা" else "Edit Profile"
    fun bio(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বায়ো / পরিচয়" else "Bio / About You"
    fun avatar(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাভাটার ছবি" else "Avatar Image"
    fun chooseAvatar(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাভাটার নির্বাচন করুন" else "Choose Avatar"
    fun customAvatarUrl(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কাস্টম ছবির URL" else "Custom Image URL"
    fun bioPlaceholder(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার সম্পর্কে সংক্ষেপে কিছু লিখুন..." else "Write a short bio about yourself..."
    fun profileSavedSuccess(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রোফাইল সফলভাবে সংরক্ষিত হয়েছে!" else "Profile saved to Firestore!"
}
