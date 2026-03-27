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
    val previousTotal: Int = 0
)

class QuizViewModel(private val repository: BibleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val dateKey: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    init {
        checkExistingQuiz()
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
