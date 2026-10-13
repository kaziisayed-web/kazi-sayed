package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MoodTag
import com.example.data.model.PostType
import com.example.data.model.VentMode
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSupport
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseEmotional
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.VioletPrimary
import com.example.util.AppLanguage
import com.example.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreatePostScreen(
    initialType: String = "VENT",
    language: AppLanguage,
    isAiThinking: Boolean,
    aiSuggestion: String?,
    onBack: () -> Unit,
    onGeneratePseudonym: () -> String,
    onAssistWriting: (String) -> Unit,
    onClearSuggestion: () -> Unit,
    onSubmitPost: (
        title: String,
        content: String,
        postType: PostType,
        category: String,
        moodTag: MoodTag,
        ventMode: VentMode,
        isAnonymous: Boolean,
        pseudonym: String,
        priceQuoted: Double,
        priceFair: Double,
        priceLocation: String,
        priceCondition: String,
        brandModel: String,
        pollQuestion: String,
        pollOptions: List<String>
    ) -> Unit
) {
    BackHandler { onBack() }

    var selectedType by remember {
        mutableStateOf(
            when (initialType) {
                "PRICE_TALK" -> PostType.PRICE_TALK
                "DISCUSSION" -> PostType.DISCUSSION
                else -> PostType.VENT
            }
        )
    }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(selectedType == PostType.VENT) }
    var pseudonym by remember { mutableStateOf(onGeneratePseudonym()) }

    // Vent options
    var selectedMood by remember { mutableStateOf(MoodTag.FRUSTRATED) }
    var selectedVentMode by remember { mutableStateOf(VentMode.JUST_LISTEN) }

    // Price options
    var priceQuoted by remember { mutableStateOf("") }
    var priceFair by remember { mutableStateOf("") }
    var priceLocation by remember { mutableStateOf("Dhaka") }
    var priceCondition by remember { mutableStateOf("New") }
    var brandModel by remember { mutableStateOf("") }

    // Discussion & Poll options
    var isPollEnabled by remember { mutableStateOf(false) }
    var pollQuestion by remember { mutableStateOf("") }
    val pollOptions = remember { mutableStateListOf("Option 1", "Option 2") }

    // Category
    val availableCategories = when (selectedType) {
        PostType.VENT -> listOf(
            AppStrings.catWork(language),
            AppStrings.catStudy(language),
            AppStrings.catFamily(language),
            AppStrings.catFriendship(language),
            AppStrings.catDailyLife(language),
            AppStrings.catOther(language)
        )
        PostType.DISCUSSION -> listOf(
            AppStrings.catFamily(language),
            AppStrings.catWork(language),
            AppStrings.catStudy(language),
            AppStrings.catFriendship(language),
            AppStrings.catDailyLife(language),
            AppStrings.catOther(language)
        )
        PostType.PRICE_TALK -> listOf(
            AppStrings.catElectronics(language),
            AppStrings.catServices(language),
            AppStrings.catDailyLife(language),
            AppStrings.catOther(language)
        )
    }
    var selectedCategory by remember { mutableStateOf(availableCategories.first()) }

    // Apply AI suggestion if present
    if (aiSuggestion != null) {
        content = aiSuggestion
        onClearSuggestion()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppStrings.createPost(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Post Type Selector Tabs
            TabRow(
                selectedTabIndex = selectedType.ordinal,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = VioletPrimary,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedType == PostType.VENT,
                    onClick = {
                        selectedType = PostType.VENT
                        isAnonymous = true
                        selectedCategory = AppStrings.catWork(language)
                    },
                    text = { Text(AppStrings.ventPost(language), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedType == PostType.DISCUSSION,
                    onClick = {
                        selectedType = PostType.DISCUSSION
                        selectedCategory = AppStrings.catFamily(language)
                    },
                    text = { Text(AppStrings.discussionPost(language), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedType == PostType.PRICE_TALK,
                    onClick = {
                        selectedType = PostType.PRICE_TALK
                        selectedCategory = AppStrings.catElectronics(language)
                    },
                    text = { Text(AppStrings.pricePost(language), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Anonymous Identity Switcher Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.anonymous(language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = AppStrings.anonymousDesc(language),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            checked = isAnonymous,
                            onCheckedChange = { isAnonymous = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = VioletPrimary,
                                checkedTrackColor = VioletPrimary.copy(alpha = 0.3f)
                            )
                        )
                    }

                    if (isAnonymous) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(VioletPrimary.copy(alpha = 0.12f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(text = "🎭", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "আপনার বেনামী ছদ্মনাম:" else "Your Pseudonym:",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = pseudonym,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VioletPrimary
                                )
                            }
                            IconButton(
                                onClick = { pseudonym = onGeneratePseudonym() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "New Pseudonym",
                                    tint = VioletPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Vent Specific Controls: Mood Picker & Mode (Just Listen vs Advice Welcome)
            if (selectedType == PostType.VENT) {
                Text(
                    text = if (language == AppLanguage.BANGLA) "আপনার বর্তমান আবেগ / মুড:" else "Your Current Mood:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val moods = listOf(
                        MoodTag.ANGRY,
                        MoodTag.FRUSTRATED,
                        MoodTag.SAD,
                        MoodTag.CONFUSED,
                        MoodTag.NEED_SUPPORT
                    )
                    moods.forEach { mood ->
                        val isSelected = selectedMood == mood
                        val label = if (language == AppLanguage.BANGLA) mood.labelBn else mood.labelEn
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) RoseEmotional else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { selectedMood = mood }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${mood.emoji} $label",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.BANGLA) "অন্যদের কাছে কী প্রত্যাশা করেন?" else "What do you need right now?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedVentMode == VentMode.JUST_LISTEN) SkyAccent.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                1.dp,
                                if (selectedVentMode == VentMode.JUST_LISTEN) SkyAccent else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedVentMode = VentMode.JUST_LISTEN }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = AppStrings.justListen(language),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedVentMode == VentMode.JUST_LISTEN) SkyAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedVentMode == VentMode.ADVICE_WELCOME) IndigoAccent.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                1.dp,
                                if (selectedVentMode == VentMode.ADVICE_WELCOME) IndigoAccent else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedVentMode = VentMode.ADVICE_WELCOME }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = AppStrings.adviceWelcome(language),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedVentMode == VentMode.ADVICE_WELCOME) IndigoAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Price Talk Specific Fields
            if (selectedType == PostType.PRICE_TALK) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = priceQuoted,
                        onValueChange = { priceQuoted = it },
                        label = { Text(AppStrings.quotedPrice(language) + " (৳)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = priceFair,
                        onValueChange = { priceFair = it },
                        label = { Text(AppStrings.fairPrice(language) + " (৳)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = priceLocation,
                        onValueChange = { priceLocation = it },
                        label = { Text(AppStrings.location(language)) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = priceCondition,
                        onValueChange = { priceCondition = it },
                        label = { Text(AppStrings.condition(language)) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = brandModel,
                    onValueChange = { brandModel = it },
                    label = { Text(if (language == AppLanguage.BANGLA) "ব্র্যান্ড ও মডেল" else "Brand & Model") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Category Selector
            Text(
                text = if (language == AppLanguage.BANGLA) "ক্যাটাগরি নির্বাচন:" else "Category:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                availableCategories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) VioletPrimary else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 11.sp,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Post Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(if (language == AppLanguage.BANGLA) "পোস্টের শিরোনাম / মূল কথা" else "Post Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_post_title"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Post Content Input
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(if (language == AppLanguage.BANGLA) "মন খুলে বিস্তারিত লিখুন..." else "Write openly...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .testTag("input_post_content"),
                maxLines = 8
            )

            Spacer(modifier = Modifier.height(10.dp))

            // AI Writing Helper Trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(VioletPrimary.copy(alpha = 0.15f))
                        .clickable(enabled = content.isNotBlank() && !isAiThinking) {
                            onAssistWriting(content)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isAiThinking) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = VioletPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Writing Helper",
                                tint = VioletPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.aiWritingHelper(language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VioletPrimary
                        )
                    }
                }
            }

            // Discussion Poll Setup
            if (selectedType == PostType.DISCUSSION) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Switch(
                        checked = isPollEnabled,
                        onCheckedChange = { isPollEnabled = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.BANGLA) "একটি জনমত পোল (Poll) যুক্ত করুন" else "Attach a Community Poll",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (isPollEnabled) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pollQuestion,
                        onValueChange = { pollQuestion = it },
                        label = { Text(if (language == AppLanguage.BANGLA) "পোলের প্রশ্ন" else "Poll Question") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    pollOptions.forEachIndexed { idx, opt ->
                        OutlinedTextField(
                            value = opt,
                            onValueChange = { pollOptions[idx] = it },
                            label = { Text("Option ${idx + 1}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSubmitPost(
                            title,
                            content,
                            selectedType,
                            selectedCategory,
                            selectedMood,
                            selectedVentMode,
                            isAnonymous,
                            pseudonym,
                            priceQuoted.toDoubleOrNull() ?: 0.0,
                            priceFair.toDoubleOrNull() ?: 0.0,
                            priceLocation,
                            priceCondition,
                            brandModel,
                            if (isPollEnabled) pollQuestion else "",
                            if (isPollEnabled) pollOptions else emptyList()
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_post_button"),
                enabled = title.isNotBlank() && content.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (selectedType) {
                        PostType.VENT -> RoseEmotional
                        PostType.DISCUSSION -> IndigoAccent
                        PostType.PRICE_TALK -> EmeraldSupport
                    }
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = AppStrings.createPost(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
