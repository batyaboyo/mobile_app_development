package com.theword.app.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.theword.app.TheWordApplication
import com.theword.app.data.embedded.QuizData
import com.theword.app.data.repository.BibleRepository
import com.theword.app.domain.model.QuizAnswer
import com.theword.app.domain.model.QuizQuestion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val answers: List<QuizAnswer?> = emptyList(),
    val currentIndex: Int = 0,
    val isComplete: Boolean = false,
    val showReview: Boolean = false,
    val alreadyTaken: Boolean = false,
    val previousScore: Int = 0,
    val previousTotal: Int = 0,
    val streak: Int = 0,
    val totalPoints: Int = 0
)

class QuizViewModel(private val repository: BibleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val dateKey: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    init {
        checkExistingQuiz()
        calculateStreakAndPoints()
    }

    private fun checkExistingQuiz() {
        viewModelScope.launch {
            val existing = repository.getQuizResult(dateKey)
            if (existing != null) {
                _uiState.update {
                    it.copy(
                        alreadyTaken = true,
                        previousScore = existing.score,
                        previousTotal = existing.total
                    )
                }
            } else {
                startNewQuiz()
            }
        }
    }

    private fun calculateStreakAndPoints() {
        viewModelScope.launch {
            val allResults = repository.getAllQuizResults()
            val totalPoints = allResults.sumOf { it.score }
            
            // Streak calculation
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dates = allResults.mapNotNull { 
                try { sdf.parse(it.dateKey) } catch (e: Exception) { null } 
            }.sortedDescending()
            
            var currentStreak = 0
            if (dates.isNotEmpty()) {
                val calendar = Calendar.getInstance()
                
                // Start from today or yesterday
                val today = Calendar.getInstance().apply { 
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                
                val latestQuizDate = Calendar.getInstance().apply { 
                    time = dates[0]
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                
                // If the latest quiz was today or yesterday, we can have a streak
                val diffDays = ((today.timeInMillis - latestQuizDate.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()
                
                if (diffDays <= 1) {
                    currentStreak = 1
                    for (i in 0 until dates.size - 1) {
                        val d1 = Calendar.getInstance().apply { time = dates[i] }
                        val d2 = Calendar.getInstance().apply { time = dates[i+1] }
                        
                        // Check if d2 is exactly one day before d1
                        d1.add(Calendar.DAY_OF_YEAR, -1)
                        if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR) && 
                            d1.get(Calendar.DAY_OF_YEAR) == d2.get(Calendar.DAY_OF_YEAR)) {
                            currentStreak++
                        } else {
                            break
                        }
                    }
                }
            }
            
            _uiState.update { it.copy(streak = currentStreak, totalPoints = totalPoints) }
        }
    }

    private fun startNewQuiz() {
        // Use same seeded random as web app (based on date)
        val seed = dateKey.replace("-", "").toLongOrNull() ?: System.currentTimeMillis()
        val random = Random(seed)
        val shuffled = QuizData.questions.shuffled(random)
        val selected = shuffled.take(10)
        _uiState.update {
            it.copy(
                questions = selected,
                answers = List(10) { null },
                currentIndex = 0,
                isComplete = false,
                showReview = false,
                alreadyTaken = false
            )
        }
    }

    fun submitAnswer(index: Int) {
        val current = _uiState.value
        val question = current.questions[current.currentIndex]
        val correct = index == question.answerIndex
        val newAnswers = current.answers.toMutableList()
        newAnswers[current.currentIndex] = QuizAnswer(index, correct)

        // Show feedback immediately
        _uiState.update { it.copy(answers = newAnswers) }

        // Delay before advancing so user can see the result
        viewModelScope.launch {
            delay(1500L)
            val nextIndex = current.currentIndex + 1
            val isLast = nextIndex >= current.questions.size
            _uiState.update {
                it.copy(
                    currentIndex = if (isLast) current.currentIndex else nextIndex,
                    isComplete = isLast
                )
            }
            if (isLast) {
                saveResult(newAnswers)
            }
        }
    }

    private fun saveResult(answers: List<QuizAnswer?>) {
        viewModelScope.launch {
            val score = answers.count { it?.correct == true }
            repository.saveQuizResult(dateKey, "", "", score, answers.size)
            calculateStreakAndPoints() // Refresh streak/points
        }
    }

    fun toggleReview() {
        _uiState.update { it.copy(showReview = !it.showReview) }
    }

    val score: Int
        get() = _uiState.value.answers.count { it?.correct == true }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return QuizViewModel(TheWordApplication.instance.repository) as T
            }
        }
    }
}
