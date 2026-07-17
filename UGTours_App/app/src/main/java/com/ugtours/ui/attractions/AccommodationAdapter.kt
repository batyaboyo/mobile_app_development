package com.ugtours.ui.attractions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ugtours.R
import com.ugtours.databinding.ItemAccommodationBinding
import com.ugtours.models.Accommodation

class AccommodationAdapter(
    private val accommodations: List<Accommodation>,
    private val onBookClick: (Accommodation) -> Unit = {}
) : RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccommodationViewHolder {
        val binding = ItemAccommodationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AccommodationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccommodationViewHolder, position: Int) {
        holder.bind(accommodations[position], onBookClick)
    }

    override fun getItemCount() = accommodations.size

    class AccommodationViewHolder(
        private val binding: ItemAccommodationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(accommodation: Accommodation, onBookClick: (Accommodation) -> Unit) {
            binding.accommodationName.text = accommodation.name
            binding.accommodationType.text = accommodation.type
            binding.accommodationPrice.text = formatPrice(accommodation.priceRange)
            binding.accommodationDistance.text = accommodation.distanceFromAttraction
            binding.accommodationRating.text = String.format("%.1f/5.0", accommodation.rating)
            binding.accommodationContact.text = accommodation.contact
            
            // Handle book button click
            binding.btnBookNow.setOnClickListener {
                onBookClick(accommodation)
            }
        }

        private fun formatPrice(priceRange: String): String {
            // Expected format: "$400-600/night" or "$150/night"
            val regex = Regex("\\$(\\d+)(?:-(\\d+))?")
            val match = regex.find(priceRange) ?: return priceRange

            val minUsd = match.groupValues[1].toIntOrNull() ?: return priceRange
            val maxUsd = match.groupValues.getOrNull(2)?.takeIf { it.isNotEmpty() }?.toIntOrNull()

            val rate = 3540
            val minUgx = minUsd * rate
            val maxUgx = if (maxUsd != null) maxUsd * rate else null

            val ugxString = if (maxUgx != null) {
                "UGX ${String.format("%,d", minUgx)} - ${String.format("%,d", maxUgx)}"
            } else {
                "UGX ${String.format("%,d", minUgx)}"
            }

            // Replace the "$X[-Y]" part with "$X[-Y] (UGX ...)"
            // We assume the original string contains the matched part.
            // Using logic to cleanly insert it.
            val originalUsdPart = match.value
            return priceRange.replaceFirst(originalUsdPart, "$originalUsdPart ($ugxString)")
        }
    }
}
