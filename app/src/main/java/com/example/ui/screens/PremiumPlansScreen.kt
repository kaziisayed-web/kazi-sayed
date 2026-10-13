package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSupport
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumPlansScreen(
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
                        text = if (language == AppLanguage.BANGLA) "মন খুলে সাপোর্টার প্ল্যান" else "Premium Supporter Plans",
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
                .testTag("premium_plans_screen"),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF3B0764), Color(0xFF1E1B4B))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) "কমিউনিটিকে সচল রাখতে পাশে থাকুন" else "Keep MONKHULE Ad-Free & Empathetic",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.BANGLA)
                                "সংবেদনশীল মনের ক্ষোভের মাঝে আমরা কোনো বিরক্তিকর বিজ্ঞাপন রাখি না। আপনার সামান্য অনুদান এই নিরাপদ সার্ভার চালু রাখতে সাহায্য করে।"
                            else
                                "We never place intrusive ads inside emotional support zones. Supporter subscriptions maintain server stability and free AI vent assistance for everyone.",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Plan 1: Free Tier
            item {
                PlanCard(
                    name = if (language == AppLanguage.BANGLA) "ফ্রি মেম্বার (বর্তমান)" else "Free Member (Current)",
                    price = "৳০ / সর্বদা",
                    features = listOf(
                        "সম্পূর্ণ বেনামী পোস্ট ও মন্তব্য",
                        "মন খোলা ঘর ও পাবলিক আলোচনা",
                        "ন্যায্য দর যাচাই ও তুলনা",
                        "দৈনিক AI ভেন্ট অ্যাসিস্ট্যান্ট সহায়তা"
                    ),
                    isCurrent = true,
                    buttonText = if (language == AppLanguage.BANGLA) "সক্রিয় রয়েছে" else "Active",
                    accentColor = VioletPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Plan 2: Supporter Tier
            item {
                PlanCard(
                    name = if (language == AppLanguage.BANGLA) "মন খুলে সাপোর্টার 🌸" else "MONKHULE Supporter 🌸",
                    price = "৳১৫০ / মাস",
                    features = listOf(
                        "১০০% বিজ্ঞাপনমুক্ত ব্রাউজিং",
                        "প্রোফাইলে বিশেষ সাপোর্টার ব্যাজ 🌸",
                        "সীমাহীন বুকমার্ক ও ড্রাফট সংরক্ষণ",
                        "অগ্রাধিকার ভিত্তিতে AI সাপোর্ট"
                    ),
                    isCurrent = false,
                    buttonText = if (language == AppLanguage.BANGLA) "সাপোর্ট করুন (বিকাশ / নগদ)" else "Become Supporter",
                    accentColor = IndigoAccent
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Notice about real billing
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "ℹ️ Payment & Transparency Notice",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Per Play policy and Google AI Studio standards, payment webhooks are verified server-side. No mock payments or unauthorized billing exist in the app.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlanCard(
    name: String,
    price: String,
    features: List<String>,
    isCurrent: Boolean,
    buttonText: String,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = if (!isCurrent) androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = price, fontSize = 14.sp, fontWeight = FontWeight.Black, color = accentColor)
            }

            Spacer(modifier = Modifier.height(10.dp))

            features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(text = feature, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {},
                enabled = !isCurrent,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = buttonText, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
