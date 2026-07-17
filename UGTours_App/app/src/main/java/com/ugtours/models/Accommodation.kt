package com.ugtours.models

data class Accommodation(
    val name: String,
    val type: String,
    val priceRange: String,
    val contact: String,
    val distanceFromAttraction: String,
    val rating: Float = 4.0f
)
