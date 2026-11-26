package com.example.codelingo

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class ScreenState {
    LANGUAGE_SELECT,
    HOME,
    LESSON_THEORY,
    LESSON_PRACTICE,
    LESSON_COMPLETE,
    OUT_OF_HEARTS
}

class GameViewModel : ViewModel() {
    var selectedCourseId by mutableStateOf<String?>(null)
    var screenState by mutableStateOf(ScreenState.LANGUAGE_SELECT)

    var xp by mutableIntStateOf(0)
    var gems by mutableIntStateOf(100)
    var hearts by mutableIntStateOf(5)
    var streak by mutableIntStateOf(1)
    val completedLessons = mutableStateListOf<String>()

    // Active lesson state
    var activeLesson by mutableStateOf<Lesson?>(null)
    var currentTheoryIndex by mutableIntStateOf(0)
    var currentQuestionIndex by mutableIntStateOf(0)
    var selectedOption by mutableStateOf<String?>(null)
    val builtTokens = mutableStateListOf<String>()

    // Answer check banner state
    var isAnswerChecked by mutableStateOf(false)
    var isLastAnswerCorrect by mutableStateOf(false)

    // In-practice theory cheat-sheet modal
    var showTheorySheet by mutableStateOf(false)

    fun loadProgress(context: Context) {
        val prefs = context.getSharedPreferences("codelingo_v3_prefs", Context.MODE_PRIVATE)
        xp = prefs.getInt("xp", 0)
        gems = prefs.getInt("gems", 100)
        hearts = prefs.getInt("hearts", 5)
        streak = prefs.getInt("streak", 1)
        val saved = prefs.getStringSet("completed", emptySet()) ?: emptySet()
        completedLessons.clear()
        completedLessons.addAll(saved)

        val savedCourse = prefs.getString("course", null)
        if (savedCourse != null && CurriculumRepository.courses.any { it.id == savedCourse }) {
            selectedCourseId = savedCourse
            screenState = ScreenState.HOME
        } else {
            selectedCourseId = null
            screenState = ScreenState.LANGUAGE_SELECT
        }
    }

    fun saveProgress(context: Context) {
        val prefs = context.getSharedPreferences("codelingo_v3_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("xp", xp)
            .putInt("gems", gems)
            .putInt("hearts", hearts)
            .putInt("streak", streak)
            .putString("course", selectedCourseId)
            .putStringSet("completed", completedLessons.toSet())
            .apply()
    }

    fun selectCourse(courseId: String, context: Context) {
        selectedCourseId = courseId
        saveProgress(context)
        screenState = ScreenState.HOME
    }

    fun openLanguageSelector() {
        activeLesson = null
        screenState = ScreenState.LANGUAGE_SELECT
    }

    fun getCurrentCourse(): Course {
        return CurriculumRepository.courses.find { it.id == selectedCourseId }
            ?: CurriculumRepository.courses.first()
    }

    fun getCourseRank(course: Course): String {
        val done = course.lessons.count { completedLessons.contains(it.id) }
        return when {
            done >= course.lessons.size -> "Senior 🏆"
            done >= 2 -> "Middle 🚀"
            done >= 1 -> "Junior+ ⚡"
            else -> "Junior 🌱"
        }
    }

    fun startLesson(lesson: Lesson) {
        if (hearts <= 0) {
            screenState = ScreenState.OUT_OF_HEARTS
            return
        }
        activeLesson = lesson
        currentTheoryIndex = 0
        currentQuestionIndex = 0
        showTheorySheet = false
        resetQuestionInput()
        // Always teach first, then practice!
        screenState = if (lesson.theorySlides.isNotEmpty()) {
            ScreenState.LESSON_THEORY
        } else {
            ScreenState.LESSON_PRACTICE
        }
    }

    fun nextTheorySlide() {
        val lesson = activeLesson ?: return
        if (currentTheoryIndex + 1 < lesson.theorySlides.size) {
            currentTheoryIndex++
        } else {
            screenState = ScreenState.LESSON_PRACTICE
        }
    }

    fun prevTheorySlide() {
        if (currentTheoryIndex > 0) {
            currentTheoryIndex--
        }
    }

    private fun resetQuestionInput() {
        selectedOption = null
        builtTokens.clear()
        isAnswerChecked = false
        isLastAnswerCorrect = false
    }

    fun toggleToken(token: String) {
        if (isAnswerChecked) return
        if (builtTokens.contains(token)) {
            builtTokens.remove(token)
        } else {
            builtTokens.add(token)
        }
    }

    fun checkAnswer(context: Context) {
        val lesson = activeLesson ?: return
        val question = lesson.questions[currentQuestionIndex]

        val correct = when (question.type) {
            QuestionType.TOKEN_BUILDER -> builtTokens.toList() == question.correctTokens
            QuestionType.MULTIPLE_CHOICE, QuestionType.FILL_BLANK -> selectedOption == question.correctAnswer
        }

        isAnswerChecked = true
        isLastAnswerCorrect = correct

        if (!correct) {
            hearts = (hearts - 1).coerceAtLeast(0)
            saveProgress(context)
        }
    }

    fun nextQuestion(context: Context) {
        if (hearts <= 0) {
            screenState = ScreenState.OUT_OF_HEARTS
            return
        }
        val lesson = activeLesson ?: return
        if (currentQuestionIndex + 1 < lesson.questions.size) {
            currentQuestionIndex++
            resetQuestionInput()
        } else {
            xp += lesson.xpReward
            gems += 20
            if (!completedLessons.contains(lesson.id)) {
                completedLessons.add(lesson.id)
            }
            saveProgress(context)
            screenState = ScreenState.LESSON_COMPLETE
        }
    }

    fun refillHearts(context: Context): Boolean {
        if (gems >= 50) {
            gems -= 50
            hearts = 5
            saveProgress(context)
            if (activeLesson != null && screenState == ScreenState.OUT_OF_HEARTS) {
                screenState = ScreenState.LESSON_PRACTICE
            }
            return true
        }
        return false
    }

    fun freePracticeRefill(context: Context) {
        hearts = 5
        saveProgress(context)
        screenState = ScreenState.HOME
    }

    fun exitToHome() {
        activeLesson = null
        showTheorySheet = false
        screenState = ScreenState.HOME
    }
}
