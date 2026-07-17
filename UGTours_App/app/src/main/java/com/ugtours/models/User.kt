package com.ugtours.models

data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String = "",
    val password: String = "" // In a real app, never store plain text passwords
)
