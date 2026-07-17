package com.ugtours.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ugtours.R

/**
 * Utility object for loading images with Glide with consistent configuration
 */
object ImageLoader {
    
    /**
     * Load image with standard configuration including transitions and caching
     */
    fun loadImage(
        context: Context,
        imageUrl: String,
        imageView: ImageView,
        placeholder: Int = R.drawable.placeholder_attraction,
        error: Int = R.drawable.placeholder_attraction
    ) {
        Glide.with(context)
            .load(imageUrl)
            .centerCrop()
            .placeholder(placeholder)
            .error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache both original & resized
            .transition(DrawableTransitionOptions.withCrossFade(300)) // Smooth fade-in
            .into(imageView)
    }
    
    /**
     * Load image without crop (for detail views)
     */
    fun loadImageFitCenter(
        context: Context,
        imageUrl: String,
        imageView: ImageView,
        placeholder: Int = R.drawable.placeholder_attraction,
        error: Int = R.drawable.placeholder_attraction
    ) {
        Glide.with(context)
            .load(imageUrl)
            .fitCenter()
            .placeholder(placeholder)
            .error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(imageView)
    }
    
    /**
     * Load circular image (for profile pictures, etc.)
     */
    fun loadCircularImage(
        context: Context,
        imageUrl: String,
        imageView: ImageView,
        placeholder: Int = R.drawable.ic_profile
    ) {
        Glide.with(context)
            .load(imageUrl)
            .circleCrop()
            .placeholder(placeholder)
            .error(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(imageView)
    }
    
    /**
     * Clear memory cache (useful for logout or settings)
     */
    fun clearMemoryCache(context: Context) {
        Glide.get(context).clearMemory()
    }
    
    /**
     * Clear disk cache (must be called on background thread)
     */
    fun clearDiskCache(context: Context) {
        Thread {
            Glide.get(context).clearDiskCache()
        }.start()
    }
}
