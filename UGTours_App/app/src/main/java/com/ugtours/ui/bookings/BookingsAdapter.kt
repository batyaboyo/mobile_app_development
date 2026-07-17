package com.ugtours.ui.bookings

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ugtours.R
import com.ugtours.databinding.ItemBookingBinding
import com.ugtours.models.Booking
import com.ugtours.models.BookingStatus

/**
 * Adapter for displaying bookings in a RecyclerView.
 */
class BookingsAdapter(
    private val onBookingClick: (Booking) -> Unit = {},
    private val onConfirmClick: (Booking) -> Unit = {},
    private val onCancelClick: (Booking) -> Unit = {}
) : ListAdapter<Booking, BookingsAdapter.BookingViewHolder>(BookingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookingViewHolder(
        private val binding: ItemBookingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(booking: Booking) {
            binding.apply {
                // Set attraction and accommodation names
                attractionName.text = booking.attractionName
                accommodationName.text = "${booking.accommodationName} (${booking.accommodationType})"

                // Set dates
                val datesText = "${booking.checkInDate} - ${booking.checkOutDate} (${booking.numberOfNights} ${if (booking.numberOfNights == 1) "night" else "nights"})"
                bookingDates.text = datesText

                // Set guests
                guestsCount.text = "${booking.numberOfGuests} ${if (booking.numberOfGuests == 1) "Guest" else "Guests"}"

                // Set price
                totalPrice.text = booking.getFormattedPrice()

                // Set contact
                contactInfo.text = booking.contactEmail

                // Set status chip
                statusChip.text = booking.getStatusText()
                when (booking.status) {
                    BookingStatus.CONFIRMED -> {
                        statusChip.setChipBackgroundColorResource(R.color.md_theme_light_primaryContainer)
                        statusChip.setTextColor(Color.parseColor("#1B5E20"))
                    }
                    BookingStatus.PENDING -> {
                        statusChip.setChipBackgroundColorResource(R.color.md_theme_light_secondaryContainer)
                        statusChip.setTextColor(Color.parseColor("#E65100"))
                    }
                    BookingStatus.CANCELLED -> {
                        statusChip.setChipBackgroundColorResource(R.color.md_theme_light_errorContainer)
                        statusChip.setTextColor(Color.parseColor("#B71C1C"))
                    }
                }

                // Show/hide action buttons based on status
                when {
                    booking.canBeConfirmed() -> {
                        actionButtons.visibility = View.VISIBLE
                        btnConfirm.visibility = View.VISIBLE
                        btnCancel.visibility = View.VISIBLE
                    }
                    booking.canBeCancelled() && booking.status == BookingStatus.CONFIRMED -> {
                        actionButtons.visibility = View.VISIBLE
                        btnConfirm.visibility = View.GONE
                        btnCancel.visibility = View.VISIBLE
                    }
                    else -> {
                        actionButtons.visibility = View.GONE
                    }
                }

                // Set click listeners
                bookingCard.setOnClickListener {
                    onBookingClick(booking)
                }

                btnConfirm.setOnClickListener {
                    onConfirmClick(booking)
                }

                btnCancel.setOnClickListener {
                    onCancelClick(booking)
                }
            }
        }
    }

    private class BookingDiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem == newItem
        }
    }
}
