package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PriceChange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.AppScreen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.RoseEmotional
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings

data class NavItem(
    val screen: AppScreen,
    val titleEn: String,
    val titleBn: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun MonkhuleBottomBar(
    currentScreen: AppScreen,
    language: AppLanguage,
    unreadNotifCount: Int,
    onTabSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(
            screen = AppScreen.Home,
            titleEn = "Home",
            titleBn = "হোম",
            activeIcon = Icons.Filled.Home,
            inactiveIcon = Icons.Outlined.Home
        ),
        NavItem(
            screen = AppScreen.VentZone,
            titleEn = "Vent Zone",
            titleBn = "মন খোলা",
            activeIcon = Icons.Filled.ChatBubbleOutline,
            inactiveIcon = Icons.Outlined.ChatBubbleOutline
        ),
        NavItem(
            screen = AppScreen.Discussions,
            titleEn = "Discuss",
            titleBn = "আলোচনা",
            activeIcon = Icons.Filled.Forum,
            inactiveIcon = Icons.Outlined.Forum
        ),
        NavItem(
            screen = AppScreen.PriceTalk,
            titleEn = "Price Talk",
            titleBn = "ন্যায্য দর",
            activeIcon = Icons.Filled.PriceChange,
            inactiveIcon = Icons.Outlined.PriceChange
        ),
        NavItem(
            screen = AppScreen.Notifications,
            titleEn = "Alerts",
            titleBn = "বিজ্ঞপ্তি",
            activeIcon = Icons.Filled.Notifications,
            inactiveIcon = Icons.Outlined.Notifications,
            badgeCount = unreadNotifCount
        ),
        NavItem(
            screen = AppScreen.Profile,
            titleEn = "Profile",
            titleBn = "প্রোফাইল",
            activeIcon = Icons.Filled.Person,
            inactiveIcon = Icons.Outlined.Person
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen::class == item.screen::class
                val label = if (language == AppLanguage.BANGLA) item.titleBn else item.titleEn

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(item.screen) }
                        .padding(vertical = 4.dp)
                        .testTag("nav_tab_${item.titleEn.lowercase().replace(" ", "_")}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp, 28.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(VioletPrimary.copy(alpha = 0.2f))
                                )
                            }

                            Icon(
                                imageVector = if (isSelected) item.activeIcon else item.inactiveIcon,
                                contentDescription = label,
                                tint = if (isSelected) VioletPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )

                            if (item.badgeCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(x = 4.dp, y = (-2).dp)
                                        .clip(CircleShape)
                                        .background(RoseEmotional),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${item.badgeCount}",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) VioletPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
