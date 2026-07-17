package com.ugtours.data.repository

import com.ugtours.data.local.dao.BookingsDao
import com.ugtours.data.local.entities.BookingEntity
import com.ugtours.models.Booking
import com.ugtours.models.BookingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for managing booking data.
 * Handles data operations and conversion between entity and domain models.
 */
class BookingsRepository(private val bookingsDao: BookingsDao) {
    
    /**
     * Get all bookings for a user
     */
    fun getUserBookings(userId: Long): Flow<List<Booking>> {
        return bookingsDao.getUserBookings(userId).map { entities ->
            entities.map { it.toBooking() }
        }
    }
    
    /**
     * Get bookings by status
     */
    fun getUserBookingsByStatus(userId: Long, status: BookingStatus): Flow<List<Booking>> {
        return bookingsDao.getUserBookingsByStatus(userId, status.name).map { entities ->
            entities.map { it.toBooking() }
        }
    }
    
    /**
     * Get upcoming confirmed bookings
     */
    fun getUpcomingBookings(userId: Long): Flow<List<Booking>> {
        return bookingsDao.getUpcomingBookings(userId).map { entities ->
            entities.map { it.toBooking() }
        }
    }
    
    /**
     * Get a specific booking by ID
     */
    suspend fun getBookingById(bookingId: Long): Booking? {
        return bookingsDao.getBookingById(bookingId)?.toBooking()
    }
    
    /**
     * Create a new booking
     */
    suspend fun createBooking(booking: Booking): Long {
        return bookingsDao.insertBooking(booking.toEntity())
    }
    
    /**
     * Update an existing booking
     */
    suspend fun updateBooking(booking: Booking) {
        bookingsDao.updateBooking(booking.toEntity())
    }
    
    /**
     * Delete a booking
     */
    suspend fun deleteBooking(booking: Booking) {
        bookingsDao.deleteBooking(booking.toEntity())
    }
    
    /**
     * Update booking status
     */
    suspend fun updateBookingStatus(bookingId: Long, status: BookingStatus) {
        bookingsDao.updateBookingStatus(bookingId, status.name, System.currentTimeMillis())
    }
    
    /**
     * Confirm a booking
     */
    suspend fun confirmBooking(bookingId: Long) {
        updateBookingStatus(bookingId, BookingStatus.CONFIRMED)
    }
    
    /**
     * Cancel a booking
     */
    suspend fun cancelBooking(bookingId: Long) {
        updateBookingStatus(bookingId, BookingStatus.CANCELLED)
    }
    
    /**
     * Get count of active bookings
     */
    suspend fun getActiveBookingsCount(userId: Long): Int {
        return bookingsDao.getActiveBookingsCount(userId)
    }
    
    /**
     * Get total amount spent by user
     */
    suspend fun getTotalSpentUSD(userId: Long): Double {
        return bookingsDao.getTotalSpentUSD(userId) ?: 0.0
    }
    
    /**
     * Get bookings for a specific attraction
     */
    fun getAttractionBookings(userId: Long, attractionId: String): Flow<List<Booking>> {
        return bookingsDao.getAttractionBookings(userId, attractionId).map { entities ->
            entities.map { it.toBooking() }
        }
    }
    
    /**
     * Delete all bookings for a user (for account deletion)
     */
    suspend fun deleteAllUserBookings(userId: Long) {
        bookingsDao.deleteAllUserBookings(userId)
    }
    
    // Extension functions for conversion between entity and domain model
    
    private fun BookingEntity.toBooking(): Booking {
        return Booking(
            id = id,
            userId = userId,
            attractionId = attractionId,
            attractionName = attractionName,
            accommodationName = accommodationName,
            accommodationType = accommodationType,
            checkInDate = checkInDate,
            checkOutDate = checkOutDate,
            numberOfGuests = numberOfGuests,
            numberOfNights = numberOfNights,
            pricePerNightUSD = pricePerNightUSD,
            totalPriceUSD = totalPriceUSD,
            totalPriceUGX = totalPriceUGX,
            status = BookingStatus.fromString(status),
            contactEmail = contactEmail,
            contactPhone = contactPhone,
            specialRequests = specialRequests,
            bookingDate = bookingDate,
            updatedAt = updatedAt
        )
    }
    
    private fun Booking.toEntity(): BookingEntity {
        return BookingEntity(
            id = id,
            userId = userId,
            attractionId = attractionId,
            attractionName = attractionName,
            accommodationName = accommodationName,
            accommodationType = accommodationType,
            checkInDate = checkInDate,
            checkOutDate = checkOutDate,
            numberOfGuests = numberOfGuests,
            numberOfNights = numberOfNights,
            pricePerNightUSD = pricePerNightUSD,
            totalPriceUSD = totalPriceUSD,
            totalPriceUGX = totalPriceUGX,
            status = status.name,
            contactEmail = contactEmail,
            contactPhone = contactPhone,
            specialRequests = specialRequests,
            bookingDate = bookingDate,
            updatedAt = updatedAt
        )
    }
}
