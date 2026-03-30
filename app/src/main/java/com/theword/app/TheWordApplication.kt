package com.theword.app

import android.app.Application
import com.theword.app.data.local.AppDatabase
import com.theword.app.data.local.PreferencesManager
import com.theword.app.data.api.RetrofitClient
import com.theword.app.data.embedded.BundledBibleProvider
import com.theword.app.data.repository.BibleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TheWordApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var database: AppDatabase
        private set
    lateinit var preferencesManager: PreferencesManager
        private set
    lateinit var repository: BibleRepository
        private set
    lateinit var bundledBibleProvider: BundledBibleProvider
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = AppDatabase.getInstance(this)
        preferencesManager = PreferencesManager(this)
        bundledBibleProvider = BundledBibleProvider(this)
        repository = BibleRepository(
            api = RetrofitClient.bibleApi,
            db = database,
            prefs = preferencesManager,
            bundledProvider = bundledBibleProvider
        )

        // Pre-populate Room cache in background (optimization, not required)
        applicationScope.launch {
            repository.initializeBundledBibles(this@TheWordApplication)
        }
    }

    companion object {
        lateinit var instance: TheWordApplication
            private set
    }
}
