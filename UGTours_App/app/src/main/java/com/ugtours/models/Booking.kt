package com.ugtours.models

/**
 * Domain model representing a booking.
 * Used in the UI layer, separate from the database entity.
 */
data class Booking(
    val id: Long = 0,
    val userId: Long,
    val attractionId: String,
    val attractionName: String,
    val accommodationName: String,
    val accommodationType: String,
    val checkInDate: String,
    val checkOutDate: String,
    val numberOfGuests: Int,
    val numberOfNights: Int,
    val pricePerNightUSD: Double,
    val totalPriceUSD: Double,
    val totalPriceUGX: Double,
    val status: BookingStatus,
    val contactEmail: String,
    val contactPhone: String,
    val specialRequests: String = "",
    val bookingDate: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Get formatted price range string
     */
    fun getFormattedPrice(): String {
        return "$${"%.2f".format(totalPriceUSD)} (UGX ${"%,.0f".format(totalPriceUGX)})"
    }
    
    /**
     * Get formatted date range
     */
    fun getDateRange(): String {
        return "$checkInDate - $checkOutDate"
    }
    
    /**
     * Get status display text
     */
    fun getStatusText(): String {
        return when (status) {
            BookingStatus.PENDING -> "Pending Confirmation"
            BookingStatus.CONFIRMED -> "Confirmed"
            BookingStatus.CANCELLED -> "Cancelled"
        }
    }
    
    /**
     * Check if booking can be cancelled
     */
    fun canBeCancelled(): Boolean {
        return status == BookingStatus.PENDING || status == BookingStatus.CONFIRMED
    }
    
    /**
     * Check if booking can be confirmed
     */
    fun canBeConfirmed(): Boolean {
        return status == BookingStatus.PENDING
    }
}

/**
 * Enum representing booking status
 */
enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED;
    
    companion object {
        fun fromString(value: String): BookingStatus {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                PENDING
            }
        }
    }
}
