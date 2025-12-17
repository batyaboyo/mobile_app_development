package com.ugtours.ui.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ugtours.data.repository.BookingsRepository
import com.ugtours.data.repository.UserPreferencesRepository
import com.ugtours.models.Booking
import com.ugtours.models.BookingStatus
import com.ugtours.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for managing bookings.
 * Handles booking operations and UI state.
 */
class BookingsViewModel(
    private val bookingsRepository: BookingsRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    private val _bookingsState = MutableStateFlow<UiState<List<Booking>>>(UiState.Loading)
    val bookingsState: StateFlow<UiState<List<Booking>>> = _bookingsState.asStateFlow()
    
    private val _createBookingState = MutableStateFlow<UiState<Long>>(UiState.Idle)
    val createBookingState: StateFlow<UiState<Long>> = _createBookingState.asStateFlow()
    
    private val _actionState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val actionState: StateFlow<UiState<String>> = _actionState.asStateFlow()
    
    private val _statsState = MutableStateFlow<BookingStats>(BookingStats())
    val statsState: StateFlow<BookingStats> = _statsState.asStateFlow()
    
    init {
        loadBookings()
        loadStats()
    }
    
    /**
     * Load all bookings for the user
     */
    fun loadBookings() {
        viewModelScope.launch {
            _bookingsState.value = UiState.Loading
            try {
                val userId = preferencesRepository.currentUserIdFlow.first() ?: -1L
                bookingsRepository.getUserBookings(userId).collect { bookings ->
                    _bookingsState.value = UiState.Success(bookings)
                }
            } catch (e: Exception) {
                _bookingsState.value = UiState.Error(e.message ?: "Failed to load bookings")
            }
        }
    }
    
    /**
     * Load bookings by status
     */
    fun loadBookingsByStatus(status: BookingStatus) {
        viewModelScope.launch {
            _bookingsState.value = UiState.Loading
            try {
                val userId = preferencesRepository.currentUserIdFlow.first() ?: -1L
                bookingsRepository.getUserBookingsByStatus(userId, status).collect { bookings ->
                    _bookingsState.value = UiState.Success(bookings)
                }
            } catch (e: Exception) {
                _bookingsState.value = UiState.Error(e.message ?: "Failed to load bookings")
            }
        }
    }
    
    /**
     * Load upcoming bookings
     */
    fun loadUpcomingBookings() {
        viewModelScope.launch {
            _bookingsState.value = UiState.Loading
            try {
                val userId = preferencesRepository.currentUserIdFlow.first() ?: -1L
                bookingsRepository.getUpcomingBookings(userId).collect { bookings ->
                    _bookingsState.value = UiState.Success(bookings)
                }
            } catch (e: Exception) {
                _bookingsState.value = UiState.Error(e.message ?: "Failed to load bookings")
            }
        }
    }
    
    /**
     * Create a new booking
     */
    fun createBooking(booking: Booking) {
        viewModelScope.launch {
            _createBookingState.value = UiState.Loading
            try {
                val bookingId = bookingsRepository.createBooking(booking)
                _createBookingState.value = UiState.Success(bookingId)
                loadBookings() // Reload bookings list
                loadStats() // Update statistics
            } catch (e: Exception) {
                _createBookingState.value = UiState.Error(e.message ?: "Failed to create booking")
            }
        }
    }
    
    /**
     * Confirm a booking
     */
    fun confirmBooking(bookingId: Long) {
        viewModelScope.launch {
            _actionState.value = UiState.Loading
            try {
                bookingsRepository.confirmBooking(bookingId)
                _actionState.value = UiState.Success("Booking confirmed successfully")
                loadBookings()
                loadStats()
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Failed to confirm booking")
            }
        }
    }
    
    /**
     * Cancel a booking
     */
    fun cancelBooking(bookingId: Long) {
        viewModelScope.launch {
            _actionState.value = UiState.Loading
            try {
                bookingsRepository.cancelBooking(bookingId)
                _actionState.value = UiState.Success("Booking cancelled successfully")
                loadBookings()
                loadStats()
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Failed to cancel booking")
            }
        }
    }
    
    /**
     * Delete a booking
     */
    fun deleteBooking(booking: Booking) {
        viewModelScope.launch {
            _actionState.value = UiState.Loading
            try {
                bookingsRepository.deleteBooking(booking)
                _actionState.value = UiState.Success("Booking deleted successfully")
                loadBookings()
                loadStats()
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Failed to delete booking")
            }
        }
    }
    
    /**
     * Load booking statistics
     */
    private fun loadStats() {
        viewModelScope.launch {
            try {
                val userId = preferencesRepository.currentUserIdFlow.first() ?: -1L
                val activeCount = bookingsRepository.getActiveBookingsCount(userId)
                val totalSpent = bookingsRepository.getTotalSpentUSD(userId)
                _statsState.value = BookingStats(
                    activeBookings = activeCount,
                    totalSpentUSD = totalSpent
                )
            } catch (e: Exception) {
                // Silently fail for stats
            }
        }
    }
    
    /**
     * Reset create booking state
     */
    fun resetCreateBookingState() {
        _createBookingState.value = UiState.Idle
    }
    
    /**
     * Reset action state
     */
    fun resetActionState() {
        _actionState.value = UiState.Idle
    }
}

/**
 * Data class for booking statistics
 */
data class BookingStats(
    val activeBookings: Int = 0,
    val totalSpentUSD: Double = 0.0
) {
    fun getFormattedTotalSpent(): String {
        return "$${"%.2f".format(totalSpentUSD)}"
    }
}
