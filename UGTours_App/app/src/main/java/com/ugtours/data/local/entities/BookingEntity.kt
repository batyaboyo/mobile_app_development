package com.ugtours.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a booking in the database.
 * Stores accommodation booking details with pricing in both USD and UGX.
 */
@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val userId: Long,
    
    val attractionId: String,
    
    val attractionName: String,
    
    val accommodationName: String,
    
    val accommodationType: String, // Luxury, Mid-range, Budget
    
    val checkInDate: String,
    
    val checkOutDate: String,
    
    val numberOfGuests: Int,
    
    val numberOfNights: Int,
    
    val pricePerNightUSD: Double,
    
    val totalPriceUSD: Double,
    
    val totalPriceUGX: Double,
    
    val status: String, // PENDING, CONFIRMED, CANCELLED
    
    val contactEmail: String,
    
    val contactPhone: String,
    
    val specialRequests: String = "",
    
    val bookingDate: Long = System.currentTimeMillis(),
    
    val updatedAt: Long = System.currentTimeMillis()
)
