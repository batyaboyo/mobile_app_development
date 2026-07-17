package com.ugtours.ui.bookings

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ugtours.databinding.DialogCreateBookingBinding
import com.ugtours.models.Accommodation
import com.ugtours.models.Attraction
import com.ugtours.models.Booking
import com.ugtours.models.BookingStatus
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper class for creating booking dialogs
 */
class BookingDialogHelper(
    private val context: Context,
    private val userEmail: String,
    private val userPhone: String,
    private val userId: Long
) {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    
    fun showBookingDialog(
        attraction: Attraction,
        accommodation: Accommodation,
        onBookingCreated: (Booking) -> Unit
    ) {
        val binding = DialogCreateBookingBinding.inflate(LayoutInflater.from(context))
        
        // Set attraction and accommodation info
        binding.attractionName.text = attraction.name
        binding.accommodationName.text = accommodation.name
        
        // Pre-fill user contact info
        binding.contactEmail.setText(userEmail)
        binding.contactPhone.setText(userPhone)
        
        // Set default dates (today + 7 days for check-in, +9 days for check-out)
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        val checkInDate = dateFormat.format(calendar.time)
        binding.checkinDate.setText(checkInDate)
        
        calendar.add(Calendar.DAY_OF_MONTH, 2)
        val checkOutDate = dateFormat.format(calendar.time)
        binding.checkoutDate.setText(checkOutDate)
        calendar.add(Calendar.DAY_OF_MONTH, -9) // Reset calendar
        
        // Setup date pickers
        binding.checkinDateLayout.setEndIconOnClickListener {
            showDatePicker(binding.checkinDate.text.toString()) { date ->
                binding.checkinDate.setText(date)
                updatePriceSummary(binding, accommodation, date, binding.checkoutDate.text.toString())
            }
        }
        
        binding.checkinDate.setOnClickListener {
            showDatePicker(binding.checkinDate.text.toString()) { date ->
                binding.checkinDate.setText(date)
                updatePriceSummary(binding, accommodation, date, binding.checkoutDate.text.toString())
            }
        }
        
        binding.checkoutDateLayout.setEndIconOnClickListener {
            showDatePicker(binding.checkoutDate.text.toString()) { date ->
                binding.checkoutDate.setText(date)
                updatePriceSummary(binding, accommodation, binding.checkinDate.text.toString(), date)
            }
        }
        
        binding.checkoutDate.setOnClickListener {
            showDatePicker(binding.checkoutDate.text.toString()) { date ->
                binding.checkoutDate.setText(date)
                updatePriceSummary(binding, accommodation, binding.checkinDate.text.toString(), date)
            }
        }
        
        // Initial price calculation
        updatePriceSummary(binding, accommodation, checkInDate, checkOutDate)
        
        // Create dialog
        MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setPositiveButton("Confirm Booking") { _, _ ->
                createBooking(binding, attraction, accommodation, onBookingCreated)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showDatePicker(currentDate: String, onDateSelected: (String) -> Unit) {
        val date = try {
            dateFormat.parse(currentDate) ?: Date()
        } catch (e: Exception) {
            Date()
        }
        
        calendar.time = date
        
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    
    private fun updatePriceSummary(
        binding: DialogCreateBookingBinding,
        accommodation: Accommodation,
        checkInDate: String,
        checkOutDate: String
    ) {
        try {
            val checkIn = dateFormat.parse(checkInDate) ?: return
            val checkOut = dateFormat.parse(checkOutDate) ?: return
            
            val nights = ((checkOut.time - checkIn.time) / (1000 * 60 * 60 * 24)).toInt()
            
            if (nights <= 0) {
                Toast.makeText(context, "Check-out must be after check-in", Toast.LENGTH_SHORT).show()
                return
            }
            
            // Extract price from accommodation price range
            val pricePerNight = extractPriceFromRange(accommodation.priceRange)
            val totalUSD = pricePerNight * nights
            val totalUGX = totalUSD * 3540
            
            binding.nightsCount.text = nights.toString()
            binding.pricePerNight.text = "$${"%.2f".format(pricePerNight)}"
            binding.totalPrice.text = "$${"%.2f".format(totalUSD)} (UGX ${"%,.0f".format(totalUGX)})"
            
        } catch (e: Exception) {
            Toast.makeText(context, "Invalid dates", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun extractPriceFromRange(priceRange: String): Double {
        // Expected format: "$400-600/night" or "$150/night"
        val regex = Regex("\\$(\\d+)")
        val match = regex.find(priceRange)
        return match?.groupValues?.get(1)?.toDoubleOrNull() ?: 250.0
    }
    
    private fun createBooking(
        binding: DialogCreateBookingBinding,
        attraction: Attraction,
        accommodation: Accommodation,
        onBookingCreated: (Booking) -> Unit
    ) {
        try {
            val checkInDate = binding.checkinDate.text.toString()
            val checkOutDate = binding.checkoutDate.text.toString()
            val numberOfGuests = binding.numberOfGuests.text.toString().toIntOrNull() ?: 2
            val contactEmail = binding.contactEmail.text.toString()
            val contactPhone = binding.contactPhone.text.toString()
            val specialRequests = binding.specialRequests.text.toString()
            
            // Validate
            if (contactEmail.isBlank()) {
                Toast.makeText(context, "Please enter contact email", Toast.LENGTH_SHORT).show()
                return
            }
            
            // Calculate nights and price
            val checkIn = dateFormat.parse(checkInDate) ?: return
            val checkOut = dateFormat.parse(checkOutDate) ?: return
            val nights = ((checkOut.time - checkIn.time) / (1000 * 60 * 60 * 24)).toInt()
            
            if (nights <= 0) {
                Toast.makeText(context, "Invalid dates", Toast.LENGTH_SHORT).show()
                return
            }
            
            val pricePerNight = extractPriceFromRange(accommodation.priceRange)
            val totalUSD = pricePerNight * nights
            val totalUGX = totalUSD * 3540
            
            // Create booking
            val booking = Booking(
                userId = userId,
                attractionId = attraction.id.toString(),
                attractionName = attraction.name,
                accommodationName = accommodation.name,
                accommodationType = accommodation.type,
                checkInDate = checkInDate,
                checkOutDate = checkOutDate,
                numberOfGuests = numberOfGuests,
                numberOfNights = nights,
                pricePerNightUSD = pricePerNight,
                totalPriceUSD = totalUSD,
                totalPriceUGX = totalUGX,
                status = BookingStatus.PENDING,
                contactEmail = contactEmail,
                contactPhone = contactPhone,
                specialRequests = specialRequests
            )
            
            onBookingCreated(booking)
            
        } catch (e: Exception) {
            Toast.makeText(context, "Error creating booking: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
