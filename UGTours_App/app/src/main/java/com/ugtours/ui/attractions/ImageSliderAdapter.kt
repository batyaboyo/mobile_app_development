package com.ugtours.ui.attractions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ugtours.R
import com.ugtours.databinding.ItemImageSliderBinding

class ImageSliderAdapter(
    private val imageUrls: List<String>
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageSliderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }

    override fun getItemCount() = imageUrls.size

    class ImageViewHolder(
        private val binding: ItemImageSliderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            val context = binding.root.context
            
            android.util.Log.d("ImageSliderAdapter", "Loading image: $imageUrl")
            
            // Check if it's a URL or a drawable resource name
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                // Load from URL
                Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_attraction)
                    .error(R.drawable.placeholder_attraction)
                    .into(binding.sliderImage)
            } else {
                // Try to load as drawable resource
                val imageResId = context.resources.getIdentifier(
                    imageUrl,
                    "drawable",
                    context.packageName
                )
                
                if (imageResId != 0) {
                    Glide.with(context)
                        .load(imageResId)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_attraction)
                        .error(R.drawable.placeholder_attraction)
                        .into(binding.sliderImage)
                } else {
                    android.util.Log.e("ImageSliderAdapter", "Failed to find image: $imageUrl")
                    binding.sliderImage.setImageResource(R.drawable.placeholder_attraction)
                }
            }
        }
    }
}
