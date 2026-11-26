package com.example.codelingo

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// Duolingo-inspired Dark Palette
val DuoGreen = Color(0xFF58CC02)
val DuoGreenDark = Color(0xFF58A700)
val DuoBlue = Color(0xFF1CB0F6)
val DuoBlueDark = Color(0xFF1899D6)
val DuoPurple = Color(0xFFCE82FF)
val DuoPurpleDark = Color(0xFFA55EEA)
val DuoRed = Color(0xFFFF4B4B)
val DuoRedDark = Color(0xFFEA2B2B)
val DuoGold = Color(0xFFFFC800)
val DuoGoldDark = Color(0xFFE5A500)
val BgDark = Color(0xFF131F24)
val CardDark = Color(0xFF202F36)
val BorderDark = Color(0xFF37464F)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: GameViewModel = viewModel()
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                vm.loadProgress(context)
            }
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = BgDark,
                    surface = CardDark,
                    primary = DuoGreen
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgDark
                ) {
                    CodeLingoApp(vm)
                }
            }
        }
    }
}

fun playFeedbackTone(success: Boolean) {
    try {
        val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
        if (success) {
            toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 160)
        } else {
            toneGen.startTone(ToneGenerator.TONE_PROP_NACK, 240)
        }
    } catch (_: Exception) {
    }
}

@Composable
fun TactileButton(
    text: String,
    mainColor: Color,
    shadowColor: Color,
    textColor: Color = Color.White,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val activeMain = if (enabled) mainColor else BorderDark
    val activeShadow = if (enabled) shadowColor else Color(0xFF253138)
    val activeText = if (enabled) textColor else Color.Gray

    Box(
        modifier = modifier
            .height(54.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(activeShadow)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(bottom = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(activeMain),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = activeText,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
fun CodeLingoApp(vm: GameViewModel) {
    when (vm.screenState) {
        ScreenState.LANGUAGE_SELECT -> LanguageSelectScreen(vm)
        ScreenState.HOME -> HomeScreen(vm)
        ScreenState.LESSON_THEORY -> LessonTheoryScreen(vm)
        ScreenState.LESSON_PRACTICE -> LessonPracticeScreen(vm)
        ScreenState.LESSON_COMPLETE -> LessonCompleteScreen(vm)
        ScreenState.OUT_OF_HEARTS -> OutOfHeartsScreen(vm)
    }
}

// ==================== 1. DEDICATED LANGUAGE PICKER ====================
@Composable
fun LanguageSelectScreen(vm: GameViewModel) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Какой язык будешь учить?",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Выбери один язык — вся теория и практика от Junior до Senior будут строго по нему.",
            color = Color.Gray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(CurriculumRepository.courses) { course ->
                val doneCount = course.lessons.count { vm.completedLessons.contains(it.id) }
                val totalCount = course.lessons.size
                val isCurrent = vm.selectedCourseId == course.id

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vm.selectCourse(course.id, context) },
                    shape = RoundedCornerShape(18.dp),
                    color = CardDark,
                    border = BorderStroke(2.dp, if (isCurrent) DuoGreen else BorderDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(BgDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(course.icon, fontSize = 28.sp)
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = course.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp
                                )
                                if (doneCount > 0) {
                                    Text(
                                        text = "$doneCount/$totalCount",
                                        color = DuoGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = course.tagline,
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = "→",
                            color = DuoGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }
}

// ==================== 2. HOME ROADMAP (JUNIOR -> MIDDLE -> SENIOR) ====================
@Composable
fun HomeScreen(vm: GameViewModel) {
    val course = vm.getCurrentCourse()

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar: Single Selected Language Switcher + Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button to switch language cleanly
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardDark)
                    .border(2.dp, BorderDark, RoundedCornerShape(12.dp))
                    .clickable { vm.openLanguageSelector() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(course.icon, fontSize = 16.sp)
                Text(
                    text = "${course.name} ▾",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("🔥 ${vm.streak}", color = DuoGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("💎 ${vm.gems}", color = DuoBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("❤️ ${vm.hearts}", color = DuoRed, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        HorizontalDivider(color = BorderDark, thickness = 1.dp)

        // Active Course & Current Grade Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(CardDark)
                .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ТВОЙ ГРЕЙД В ${course.name.uppercase()}",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = vm.getCourseRank(course),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
            Text(
                text = "⚡ ${vm.xp} XP",
                color = DuoGold,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }

        // Progressive Path: Junior -> Middle -> Senior
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            itemsIndexed(course.lessons) { index, lesson ->
                val isCompleted = vm.completedLessons.contains(lesson.id)
                val isUnlocked = index == 0 || vm.completedLessons.contains(course.lessons[index - 1].id)
                val tierColor = Color(lesson.level.badgeColorHex)
                val tierDark = when (lesson.level) {
                    SkillLevel.JUNIOR -> DuoGreenDark
                    SkillLevel.MIDDLE -> DuoBlueDark
                    SkillLevel.SENIOR -> DuoPurpleDark
                }

                // Section Banner for Tier (Junior / Middle / Senior)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) tierColor else CardDark
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "УРОВЕНЬ ${index + 1} • ${lesson.level.title}",
                                color = if (isUnlocked) Color.White.copy(alpha = 0.9f) else Color.Gray,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = lesson.level.subtitle,
                                color = if (isUnlocked) Color.White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Text(
                            text = if (isCompleted) "✅" else if (isUnlocked) "📖" else "🔒",
                            fontSize = 20.sp
                        )
                    }
                }

                // Lesson Node
                val offsetX = when (index % 3) {
                    1 -> 36.dp
                    2 -> (-36).dp
                    else -> 0.dp
                }

                Column(
                    modifier = Modifier
                        .offset(x = offsetX)
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val nodeMain = when {
                        isCompleted -> DuoGold
                        isUnlocked -> tierColor
                        else -> CardDark
                    }
                    val nodeShadow = when {
                        isCompleted -> DuoGoldDark
                        isUnlocked -> tierDark
                        else -> BorderDark
                    }

                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .background(nodeShadow)
                            .clickable(enabled = isUnlocked) {
                                vm.startLesson(lesson)
                            }
                            .padding(bottom = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(nodeMain),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isUnlocked) lesson.icon else "🔒",
                                fontSize = 34.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = lesson.title,
                        color = if (isUnlocked) Color.White else Color.Gray,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Теория (${lesson.theorySlides.size}) + Практика (${lesson.questions.size})",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// ==================== 3. THEORY LEARNING PHASE ====================
@Composable
fun LessonTheoryScreen(vm: GameViewModel) {
    val lesson = vm.activeLesson ?: return
    val slide = lesson.theorySlides[vm.currentTheoryIndex]
    val isLastSlide = vm.currentTheoryIndex == lesson.theorySlides.lastIndex
    val progress = (vm.currentTheoryIndex + 1).toFloat() / lesson.theorySlides.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = { vm.exitToHome() }) {
                Icon(Icons.Default.Close, contentDescription = "Выйти", tint = Color.Gray)
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = DuoBlue,
                trackColor = BorderDark
            )
            Text(
                text = "📖 ${vm.currentTheoryIndex + 1}/${lesson.theorySlides.size}",
                color = DuoBlue,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        // Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(lesson.level.badgeColorHex).copy(alpha = 0.2f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "ТЕОРИЯ • ${lesson.level.title}",
                color = Color(lesson.level.badgeColorHex),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = slide.title,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 23.sp,
                lineHeight = 30.sp
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = slide.explanation,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            Spacer(Modifier.height(18.dp))

            // Code Example Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0D171B))
                    .border(2.dp, BorderDark, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = slide.codeExample,
                    color = Color(0xFFA6E22E),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }

            Spacer(Modifier.height(18.dp))

            // Pro Tip Callout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DuoGold.copy(alpha = 0.12f))
                    .border(1.dp, DuoGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text("💡", fontSize = 18.sp)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = slide.proTip,
                    color = Color(0xFFFFE58F),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (vm.currentTheoryIndex > 0) {
                TactileButton(
                    text = "НАЗАД",
                    mainColor = CardDark,
                    shadowColor = BorderDark,
                    modifier = Modifier.weight(0.38f)
                ) {
                    vm.prevTheorySlide()
                }
            }
            TactileButton(
                text = if (isLastSlide) "К ПРАКТИКЕ 🚀" else "ДАЛЕЕ",
                mainColor = if (isLastSlide) DuoGreen else DuoBlue,
                shadowColor = if (isLastSlide) DuoGreenDark else DuoBlueDark,
                modifier = Modifier.weight(1f)
            ) {
                vm.nextTheorySlide()
            }
        }
    }
}

// ==================== 4. PRACTICE PHASE ====================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LessonPracticeScreen(vm: GameViewModel) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val lesson = vm.activeLesson ?: return
    val question = lesson.questions[vm.currentQuestionIndex]
    val progress = (vm.currentQuestionIndex + (if (vm.isAnswerChecked) 1 else 0)).toFloat() / lesson.questions.size

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Progress Bar, Guidebook Peek, Hearts
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(onClick = { vm.exitToHome() }) {
                    Icon(Icons.Default.Close, contentDescription = "Выйти", tint = Color.Gray)
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = DuoGreen,
                    trackColor = BorderDark
                )
                // Quick Theory Cheat-sheet button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardDark)
                        .border(1.dp, BorderDark, RoundedCornerShape(10.dp))
                        .clickable { vm.showTheorySheet = true }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text("📖", fontSize = 15.sp)
                }
                Text(
                    text = "❤️ ${vm.hearts}",
                    color = DuoRed,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "ЗАКРЕПЛЕНИЕ • ${lesson.level.title}",
                color = DuoGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(6.dp))

            // Question Prompt
            Text(
                text = question.prompt,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                lineHeight = 27.sp
            )

            // Optional Code Snippet
            if (question.codeSnippet != null) {
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0D171B))
                        .border(2.dp, BorderDark, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = question.codeSnippet,
                        color = Color(0xFFA6E22E),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            when (question.type) {
                QuestionType.MULTIPLE_CHOICE, QuestionType.FILL_BLANK -> {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        question.options.forEach { option ->
                            val isSelected = vm.selectedOption == option
                            val borderColor = if (isSelected) DuoBlue else BorderDark
                            val bgColor = if (isSelected) DuoBlue.copy(alpha = 0.18f) else CardDark

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !vm.isAnswerChecked) {
                                        vm.selectedOption = option
                                    },
                                shape = RoundedCornerShape(16.dp),
                                color = bgColor,
                                border = BorderStroke(2.dp, borderColor)
                            ) {
                                Text(
                                    text = option,
                                    modifier = Modifier.padding(16.dp),
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                QuestionType.TOKEN_BUILDER -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 76.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardDark)
                            .border(2.dp, BorderDark, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        if (vm.builtTokens.isEmpty()) {
                            Text(
                                text = "Нажимай на блоки ниже, чтобы собрать код...",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        } else {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                vm.builtTokens.forEach { token ->
                                    TokenChip(text = token, isSelected = false) {
                                        vm.toggleToken(token)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        question.options.forEach { token ->
                            val used = vm.builtTokens.contains(token)
                            TokenChip(
                                text = token,
                                isSelected = used,
                                onClick = { vm.toggleToken(token) }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            val canCheck = when (question.type) {
                QuestionType.TOKEN_BUILDER -> vm.builtTokens.isNotEmpty()
                else -> vm.selectedOption != null
            }

            if (!vm.isAnswerChecked) {
                TactileButton(
                    text = "ПРОВЕРИТЬ",
                    mainColor = DuoGreen,
                    shadowColor = DuoGreenDark,
                    enabled = canCheck,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    vm.checkAnswer(context)
                    playFeedbackTone(vm.isLastAnswerCorrect)
                    haptic.performHapticFeedback(
                        if (vm.isLastAnswerCorrect) HapticFeedbackType.TextHandleMove
                        else HapticFeedbackType.LongPress
                    )
                }
            }
        }

        // Bottom Feedback Sheet
        if (vm.isAnswerChecked) {
            val bannerBg = if (vm.isLastAnswerCorrect) Color(0xFF1A3316) else Color(0xFF3A181A)
            val titleColor = if (vm.isLastAnswerCorrect) DuoGreen else DuoRed
            val btnMain = if (vm.isLastAnswerCorrect) DuoGreen else DuoRed
            val btnShadow = if (vm.isLastAnswerCorrect) DuoGreenDark else DuoRedDark

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(bannerBg)
                    .padding(20.dp)
            ) {
                Text(
                    text = if (vm.isLastAnswerCorrect) "Красава! Верно 🎉" else "Не совсем так 😅",
                    color = titleColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(6.dp))
                if (!vm.isLastAnswerCorrect) {
                    Text(
                        text = "Правильный ответ: ${question.correctAnswer}",
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    text = question.explanation,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(16.dp))
                TactileButton(
                    text = "ДАЛЕЕ",
                    mainColor = btnMain,
                    shadowColor = btnShadow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    vm.nextQuestion(context)
                }
            }
        }

        // Guidebook / Theory Cheat-Sheet Dialog
        if (vm.showTheorySheet) {
            AlertDialog(
                onDismissRequest = { vm.showTheorySheet = false },
                containerColor = CardDark,
                title = {
                    Text(
                        text = "📖 Шпаргалка урока",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        lesson.theorySlides.forEach { slide ->
                            Text(
                                text = slide.title,
                                color = DuoBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = slide.explanation,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BgDark)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = slide.codeExample,
                                    color = Color(0xFFA6E22E),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { vm.showTheorySheet = false }) {
                        Text("ПОНЯТНО", color = DuoGreen, fontWeight = FontWeight.ExtraBold)
                    }
                }
            )
        }
    }
}

@Composable
fun TokenChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) BorderDark else CardDark)
            .border(2.dp, BorderDark, RoundedCornerShape(12.dp))
            .clickable(enabled = !isSelected, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Transparent else Color.White,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun LessonCompleteScreen(vm: GameViewModel) {
    val lesson = vm.activeLesson
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🏆", fontSize = 76.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Уровень пройден!",
            color = DuoGold,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "${lesson?.level?.title ?: ""} • ${lesson?.title ?: ""}",
            color = Color.Gray,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(28.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatBox(title = "ОПЫТ", value = "+${lesson?.xpReward ?: 25} XP", color = DuoGold)
            StatBox(title = "КРИСТАЛЛЫ", value = "+20 💎", color = DuoBlue)
        }
        Spacer(Modifier.height(40.dp))
        TactileButton(
            text = "НА КАРТУ КУРСА",
            mainColor = DuoGreen,
            shadowColor = DuoGreenDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            vm.exitToHome()
        }
    }
}

@Composable
fun OutOfHeartsScreen(vm: GameViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("💔", fontSize = 72.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Закончились жизни!",
            color = DuoRed,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Восстанови сердца за кристаллы или вернись на главный экран.",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )
        Spacer(Modifier.height(28.dp))
        TactileButton(
            text = "ВОССТАНОВИТЬ ЗА 50 💎",
            mainColor = DuoBlue,
            shadowColor = DuoBlueDark,
            enabled = vm.gems >= 50,
            modifier = Modifier.fillMaxWidth()
        ) {
            vm.refillHearts(context)
        }
        Spacer(Modifier.height(12.dp))
        TactileButton(
            text = "БЕСПЛАТНАЯ ТРЕНИРОВКА (+5 ❤️)",
            mainColor = DuoGreen,
            shadowColor = DuoGreenDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            vm.freePracticeRefill(context)
        }
    }
}

@Composable
fun StatBox(title: String, value: String, color: Color) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CardDark)
            .border(2.dp, color, RoundedCornerShape(16.dp))
            .padding(horizontal = 22.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Spacer(Modifier.height(4.dp))
        Text(value, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
    }
}
