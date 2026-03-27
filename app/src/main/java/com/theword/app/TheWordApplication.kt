package com.theword.app

import android.app.Application
import com.theword.app.data.local.AppDatabase
import com.theword.app.data.local.PreferencesManager
import com.theword.app.data.api.RetrofitClient
import com.theword.app.data.repository.BibleRepository

class TheWordApplication : Application() {

    lateinit var database: AppDatabase
        private set
    lateinit var preferencesManager: PreferencesManager
        private set
    lateinit var repository: BibleRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = AppDatabase.getInstance(this)
        preferencesManager = PreferencesManager(this)
        repository = BibleRepository(
            api = RetrofitClient.bibleApi,
            db = database,
            prefs = preferencesManager
        )
    }

    companion object {
        lateinit var instance: TheWordApplication
            private set
    }
}
