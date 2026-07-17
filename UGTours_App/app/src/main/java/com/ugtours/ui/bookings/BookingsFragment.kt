package com.ugtours.ui.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ugtours.R
import com.ugtours.databinding.FragmentBookingsBinding
import com.ugtours.models.Booking
import com.ugtours.models.BookingStatus
import com.ugtours.ui.ViewModelFactory
import com.ugtours.ui.common.UiState
import kotlinx.coroutines.launch

/**
 * Bookings Fragment with MVVM architecture.
 * Displays user's accommodation bookings with filtering and management capabilities.
 */
class BookingsFragment : Fragment() {

    private var _binding: FragmentBookingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookingsViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    private lateinit var adapter: BookingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        adapter = BookingsAdapter(
            onBookingClick = { booking ->
                showBookingDetails(booking)
            },
            onConfirmClick = { booking ->
                confirmBooking(booking)
            },
            onCancelClick = { booking ->
                cancelBooking(booking)
            }
        )

        binding.bookingsRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        // Observe bookings list
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookingsState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.bookingsRecyclerView.visibility = View.GONE
                        binding.emptyView.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        if (state.data.isEmpty()) {
                            binding.emptyView.visibility = View.VISIBLE
                            binding.bookingsRecyclerView.visibility = View.GONE
                        } else {
                            binding.emptyView.visibility = View.GONE
                            binding.bookingsRecyclerView.visibility = View.VISIBLE
                            adapter.submitList(state.data)
                        }
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.emptyView.visibility = View.VISIBLE
                        binding.emptyMessage.text = state.message
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        // Observe booking statistics
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.statsState.collect { stats ->
                binding.activeBookingsCount.text = stats.activeBookings.toString()
                binding.totalSpent.text = stats.getFormattedTotalSpent()
            }
        }

        // Observe action results
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actionState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        Toast.makeText(requireContext(), state.data, Toast.LENGTH_SHORT).show()
                        viewModel.resetActionState()
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        viewModel.resetActionState()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupClickListeners() {
        // Filter chips
        binding.filterChips.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.chip_all -> viewModel.loadBookings()
                R.id.chip_upcoming -> viewModel.loadUpcomingBookings()
                R.id.chip_confirmed -> viewModel.loadBookingsByStatus(BookingStatus.CONFIRMED)
                R.id.chip_pending -> viewModel.loadBookingsByStatus(BookingStatus.PENDING)
                R.id.chip_cancelled -> viewModel.loadBookingsByStatus(BookingStatus.CANCELLED)
            }
        }

        // Explore button in empty state
        binding.exploreButton.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottom_navigation
            )?.selectedItemId = R.id.navigation_attractions
        }
    }

    private fun showBookingDetails(booking: Booking) {
        val message = buildString {
            append("Attraction: ${booking.attractionName}\n\n")
            append("Accommodation: ${booking.accommodationName}\n")
            append("Type: ${booking.accommodationType}\n\n")
            append("Check-in: ${booking.checkInDate}\n")
            append("Check-out: ${booking.checkOutDate}\n")
            append("Nights: ${booking.numberOfNights}\n")
            append("Guests: ${booking.numberOfGuests}\n\n")
            append("Price per night: $${booking.pricePerNightUSD}\n")
            append("Total: ${booking.getFormattedPrice()}\n\n")
            append("Contact: ${booking.contactEmail}\n")
            if (booking.contactPhone.isNotEmpty()) {
                append("Phone: ${booking.contactPhone}\n")
            }
            if (booking.specialRequests.isNotEmpty()) {
                append("\nSpecial Requests:\n${booking.specialRequests}")
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Booking Details")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun confirmBooking(booking: Booking) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm Booking")
            .setMessage("Are you sure you want to confirm this booking for ${booking.accommodationName}?")
            .setPositiveButton("Confirm") { _, _ ->
                viewModel.confirmBooking(booking.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun cancelBooking(booking: Booking) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel Booking")
            .setMessage("Are you sure you want to cancel this booking? This action cannot be undone.")
            .setPositiveButton("Yes, Cancel") { _, _ ->
                viewModel.cancelBooking(booking.id)
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
