package com.b7b.sobriety

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.b7b.sobriety.data.AppDatabase
import com.b7b.sobriety.data.PreferencesManager
import com.b7b.sobriety.repository.SobrietyRepository
import com.b7b.sobriety.ui.components.BottomNavBar
import com.b7b.sobriety.ui.navigation.AppNavigation
import com.b7b.sobriety.ui.theme.SobrietyTheme
import com.b7b.sobriety.viewmodel.SobrietyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getInstance(applicationContext)
        val preferencesManager = PreferencesManager(applicationContext)
        val repository = SobrietyRepository(database.checkInDao(), preferencesManager)
        
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SobrietyViewModel(repository) as T
            }
        }

        setContent {
            val viewModel: SobrietyViewModel = viewModel(factory = viewModelFactory)
            val uiState by viewModel.uiState.collectAsState()
            val navController = rememberNavController()

            SobrietyTheme(darkTheme = uiState.preferences.isDarkTheme) {
                Scaffold(
                    bottomBar = {
                        if (uiState.preferences.quitDate != null) {
                            BottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(navController, viewModel, uiState)
                    }
                }
            }
        }
    }
}
