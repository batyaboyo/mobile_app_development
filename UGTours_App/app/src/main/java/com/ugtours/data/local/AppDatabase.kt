package com.ugtours.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ugtours.data.local.dao.BookingsDao
import com.ugtours.data.local.dao.FavoritesDao
import com.ugtours.data.local.dao.RecentlyViewedDao
import com.ugtours.data.local.dao.UserDao
import com.ugtours.data.local.entities.BookingEntity
import com.ugtours.data.local.entities.FavoriteEntity
import com.ugtours.data.local.entities.RecentlyViewedEntity
import com.ugtours.data.local.entities.UserEntity

/**
 * Room database for the UGTours application.
 * Contains tables for users, favorites, recently viewed attractions, and bookings.
 */
@Database(
    entities = [
        UserEntity::class,
        FavoriteEntity::class,
        RecentlyViewedEntity::class,
        BookingEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun recentlyViewedDao(): RecentlyViewedDao
    abstract fun bookingsDao(): BookingsDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * Get the singleton instance of the database.
         * Creates the database if it doesn't exist.
         * @param context Application context
         * @return The database instance
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ugtours_database"
                )
                    .fallbackToDestructiveMigration() // For development - remove in production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
