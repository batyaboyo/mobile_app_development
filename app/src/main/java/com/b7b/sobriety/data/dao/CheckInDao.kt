package com.b7b.sobriety.data.dao

import androidx.room.*
import com.b7b.sobriety.data.model.CheckIn
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(checkIn: CheckIn)

    @Query("SELECT * FROM check_ins ORDER BY date DESC")
    fun getAllFlow(): Flow<List<CheckIn>>

    @Query("SELECT * FROM check_ins ORDER BY date DESC")
    suspend fun getAll(): List<CheckIn>

    @Query("SELECT * FROM check_ins WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): CheckIn?

    @Query("SELECT * FROM check_ins WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getRange(startDate: String, endDate: String): List<CheckIn>

    @Query("DELETE FROM check_ins")
    suspend fun deleteAll()
}
