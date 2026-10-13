package com.example.data.local

import com.example.data.model.CommentEntity
import com.example.data.model.MoodTag
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.PostType
import com.example.data.model.VentMode

object SeedData {
    fun getInitialPosts(): List<PostEntity> {
        val now = System.currentTimeMillis()
        val hour = 3600 * 1000L

        return listOf(
            // 1. Emotional Vent - Just Listen (Work burnout / Dhaka commute)
            PostEntity(
                id = "post-vent-1",
                title = "অফিস আর জ্যামের চাপে দম বন্ধ হয়ে আসছে, কারও কাছে কিছু বলার জায়গা নেই",
                content = "প্রতিদিন মিরপুর থেকে মতিঝিল আড়াই ঘণ্টার জ্যাম, এরপর অফিসে ১০ ঘণ্টার কাজের চাপ। বাসায় ফিরে পরিবারকে সময় দেওয়া তো দূরের কথা, নিজের নিঃশ্বাস ফেলার সময় পাই না। কারো কাছে বললে বলে 'সবাই তো কষ্ট করে'। জানি সবাই করে, কিন্তু আমারও তো ক্লান্তি আসে। শুধু একটু মন হালকা করতে লিখলাম। দয়া করে কোনো সমাধান বা উপদেশ দেবেন না, শুধু একটু মন দিয়ে শুনুন।",
                authorName = "Anonymous",
                authorHandle = "anon_user",
                isAnonymous = true,
                pseudonym = "ক্লান্ত পথিক 🌧️",
                category = "Work & Career",
                postType = PostType.VENT,
                moodTag = MoodTag.FRUSTRATED,
                ventMode = VentMode.JUST_LISTEN,
                timestamp = now - 2 * hour,
                supportiveCount = 42,
                helpfulCount = 8,
                commentCount = 12,
                isBookmarked = false
            ),

            // 2. Emotional Vent - Advice Welcome (University depression / Career transition)
            PostEntity(
                id = "post-vent-2",
                title = "Struggling with post-graduation panic and fear of letting my parents down",
                content = "Graduated 6 months ago in BBA from a private university. Sent out 70+ CVs with barely two interview calls. Everyone around me on LinkedIn is announcing jobs or Masters abroad. I feel like an absolute burden to my parents who invested all their savings in my degree. I feel so guilty every time they ask how my job hunt is going. Has anyone walked through this dark phase and survived? Genuine constructive advice is welcome.",
                authorName = "Anonymous",
                authorHandle = "anon_grad",
                isAnonymous = true,
                pseudonym = "Silent Horizon 🌅",
                category = "Study & Campus",
                postType = PostType.VENT,
                moodTag = MoodTag.NEED_SUPPORT,
                ventMode = VentMode.ADVICE_WELCOME,
                timestamp = now - 5 * hour,
                supportiveCount = 57,
                helpfulCount = 29,
                commentCount = 18,
                isBookmarked = true
            ),

            // 3. Discussion with Poll (Mental Health in South Asian families)
            PostEntity(
                id = "post-disc-1",
                title = "পরিবারে মানসিক স্বাস্থ্য নিয়ে কথা বলা কি এখনো অসম্ভব ট্যাবূ?",
                content = "আমরা তরুণ প্রজন্ম যখন মানসিক অবসাদ বা অ্যাংজাইটি নিয়ে পরিবারের বড়দের সাথে খোলামেলা কথা বলতে যাই, তখন বেশিরভাগ সময়ই উত্তর আসে 'নামাজ পড়ো ঠিক হয়ে যাবে' অথবা 'তোমাদের যুগের মানুষের কোনো সহ্যশক্তি নেই'। আপনার পরিবারে কি কখনো মন খারাপের কথা স্বাভাবিকভাবে গ্রহণ করা হয়?",
                authorName = "Rafid Hasan",
                authorHandle = "rafid_h",
                isAnonymous = false,
                pseudonym = "",
                category = "Family",
                postType = PostType.DISCUSSION,
                moodTag = MoodTag.NONE,
                ventMode = VentMode.ADVICE_WELCOME,
                timestamp = now - 8 * hour,
                supportiveCount = 89,
                helpfulCount = 64,
                commentCount = 31,
                pollQuestion = "আপনার পরিবারে মানসিক স্বাস্থ্য নিয়ে খোলামেলা কথা বলার পরিবেশ আছে?",
                pollOptions = "হ্যাঁ সম্পূর্ণ খোলামেলা,মাঝে মাঝে বোঝে,একদমই না হাসাহাসি করে,চেষ্টাই করিনি কখনো",
                pollVotes = "18,42,105,67",
                userVotedOptionIndex = -1
            ),

            // 4. Price Talk (Fair Price Discussion: Laptop battery replacement in Elephant Road)
            PostEntity(
                id = "post-price-1",
                title = "Dell XPS 13 9360 Battery Replacement: এলিফ্যান্ট রোডে চেয়েছিল ৳৫,৫০০, আসল ন্যায্য দর কত?",
                content = "আমার ল্যাপটপের ব্যাটারি ব্যাকআপ একদম শেষ। মাল্টিপ্ল্যান ও এলিফ্যান্ট রোডের বেশ কয়েকটি দোকানে ঘুরেছি। এক দোকানে বলল অরিজিনাল ৳৫,৫০০, অন্য দোকানে বলল কপি ৳২,৮০০। একজন পরিচিত মেকানিক বললেন ডেল এর থার্ড পার্টি জেনুইন সেল ৩,৮০০ টাকায় পাওয়া যায়। কারও সাম্প্রতিক অভিজ্ঞতার ন্যায্য দর শেয়ার করবেন কি?",
                authorName = "Tanim Rahman",
                authorHandle = "tanim_tech",
                isAnonymous = false,
                pseudonym = "",
                category = "Electronics & Tech",
                postType = PostType.PRICE_TALK,
                moodTag = MoodTag.NONE,
                ventMode = VentMode.ADVICE_WELCOME,
                timestamp = now - 11 * hour,
                supportiveCount = 34,
                helpfulCount = 76,
                commentCount = 15,
                priceQuoted = 5500.0,
                priceFair = 3800.0,
                priceCurrency = "BDT (৳)",
                priceLocation = "Elephant Road, Dhaka",
                priceCondition = "New (OEM)",
                brandModel = "Dell XPS 13 (60Wh battery)"
            ),

            // 5. Price Talk (Fair Price Discussion: Medical Specialist Consultation Fees in Dhaka)
            PostEntity(
                id = "post-price-2",
                title = "Specialist Doctor Consultation Fees: ৳1,800 vs ৳1,200 standard across hospitals?",
                content = "Visited a top diagnostic center in Dhanmondi for an endocrinology consultation. The initial visit fee was ৳1,800 with 30-day follow-up at ৳1,000. While the doctor spent only 6 minutes, the fee felt inflated compared to standard ৳1,200. Is this normal across Dhaka or are private diagnostic centers overcharging?",
                authorName = "Anonymous",
                authorHandle = "health_seeker",
                isAnonymous = true,
                pseudonym = "Careful Patient 🩺",
                category = "Services & Repairs",
                postType = PostType.PRICE_TALK,
                moodTag = MoodTag.NONE,
                ventMode = VentMode.ADVICE_WELCOME,
                timestamp = now - 18 * hour,
                supportiveCount = 52,
                helpfulCount = 88,
                commentCount = 24,
                priceQuoted = 1800.0,
                priceFair = 1200.0,
                priceCurrency = "BDT (৳)",
                priceLocation = "Dhanmondi, Dhaka",
                priceCondition = "Service",
                brandModel = "Endocrinologist Consultation"
            ),

            // 6. Vent - Sad / Friendship betrayal
            PostEntity(
                id = "post-vent-3",
                title = "যে বন্ধুকে জীবনের সব গোপন কথা বলেছিলাম, সে-ই পেছনের কথা অন্যদের কাছে ফাঁস করল",
                content = "স্কুল জীবন থেকে ১০ বছরের বন্ধুত্ব ছিল। আমার জীবনের সবথেকে কঠিন সময়ের দুর্বলতাগুলো আমি শুধু তাকেই বিশ্বাস করে বলেছিলাম। কাল জানলাম সে আমাদের পুরো ফ্রেন্ড সার্কেলে সেসব রসিয়ে রসিয়ে বলেছে। কষ্ট লাগছে না, কেমন যেন একটা শূন্যতা আর অবিশ্বাসের অনুভূতি হচ্ছে। মানুষের উপর থেকে বিশ্বাস উঠে যাওয়া কতটা বিষাক্ত।",
                authorName = "Anonymous",
                authorHandle = "anon_friend",
                isAnonymous = true,
                pseudonym = "ভাঙা আয়না 🪞",
                category = "Friendship",
                postType = PostType.VENT,
                moodTag = MoodTag.SAD,
                ventMode = VentMode.JUST_LISTEN,
                timestamp = now - 24 * hour,
                supportiveCount = 68,
                helpfulCount = 14,
                commentCount = 20,
                isBookmarked = false
            )
        )
    }

    fun getInitialComments(): List<CommentEntity> {
        val now = System.currentTimeMillis()
        val hour = 3600 * 1000L

        return listOf(
            CommentEntity(
                id = "comment-1",
                postId = "post-vent-1",
                content = "আপনার কষ্টটা অনুভব করতে পারছি ভাই। ঢাকা শহরের এই দৈনন্দিন লড়াই মানুষকে ভেতরে ভেতরে নিঃশেষ করে দেয়। আপনি দুর্বল নন, আপনি একা নন। আজ রাতে একটু বিশ্রাম নিন। আমরা পাশে আছি। 🫂",
                authorName = "Anonymous",
                isAnonymous = true,
                pseudonym = "সহমর্মী হৃদয় 💙",
                timestamp = now - 1 * hour,
                supportiveCount = 15,
                isAiResponse = false
            ),
            CommentEntity(
                id = "comment-2",
                postId = "post-vent-1",
                content = "মন খুলে বলে খুব ভালো করেছেন। কখনো কখনো কথাগুলো কাউকে শুনিয়ে দেওয়াটাই প্রশান্তির। কোনো উপদেশ দেব না, শুধু বলব—আপনার মনের এই ঝড় একদিন কাটবেই।",
                authorName = "Tasnim Alam",
                isAnonymous = false,
                pseudonym = "",
                timestamp = now - 45 * 60 * 1000L,
                supportiveCount = 9,
                isAiResponse = false
            ),
            CommentEntity(
                id = "comment-ai-1",
                postId = "post-vent-1",
                content = "🤖 [AI সহচর]: আপনার অনুভূতিগুলো সম্পূর্ণ স্বাভাবিক ও যৌক্তিক। দীর্ঘ কর্মঘণ্টা আর ক্লান্তিকর যাতায়াত একজন মানুষের স্নায়ুর ওপর সীমাহীন চাপ ফেলে। যখন আশেপাশের মানুষ আপনার ক্লান্তিকে সাধারণীকরণ করে, তখন একাকীত্ব আরও তীব্র হয়। মনে রাখবেন, বিশ্রাম চাওয়া কোনো অপরাধ নয়। আপনি আজ বেঁচে থাকার জন্য যথেষ্ট পরিশ্রম করেছেন। নিজেকে একটু সময় দিন।",
                authorName = "MONKHULE AI",
                isAnonymous = false,
                pseudonym = "AI Vent Assistant",
                timestamp = now - 30 * 60 * 1000L,
                supportiveCount = 22,
                isAiResponse = true
            ),
            CommentEntity(
                id = "comment-3",
                postId = "post-price-1",
                content = "এলিফ্যান্ট রোডের আলপনা প্লাজা বা মাল্টিপ্ল্যানের ৩য় তলার পরিচিত ল্যাপটপ পার্টস শপগুলোতে ৳৩,৫০০ থেকে ৳৩,৮০০ এর মধ্যে ৬ মাসের রিপ্লেসমেন্ট ওয়ারেন্টি সহ পাওয়া যায়। ৫,৫০০ টাকা একদমই অযৌক্তিক অতিরিক্ত দাবি।",
                authorName = "Hasan Mahmud",
                isAnonymous = false,
                pseudonym = "",
                timestamp = now - 9 * hour,
                supportiveCount = 18,
                isAiResponse = false
            )
        )
    }

    fun getInitialNotifications(): List<NotificationEntity> {
        val now = System.currentTimeMillis()
        val hour = 3600 * 1000L

        return listOf(
            NotificationEntity(
                id = "notif-1",
                title = "নতুন সহমর্মী বার্তা 🫂",
                message = "আপনার পোস্ট 'অফিস আর জ্যামের চাপে...' এ 'সহমর্মী হৃদয়' একটি সমর্থনমূলক মন্তব্য করেছেন।",
                type = "COMMENT",
                relatedPostId = "post-vent-1",
                timestamp = now - 1 * hour,
                isRead = false
            ),
            NotificationEntity(
                id = "notif-2",
                title = "পাশে আছি প্রতিক্রিয়া ❤️",
                message = "১০+ জন সদস্য আপনার সাম্প্রতিক ভেন্টে 'পাশে আছি' প্রতিক্রিয়া জানিয়েছেন।",
                type = "SUPPORT",
                relatedPostId = "post-vent-1",
                timestamp = now - 3 * hour,
                isRead = false
            ),
            NotificationEntity(
                id = "notif-3",
                title = "মন খুলে কমিউনিটি বার্তা 🌸",
                message = "মন খুলে অ্যাপে আপনাকে স্বাগতম! এখানে আপনি সম্পূর্ণ বেনামে মন খুলে কথা বলতে পারেন। কোনো বিচার নেই, শুধু শোনার মানুষ।",
                type = "ADMIN",
                relatedPostId = "",
                timestamp = now - 24 * hour,
                isRead = true
            )
        )
    }
}
