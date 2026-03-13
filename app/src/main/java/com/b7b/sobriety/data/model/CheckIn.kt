package com.b7b.sobriety.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_ins")
data class CheckIn(
    @PrimaryKey
    val date: String,          // "YYYY-MM-DD"
    val status: String,        // "sober" or "slip"
    val mood: String? = null,  // "😊", "🙂", "😐", "😔"
    val urge: String? = null,  // "None", "Mild", "Strong", "High"
    val note: String? = null   // Journal entry text
)
