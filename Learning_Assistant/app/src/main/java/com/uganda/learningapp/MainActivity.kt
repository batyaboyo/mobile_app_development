package com.uganda.learningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.uganda.learningapp.data.AppDatabase
import com.uganda.learningapp.data.DataPopulator
import com.uganda.learningapp.ui.AppNavigation
import com.uganda.learningapp.ui.theme.LearningAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(this)

        // Populate database on first launch
        CoroutineScope(Dispatchers.IO).launch {
            val modules = database.roadmapDao().getAllModules().first()
            if (modules.isEmpty()) {
                DataPopulator.populate(database.roadmapDao())
            }
        }

        setContent {
            // State for dark mode - default to true (dark mode)
            var isDarkMode by remember { mutableStateOf(true) }

            // Load settings from database
            LaunchedEffect(Unit) {
                database.roadmapDao().getUserSettings().collect { settings ->
                    if (settings != null) {
                        isDarkMode = settings.darkModeEnabled
                    }
                }
            }

            LearningAppTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        database = database,
                        isDarkMode = isDarkMode,
                        onThemeChange = { enabled ->
                            isDarkMode = enabled
                        }
                    )
                }
            }
        }
    }
}
