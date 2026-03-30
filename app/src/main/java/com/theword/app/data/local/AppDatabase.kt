package com.theword.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        BookmarkEntity::class,
        HighlightEntity::class,
        ReadingProgressEntity::class,
        QuizResultEntity::class,
        TranslationCacheEntity::class,
        BookCacheEntity::class,
        ChapterCacheEntity::class,
        JournalEntryEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao
    abstract fun highlightDao(): HighlightDao
    abstract fun readingProgressDao(): ReadingProgressDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun bibleCacheDao(): BibleCacheDao
    abstract fun journalDao(): JournalDao

    companion object {
        val MIGRATION_4_5 = object : androidx.room.migration.Migration(4, 5) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Empty migration since no schema changes occurred between v4 and v5
                // This establishes the pattern for future updates
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "theword_database"
                )
                    .addMigrations(MIGRATION_4_5)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
