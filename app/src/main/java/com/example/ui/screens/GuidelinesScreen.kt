package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSupport
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseEmotional
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidelinesScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppStrings.communityRules(language),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("guidelines_screen"),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = if (language == AppLanguage.BANGLA)
                        "মন খুলে (MONKHULE) কমিউনিটি অঙ্গীকার"
                    else
                        "MONKHULE Community Pledge",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == AppLanguage.BANGLA)
                        "এখানে আমরা একে অপরকে শুনি, সম্মান করি এবং বিচার করি না।"
                    else
                        "A sanctuary where we listen with empathy, protect anonymity, and respect boundaries.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Rule 1: Just Listen & Empathy
            item {
                GuidelineCard(
                    title = if (language == AppLanguage.BANGLA) "১. শুধু শুনুন — উপদেশ নয়, সহমর্মিতা" else "1. Empathy over Unsolicited Advice",
                    description = if (language == AppLanguage.BANGLA)
                        "যখন কোনো ভেন্ট লেখক 'শুধু শুনুন 👂' নির্বাচন করেন, তখন অযাচিত উপদেশ বা লেকচার দেওয়া সম্পূর্ণ নিষিদ্ধ। শুধু পাশে থাকুন, সহমর্মিতা জানান।"
                    else
                        "When an author selects 'Just Listen 👂', unsolicited advice is discouraged. Offer emotional presence and validation.",
                    color = VioletPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Rule 2: Anonymity & Privacy
            item {
                GuidelineCard(
                    title = if (language == AppLanguage.BANGLA) "২. সম্পূর্ণ বেনামী পরিচয় রক্ষা" else "2. Privacy & Relative Anonymity",
                    description = if (language == AppLanguage.BANGLA)
                        "বেনামী লেখকদের আসল পরিচয় খোঁজার চেষ্টা (Doxxing) কঠোরভাবে নিষিদ্ধ। অন্য ব্যবহারকারীদের ব্যক্তিগত তথ্য প্রকাশ করবেন না।"
                    else
                        "Never attempt to unmask, trace, or expose an anonymous poster's identity. Doxxing results in immediate account suspension.",
                    color = IndigoAccent
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Rule 3: Constructive Venting vs Abuse
            item {
                GuidelineCard(
                    title = if (language == AppLanguage.BANGLA) "৩. আবেগ প্রকাশ বনাম গালিগালাজ" else "3. Raw Emotion vs Targeted Harassment",
                    description = if (language == AppLanguage.BANGLA)
                        "কষ্ট ও ক্ষোভ প্রকাশের সাধারণ আবেগ গ্রহণযোগ্য, কিন্তু কাউকে সরাসরি হুমকি, ব্যক্তিগত আক্রমণ, বা ঘৃণা ছড়ানো নিষিদ্ধ।"
                    else
                        "Ordinary profanity as emotional expression is permitted; targeted harassment, threats of violence, or hate speech are strictly prohibited.",
                    color = RoseEmotional
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Rule 4: Price Talk Integrity
            item {
                GuidelineCard(
                    title = if (language == AppLanguage.BANGLA) "৪. ন্যায্য বাজার দর ও সততা" else "4. Fair Price Integrity",
                    description = if (language == AppLanguage.BANGLA)
                        "ন্যায্য দর আলোচনায় বিভ্রান্তিকর মিথ্যা দাম প্রচার করবেন না। বাস্তব বাজার অভিজ্ঞতা থেকে সত্য মূল্য শেয়ার করুন।"
                    else
                        "Only share authentic, real-world price quotes from verified stores and transactions in BDT (৳).",
                    color = EmeraldSupport
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Emergency Helplines Bangladesh
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = RoseEmotional.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = RoseEmotional)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) "জরুরি মানসিক স্বাস্থ্য হেল্পলাইন" else "Mental Health & Crisis Support",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoseEmotional
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.BANGLA)
                                "আপনি যদি চরম মানসিক সংকটে থাকেন বা নিজেকে ক্ষতি করার চিন্তা আসে, অনুগ্রহ করে সাহায্য নিন:\n• কান পেতে রই (Kaan Pete Roi): +8801779554391\n• জাতীয় জরুরি সেবা: 999 বা 333"
                            else
                                "If you or someone you know is in crisis, confidential support is available:\n• Kaan Pete Roi (Bangladesh): +8801779554391\n• National Emergency Helpline: 999 or 333",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GuidelineCard(title: String, description: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )
        }
    }
}
