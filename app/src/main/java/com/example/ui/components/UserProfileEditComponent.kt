package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.UserProfile
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSupport
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings

val PRESET_AVATARS = listOf(
    "👤", "🦊", "🐯", "🦉", "🌸", "⚡",
    "🚀", "🎭", "🛡️", "🌙", "🎧", "🎨",
    "🌿", "🌟", "🐱", "🐼"
)

val AVATAR_GRADIENTS = listOf(
    listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
    listOf(Color(0xFFF97316), Color(0xFFEF4444)),
    listOf(Color(0xFF06B6D4), Color(0xFF3B82F6)),
    listOf(Color(0xFF10B981), Color(0xFF059669)),
    listOf(Color(0xFFEC4899), Color(0xFFF43F5E)),
    listOf(Color(0xFF8B5CF6), Color(0xFFD946EF))
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserProfileEditComponent(
    currentProfile: UserProfile?,
    isSaving: Boolean,
    language: AppLanguage,
    onSaveProfile: (displayName: String, bio: String, avatar: String) -> Unit,
    onCancel: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var displayName by remember(currentProfile) {
        mutableStateOf(currentProfile?.displayName ?: "")
    }
    var bio by remember(currentProfile) {
        mutableStateOf(currentProfile?.bio ?: "")
    }
    var selectedAvatar by remember(currentProfile) {
        mutableStateOf(currentProfile?.avatar?.ifBlank { "👤" } ?: "👤")
    }
    var isCustomUrlMode by remember(currentProfile) {
        mutableStateOf(currentProfile?.avatar?.startsWith("http") == true)
    }
    var customImageUrl by remember(currentProfile) {
        mutableStateOf(if (currentProfile?.avatar?.startsWith("http") == true) currentProfile.avatar else "")
    }
    var validationError by remember { mutableStateOf<String?>(null) }

    val bioSuggestions = if (language == AppLanguage.BANGLA) listOf(
        "মনের কথা খোলাখুলি শেয়ার করি 💭",
        "সহানুভূতিশীল ও ইতিবাচক মনোভাব ✨",
        "বই পড়া ও আড্ডা দেওয়া পছন্দ করি 📚",
        "প্রযুক্তি ও গ্যাজেট নিয়ে উৎসাহী 📱",
        "নীরব শ্রোতা ও সহায়তাকারী 🫂"
    ) else listOf(
        "Expressing thoughts freely 💭",
        "Empathy & kindness first ✨",
        "Books, coffee & late talks 📚",
        "Tech, gadget & market enthusiast 📱",
        "Always here to listen & support 🫂"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("user_profile_edit_component"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(VioletPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = VioletPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = AppStrings.editProfile(language),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.BANGLA)
                                "Firestore 'users' কালেকশনে সংরক্ষিত হবে"
                            else
                                "Synced to 'users' Firestore collection",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (onCancel != null) {
                    IconButton(
                        onClick = onCancel,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Live Profile Preview Card
            Text(
                text = if (language == AppLanguage.BANGLA) "লাইভ প্রিভিউ (Live Preview)" else "Live Profile Preview",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyanAccent
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF1E1B4B),
                                Color(0xFF2E1065),
                                Color(0xFF0F172A)
                            )
                        )
                    )
                    .border(1.dp, CyanAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar in Preview
                    ProfileAvatarView(
                        avatar = if (isCustomUrlMode && customImageUrl.isNotBlank()) customImageUrl else selectedAvatar,
                        size = 56
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = displayName.ifBlank {
                                    if (language == AppLanguage.BANGLA) "আপনার নাম" else "Your Name"
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(EmeraldSupport.copy(alpha = 0.25f))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "VERIFIED",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSupport
                                )
                            }
                        }

                        if (currentProfile?.email?.isNotBlank() == true) {
                            Text(
                                text = currentProfile.email,
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = bio.ifBlank {
                                if (language == AppLanguage.BANGLA)
                                    "কোনো বায়ো এখনো যোগ করা হয়নি..."
                                else
                                    "No bio added yet..."
                            },
                            fontSize = 12.sp,
                            color = Color(0xFFE2E8F0),
                            maxLines = 2
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Avatar Selection Section
            Text(
                text = AppStrings.chooseAvatar(language),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Mode Selector: Presets vs Custom Image URL
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = !isCustomUrlMode,
                    onClick = { isCustomUrlMode = false },
                    label = { Text(if (language == AppLanguage.BANGLA) "অ্যাভাটার তালিকা" else "Preset Avatars") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.SentimentSatisfied,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VioletPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("preset_avatars_chip")
                )

                FilterChip(
                    selected = isCustomUrlMode,
                    onClick = { isCustomUrlMode = true },
                    label = { Text(if (language == AppLanguage.BANGLA) "কাস্টম ছবির URL" else "Image URL") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyanAccent,
                        selectedLabelColor = Color.Black
                    ),
                    modifier = Modifier.testTag("custom_url_avatar_chip")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!isCustomUrlMode) {
                // Preset Avatar FlowRow Grid
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_avatar_selector"),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PRESET_AVATARS.forEachIndexed { index, avatarEmoji ->
                        val isSelected = selectedAvatar == avatarEmoji
                        val gradient = AVATAR_GRADIENTS[index % AVATAR_GRADIENTS.size]

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(gradient))
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color.White else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    selectedAvatar = avatarEmoji
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = avatarEmoji, fontSize = 24.sp)
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldSupport),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Custom Image URL Input
                OutlinedTextField(
                    value = customImageUrl,
                    onValueChange = {
                        customImageUrl = it
                        selectedAvatar = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_avatar_url_input"),
                    label = { Text(AppStrings.customAvatarUrl(language)) },
                    placeholder = { Text("https://example.com/my-photo.jpg") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Image, contentDescription = null, tint = CyanAccent)
                    },
                    trailingIcon = {
                        if (customImageUrl.isNotBlank()) {
                            IconButton(onClick = { customImageUrl = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        focusedLabelColor = CyanAccent
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. Display Name Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.fullName(language),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${displayName.length}/50",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = displayName,
                onValueChange = {
                    if (it.length <= 50) {
                        displayName = it
                        if (it.trim().length >= 2) validationError = null
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_display_name_input"),
                placeholder = { Text("Sayed Kazi") },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = VioletPrimary)
                },
                isError = validationError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VioletPrimary,
                    focusedLabelColor = VioletPrimary
                )
            )

            if (validationError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = validationError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4. Bio Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.bio(language),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${bio.length}/300",
                    fontSize = 11.sp,
                    color = if (bio.length > 280) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = {
                    if (it.length <= 300) bio = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_bio_input"),
                placeholder = { Text(AppStrings.bioPlaceholder(language)) },
                minLines = 3,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VioletPrimary,
                    focusedLabelColor = VioletPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Bio Inspiration Chips (Horizontal Scroll)
            Text(
                text = if (language == AppLanguage.BANGLA) "বায়ো আইডিয়া ট্যাপ করুন:" else "Tap to add bio ideas:",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                bioSuggestions.forEach { suggestion ->
                    SuggestionChip(
                        onClick = {
                            bio = if (bio.isBlank()) suggestion else "$bio | $suggestion".take(300)
                        },
                        label = { Text(suggestion, fontSize = 11.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // 5. Actions (Save & Reset)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        displayName = currentProfile?.displayName ?: ""
                        bio = currentProfile?.bio ?: ""
                        selectedAvatar = currentProfile?.avatar?.ifBlank { "👤" } ?: "👤"
                        customImageUrl = if (currentProfile?.avatar?.startsWith("http") == true) currentProfile.avatar else ""
                        isCustomUrlMode = currentProfile?.avatar?.startsWith("http") == true
                        validationError = null
                    },
                    modifier = Modifier
                        .weight(0.4f)
                        .height(48.dp)
                        .testTag("profile_reset_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(AppStrings.cancel(language), fontSize = 13.sp)
                }

                Button(
                    onClick = {
                        val trimmedName = displayName.trim()
                        if (trimmedName.length < 2) {
                            validationError = if (language == AppLanguage.BANGLA)
                                "নাম কমপক্ষে ২ অক্ষরের হতে হবে।"
                            else
                                "Display name must be at least 2 characters."
                            return@Button
                        }
                        validationError = null
                        val avatarToSave = if (isCustomUrlMode && customImageUrl.isNotBlank()) {
                            customImageUrl.trim()
                        } else {
                            selectedAvatar
                        }
                        onSaveProfile(trimmedName, bio.trim(), avatarToSave)
                    },
                    enabled = !isSaving && displayName.trim().length >= 2,
                    modifier = Modifier
                        .weight(0.6f)
                        .height(48.dp)
                        .testTag("profile_save_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VioletPrimary
                    )
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (language == AppLanguage.BANGLA) "সংরক্ষণ হচ্ছে..." else "Saving...", fontSize = 13.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(AppStrings.save(language), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileAvatarView(
    avatar: String,
    size: Int = 56,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(VioletPrimary.copy(alpha = 0.5f), CyanAccent.copy(alpha = 0.5f))
                )
            )
            .border(2.dp, CyanAccent, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (avatar.startsWith("http://") || avatar.startsWith("https://")) {
            AsyncImage(
                model = avatar,
                contentDescription = "Avatar Image",
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = avatar.ifBlank { "👤" },
                fontSize = (size * 0.52).sp
            )
        }
    }
}
