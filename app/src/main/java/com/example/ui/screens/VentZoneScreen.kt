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
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.data.model.MoodTag
import com.example.data.model.PostEntity
import com.example.data.model.VentMode
import com.example.ui.components.PostCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseEmotional
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings

@Composable
fun VentZoneScreen(
    ventPosts: List<PostEntity>,
    language: AppLanguage,
    selectedMood: MoodTag?,
    onMoodSelected: (MoodTag?) -> Unit,
    onPostClick: (String) -> Unit,
    onSupportClick: (String) -> Unit,
    onHelpfulClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onReportClick: (String) -> Unit,
    onCreateVentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedModeFilter by remember { mutableStateOf<VentMode?>(null) }

    val moodOptions = listOf(
        MoodTag.ANGRY,
        MoodTag.FRUSTRATED,
        MoodTag.SAD,
        MoodTag.CONFUSED,
        MoodTag.NEED_SUPPORT
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("vent_zone_list"),
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
                                    Color(0xFF4C1D95),
                                    Color(0xFF831843),
                                    Color(0xFF1E1B4B)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🌧️", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) "মন খোলা ঘর (Vent Zone)" else "Vent Zone — Emotional Release",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == AppLanguage.BANGLA)
                                "কষ্ট, রাগ, অভিমান আর একাকীত্ব চেপে রাখবেন না। সম্পূর্ণ বেনামে নিজের মনের মেঘ ঝেড়ে ফেলুন। কেউ আপনার নাম জানবে না, কোনো বিচার হবে না।"
                            else
                                "Express sadness, anger, frustration, or everyday burnout completely anonymously. No identities revealed, no judgments made.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFF1F5F9),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // AI Vent Assistant Notice
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.25f))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "AI Assistant",
                                    tint = CyanAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.BANGLA)
                                        "🤖 AI সহচর প্রতিটি ভেন্টে সহমর্মী প্রতিক্রিয়া জানাতে প্রস্তুত।"
                                    else
                                        "🤖 Empathetic AI Assistant is available to listen and reflect.",
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Mode Toggle: All vs Just Listen vs Advice Welcome
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModeFilterPill(
                        label = if (language == AppLanguage.BANGLA) "সব ভেন্ট" else "All Vents",
                        isSelected = selectedModeFilter == null,
                        onClick = { selectedModeFilter = null },
                        modifier = Modifier.weight(1f)
                    )
                    ModeFilterPill(
                        label = AppStrings.justListen(language),
                        isSelected = selectedModeFilter == VentMode.JUST_LISTEN,
                        onClick = { selectedModeFilter = VentMode.JUST_LISTEN },
                        modifier = Modifier.weight(1f)
                    )
                    ModeFilterPill(
                        label = AppStrings.adviceWelcome(language),
                        isSelected = selectedModeFilter == VentMode.ADVICE_WELCOME,
                        onClick = { selectedModeFilter = VentMode.ADVICE_WELCOME },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Mood Filter Chips
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(moodOptions) { mood ->
                        val isSelected = selectedMood == mood
                        val label = if (language == AppLanguage.BANGLA) mood.labelBn else mood.labelEn

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) RoseEmotional
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { onMoodSelected(mood) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${mood.emoji} $label",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Vents List
            val filteredVents = ventPosts.filter { post ->
                val matchesMood = selectedMood == null || post.moodTag == selectedMood
                val matchesMode = selectedModeFilter == null || post.ventMode == selectedModeFilter
                matchesMood && matchesMode
            }

            if (filteredVents.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🌧️", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) "কোনো ভেন্ট পাওয়া যায়নি" else "No vents in this mood filter",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            } else {
                items(filteredVents, key = { it.id }) { post ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        PostCard(
                            post = post,
                            language = language,
                            onCardClick = { onPostClick(post.id) },
                            onSupportClick = { onSupportClick(post.id) },
                            onHelpfulClick = { onHelpfulClick(post.id) },
                            onBookmarkClick = { onBookmarkClick(post.id) },
                            onReportClick = { onReportClick(post.id) }
                        )
                    }
                }
            }
        }

        // FAB to Write a Vent
        FloatingActionButton(
            onClick = onCreateVentClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 24.dp)
                .testTag("fab_create_vent"),
            containerColor = RoseEmotional,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create Vent")
        }
    }
}

@Composable
fun ModeFilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) VioletPrimary
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
