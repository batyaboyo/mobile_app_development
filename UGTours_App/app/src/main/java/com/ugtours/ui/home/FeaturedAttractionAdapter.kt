package com.ugtours.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ugtours.R
import com.ugtours.databinding.ItemFeaturedAttractionBinding
import com.ugtours.models.Attraction

class FeaturedAttractionAdapter(
    private val onAttractionClick: (Attraction) -> Unit
) : ListAdapter<Attraction, FeaturedAttractionAdapter.FeaturedAttractionViewHolder>(AttractionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedAttractionViewHolder {
        val binding = ItemFeaturedAttractionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeaturedAttractionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeaturedAttractionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FeaturedAttractionViewHolder(
        private val binding: ItemFeaturedAttractionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(attraction: Attraction) {
            binding.attractionName.text = attraction.name
            binding.attractionLocation.text = attraction.location
            binding.attractionCategory.text = attraction.category

            // Load image with Glide
            val imageUrl = attraction.thumbnailUrl.ifEmpty { 
                attraction.imageUrls.firstOrNull() ?: "" 
            }
            
            if (imageUrl.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_attraction)
                    .error(R.drawable.placeholder_attraction)
                    .centerCrop()
                    .into(binding.attractionImage)
            } else {
                binding.attractionImage.setImageResource(R.drawable.placeholder_attraction)
            }

            binding.root.setOnClickListener {
                onAttractionClick(attraction)
            }
        }
    }

    private class AttractionDiffCallback : DiffUtil.ItemCallback<Attraction>() {
        override fun areItemsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem == newItem
        }
    }
}
