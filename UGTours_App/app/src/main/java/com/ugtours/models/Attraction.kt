package com.ugtours.models

data class Attraction(
    val id: Int,
    val name: String,
    val category: String,
    val location: String,
    val description: String,
    val uniqueFeatures: List<String>,
    val imageUrls: List<String>,
    val thumbnailUrl: String,
    val nearbyAccommodations: List<Accommodation>,
    var isFavorite: Boolean = false
)
