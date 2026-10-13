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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PriceCheck
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
import com.example.data.model.PostEntity
import com.example.ui.components.PostCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSupport
import com.example.ui.theme.RoseEmotional
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings

@Composable
fun PriceTalkScreen(
    pricePosts: List<PostEntity>,
    language: AppLanguage,
    onPostClick: (String) -> Unit,
    onSupportClick: (String) -> Unit,
    onHelpfulClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onReportClick: (String) -> Unit,
    onCreatePriceReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLocation by remember { mutableStateOf<String?>(null) }
    val locations = listOf(
        if (language == AppLanguage.BANGLA) "সব এলাকা" else "All Cities",
        "Dhaka",
        "Chittagong",
        "Sylhet",
        "Rajshahi",
        "Khulna"
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("price_talk_list"),
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
                                    Color(0xFF064E3B),
                                    Color(0xFF047857),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PriceCheck,
                                contentDescription = null,
                                tint = CyanAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) "ন্যায্য দর আলোচনা (Price Talk)" else "Price Talk — Fair Pricing Community",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == AppLanguage.BANGLA)
                                "দোকানদার কি বেশি দাম চাইল? পণ্য বা সেবার ন্যায্য মূল্য যাচাই করুন। কমিউনিটির বাস্তব অভিজ্ঞতা থেকে আসল বাজার দর জানুন।"
                            else
                                "Were you overcharged? Compare quoted store rates against community-verified fair prices in BDT (৳).",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2E8F0),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Black.copy(alpha = 0.25f))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "🇧🇩 মুদ্রা: BDT (৳)" else "🇧🇩 Currency: BDT (৳)",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Black.copy(alpha = 0.25f))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "🔍 যাচাইকৃত তুলনা" else "🔍 Real Comparisons",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // Location Filters
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(locations) { loc ->
                        val isAll = loc.contains("All") || loc.contains("সব")
                        val isSelected = (isAll && selectedLocation == null) || (loc == selectedLocation)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) EmeraldSupport
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable {
                                    if (isAll) selectedLocation = null
                                    else selectedLocation = if (selectedLocation == loc) null else loc
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = loc,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Price Reports List
            val filtered = pricePosts.filter { post ->
                selectedLocation == null || post.priceLocation.contains(selectedLocation!!, ignoreCase = true)
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
                            text = if (language == AppLanguage.BANGLA) "কোনো দরদাম পোস্ট নেই" else "No price reports in this city",
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
                            onReportClick = { onReportClick(post.id) }
                        )
                    }
                }
            }
        }

        // FAB to Submit Fair Price Report
        FloatingActionButton(
            onClick = onCreatePriceReportClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 24.dp)
                .testTag("fab_create_price_report"),
            containerColor = EmeraldSupport,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Price Report")
        }
    }
}
