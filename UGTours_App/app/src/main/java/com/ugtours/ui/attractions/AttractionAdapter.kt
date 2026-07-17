package com.ugtours.ui.attractions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ugtours.R
import com.ugtours.databinding.ItemAttractionBinding
import com.ugtours.models.Attraction

class AttractionAdapter(
    private val onAttractionClick: (Attraction) -> Unit
) : ListAdapter<Attraction, AttractionAdapter.AttractionViewHolder>(AttractionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        val binding = ItemAttractionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AttractionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AttractionViewHolder(
        private val binding: ItemAttractionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.attractionCard.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAttractionClick(getItem(position))
                }
            }
        }

        fun bind(attraction: Attraction) {
            binding.attractionName.text = attraction.name
            binding.categoryChip.text = attraction.category
            
            // Load image using Glide - support both URLs and drawable resources
            val context = binding.root.context
            
            android.util.Log.d("AttractionAdapter", "Loading thumbnail for ${attraction.name}: ${attraction.thumbnailUrl}")
            
            // Check if it's a URL or a drawable resource name
            if (attraction.thumbnailUrl.startsWith("http://") || attraction.thumbnailUrl.startsWith("https://")) {
                // Load from URL
                Glide.with(context)
                    .load(attraction.thumbnailUrl)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_attraction)
                    .error(R.drawable.placeholder_attraction)
                    .into(binding.attractionImage)
            } else {
                // Try to load as drawable resource
                val imageResId = context.resources.getIdentifier(
                    attraction.thumbnailUrl,
                    "drawable",
                    context.packageName
                )
                
                if (imageResId != 0) {
                    Glide.with(context)
                        .load(imageResId)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_attraction)
                        .error(R.drawable.placeholder_attraction)
                        .into(binding.attractionImage)
                } else {
                    android.util.Log.e("AttractionAdapter", "Failed to find thumbnail: ${attraction.thumbnailUrl}")
                    binding.attractionImage.setImageResource(R.drawable.placeholder_attraction)
                }
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
