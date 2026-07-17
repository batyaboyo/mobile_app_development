package com.uganda.learningapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(entity = WeekUnitEntity::class, parentColumns = ["id"], childColumns = ["weekId"])
    ],
    indices = [androidx.room.Index(value = ["weekId"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weekId: Int,
    val description: String,
    val isCompleted: Boolean = false
)
