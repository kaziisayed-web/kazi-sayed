package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PostEntity
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSupport
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseEmotional
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import com.example.data.model.UserProfile
import com.example.ui.components.GoogleSignInCard
import com.example.ui.components.ProfileAvatarView
import com.example.ui.components.UserProfileEditComponent
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ProfileScreen(
    userPosts: List<PostEntity>,
    savedPosts: List<PostEntity>,
    language: AppLanguage,
    isDarkMode: Boolean,
    onToggleLanguage: () -> Unit,
    onToggleTheme: () -> Unit,
    onOpenSavedPosts: () -> Unit,
    onOpenAdminDashboard: () -> Unit,
    onOpenGuidelines: () -> Unit,
    onOpenPremiumPlans: () -> Unit,
    onOpenCloudSync: () -> Unit,
    onGeneratePseudonym: () -> String,
    userProfile: UserProfile? = null,
    isSavingProfile: Boolean = false,
    onSaveProfile: ((displayName: String, bio: String, avatar: String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var pseudonym by remember { mutableStateOf(onGeneratePseudonym()) }
    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
    var isEditingProfile by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Firebase Google Sign In Card
        item {
            GoogleSignInCard(
                currentUser = currentUser,
                language = language,
                onAuthChanged = {
                    currentUser = Firebase.auth.currentUser
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // User Profile Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF1E1B4B),
                                Color(0xFF312E81),
                                Color(0xFF0F172A)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileAvatarView(
                            avatar = userProfile?.avatar?.ifBlank { "👤" } ?: "👤",
                            size = 60
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userProfile?.displayName?.ifBlank {
                                        currentUser?.displayName?.ifBlank { "MONKHULE Member" }
                                    } ?: currentUser?.displayName?.ifBlank { "MONKHULE Member" } ?: "Kazi Sayed",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(EmeraldSupport.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (currentUser != null) "VERIFIED" else "MEMBER",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldSupport
                                    )
                                }
                            }
                            Text(
                                text = userProfile?.email?.ifBlank { currentUser?.email } ?: currentUser?.email ?: "kaziisayed@gmail.com",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        // Edit Profile Button
                        OutlinedButton(
                            onClick = { isEditingProfile = !isEditingProfile },
                            modifier = Modifier.testTag("edit_profile_toggle_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = CyanAccent
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.6f)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isEditingProfile) {
                                    if (language == AppLanguage.BANGLA) "বন্ধ" else "Close"
                                } else {
                                    if (language == AppLanguage.BANGLA) "সম্পাদনা" else "Edit"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // User Bio (if present)
                    if (userProfile?.bio?.isNotBlank() == true) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "“${userProfile.bio}”",
                                fontSize = 12.sp,
                                color = Color(0xFFE2E8F0),
                                lineHeight = 17.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Pseudonym Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.25f))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🎭", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.BANGLA) "ডিফল্ট বেনামী ছদ্মনাম" else "Default Anonymous Pseudonym",
                                        fontSize = 10.sp,
                                        color = Color(0xFFCBD5E1)
                                    )
                                    Text(
                                        text = pseudonym,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyanAccent
                                    )
                                }
                            }

                            IconButton(
                                onClick = { pseudonym = onGeneratePseudonym() },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "New Pseudonym",
                                    tint = CyanAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProfileStatItem(
                            count = "${userPosts.size}",
                            label = if (language == AppLanguage.BANGLA) "পোস্ট" else "Posts"
                        )
                        ProfileStatItem(
                            count = "${userPosts.sumOf { it.supportiveCount }}",
                            label = if (language == AppLanguage.BANGLA) "সহমর্মিতা" else "Support"
                        )
                        ProfileStatItem(
                            count = "${savedPosts.size}",
                            label = if (language == AppLanguage.BANGLA) "সংরক্ষিত" else "Saved"
                        )
                    }
                }
            }
        }

        // Editable User Profile Component
        item {
            AnimatedVisibility(visible = isEditingProfile) {
                Column {
                    Spacer(modifier = Modifier.height(14.dp))
                    UserProfileEditComponent(
                        currentProfile = userProfile ?: UserProfile(
                            userId = currentUser?.uid ?: "",
                            displayName = currentUser?.displayName ?: "MONKHULE Member",
                            email = currentUser?.email ?: "",
                            bio = "",
                            avatar = "👤"
                        ),
                        isSaving = isSavingProfile,
                        language = language,
                        onSaveProfile = { newName, newBio, newAvatar ->
                            onSaveProfile?.invoke(newName, newBio, newAvatar)
                            isEditingProfile = false
                        },
                        onCancel = { isEditingProfile = false }
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Settings & Shortcuts
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Saved Posts
                    ProfileSettingRow(
                        icon = Icons.Default.Bookmark,
                        iconTint = VioletPrimary,
                        title = if (language == AppLanguage.BANGLA) "সংরক্ষিত পোস্টসমূহ (${savedPosts.size})" else "Saved Bookmarks (${savedPosts.size})",
                        subtitle = if (language == AppLanguage.BANGLA) "আপনার প্রিয় পোস্ট ও আলোচনা" else "View your saved content",
                        onClick = onOpenSavedPosts
                    )

                    // Language Toggle
                    ProfileSettingRow(
                        icon = Icons.Default.Language,
                        iconTint = IndigoAccent,
                        title = if (language == AppLanguage.BANGLA) "ভাষা: বাংলা (English এ পরিবর্তন করুন)" else "Language: English (Switch to বাংলা)",
                        subtitle = "Bilingual Community Support",
                        onClick = onToggleLanguage
                    )

                    // Theme Toggle
                    ProfileSettingRow(
                        icon = Icons.Default.DarkMode,
                        iconTint = CyanAccent,
                        title = if (isDarkMode) "Dark Mode (সক্রিয়)" else "Light Mode (সক্রিয়)",
                        subtitle = "Toggle dark & light appearance",
                        onClick = onToggleTheme
                    )

                    // Cloud & Firebase Sync Info
                    ProfileSettingRow(
                        icon = Icons.Default.CloudDone,
                        iconTint = EmeraldSupport,
                        title = AppStrings.cloudSync(language),
                        subtitle = "Firestore schema & cloud deployment status",
                        onClick = onOpenCloudSync
                    )

                    // Community Guidelines
                    ProfileSettingRow(
                        icon = Icons.Default.Policy,
                        iconTint = RoseEmotional,
                        title = AppStrings.communityRules(language),
                        subtitle = "Emotional safety & anti-harassment policy",
                        onClick = onOpenGuidelines
                    )

                    // Premium Plans
                    ProfileSettingRow(
                        icon = Icons.Default.Star,
                        iconTint = Color(0xFFF59E0B),
                        title = if (language == AppLanguage.BANGLA) "মন খুলে সাপোর্টার ও প্রিমিয়াম" else "Premium Supporter Plans",
                        subtitle = "Ad-free experience & supporter badge",
                        onClick = onOpenPremiumPlans
                    )

                    // Admin Dashboard
                    ProfileSettingRow(
                        icon = Icons.Default.AdminPanelSettings,
                        iconTint = RoseEmotional,
                        title = AppStrings.adminDashboard(language),
                        subtitle = "Content moderation queue & system analytics",
                        onClick = onOpenAdminDashboard
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF94A3B8)
        )
    }
}

@Composable
fun ProfileSettingRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(text = "➔", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
    }
}
