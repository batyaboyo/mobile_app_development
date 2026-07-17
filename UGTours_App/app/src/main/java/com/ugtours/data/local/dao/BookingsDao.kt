package com.ugtours.data.local.dao

import androidx.room.*
import com.ugtours.data.local.entities.BookingEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for bookings table.
 * Provides CRUD operations for booking management.
 */
@Dao
interface BookingsDao {
    
    /**
     * Get all bookings for a specific user, ordered by booking date (newest first)
     */
    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY bookingDate DESC")
    fun getUserBookings(userId: Long): Flow<List<BookingEntity>>
    
    /**
     * Get a specific booking by ID
     */
    @Query("SELECT * FROM bookings WHERE id = :bookingId")
    suspend fun getBookingById(bookingId: Long): BookingEntity?
    
    /**
     * Get bookings by status for a user
     */
    @Query("SELECT * FROM bookings WHERE userId = :userId AND status = :status ORDER BY bookingDate DESC")
    fun getUserBookingsByStatus(userId: Long, status: String): Flow<List<BookingEntity>>
    
    /**
     * Get upcoming bookings (confirmed status)
     */
    @Query("SELECT * FROM bookings WHERE userId = :userId AND status = 'CONFIRMED' ORDER BY checkInDate ASC")
    fun getUpcomingBookings(userId: Long): Flow<List<BookingEntity>>
    
    /**
     * Insert a new booking
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long
    
    /**
     * Update an existing booking
     */
    @Update
    suspend fun updateBooking(booking: BookingEntity)
    
    /**
     * Delete a booking
     */
    @Delete
    suspend fun deleteBooking(booking: BookingEntity)
    
    /**
     * Update booking status
     */
    @Query("UPDATE bookings SET status = :status, updatedAt = :updatedAt WHERE id = :bookingId")
    suspend fun updateBookingStatus(bookingId: Long, status: String, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * Get count of active bookings (confirmed) for a user
     */
    @Query("SELECT COUNT(*) FROM bookings WHERE userId = :userId AND status = 'CONFIRMED'")
    suspend fun getActiveBookingsCount(userId: Long): Int
    
    /**
     * Get total amount spent by user (confirmed bookings only)
     */
    @Query("SELECT SUM(totalPriceUSD) FROM bookings WHERE userId = :userId AND status = 'CONFIRMED'")
    suspend fun getTotalSpentUSD(userId: Long): Double?
    
    /**
     * Delete all bookings for a user (for account deletion)
     */
    @Query("DELETE FROM bookings WHERE userId = :userId")
    suspend fun deleteAllUserBookings(userId: Long)
    
    /**
     * Get bookings for a specific attraction
     */
    @Query("SELECT * FROM bookings WHERE userId = :userId AND attractionId = :attractionId ORDER BY bookingDate DESC")
    fun getAttractionBookings(userId: Long, attractionId: String): Flow<List<BookingEntity>>
}
