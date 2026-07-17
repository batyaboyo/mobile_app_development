package com.uganda.learningapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "quizzes",
    foreignKeys = [
        ForeignKey(entity = WeekUnitEntity::class, parentColumns = ["id"], childColumns = ["weekId"])
    ],
    indices = [androidx.room.Index(value = ["weekId"])]
)
data class QuizEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weekId: Int,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswerIndex: Int // 0, 1, 2, 3
)
