package com.b7b.sobriety

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.b7b.sobriety.data.AppDatabase
import com.b7b.sobriety.data.PreferencesManager
import com.b7b.sobriety.repository.SobrietyRepository
import androidx.compose.ui.res.stringResource
import com.b7b.sobriety.ui.components.BottomNavBar
import com.b7b.sobriety.ui.navigation.AppNavigation
import com.b7b.sobriety.ui.navigation.Screen
import com.b7b.sobriety.R
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.SobrietyTheme
import com.b7b.sobriety.viewmodel.SobrietyViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferencesManager = PreferencesManager(applicationContext)
        val database = runCatching {
            AppDatabase.getInstance(applicationContext)
        }.getOrElse {
            // Recover once from an incompatible/corrupted on-device DB instead of crashing at launch.
            applicationContext.deleteDatabase("sobriety_db")
            AppDatabase.resetInstance()
            AppDatabase.getInstance(applicationContext)
        }
        val repository = SobrietyRepository(database.checkInDao(), preferencesManager)
        
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SobrietyViewModel(repository) as T
            }
        }

        setContent {
            val viewModel: SobrietyViewModel = viewModel(factory = viewModelFactory)
            val uiState by viewModel.uiState.collectAsState()
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showNavigation = currentRoute != Screen.Splash.route && currentRoute != Screen.Onboarding.route
            val screenTitle = when (currentRoute) {
                Screen.Dashboard.route -> stringResource(R.string.app_name)
                Screen.Calendar.route -> stringResource(R.string.calendar)
                Screen.Health.route -> stringResource(R.string.progress)
                Screen.Journal.route -> stringResource(R.string.journal)
                Screen.Settings.route -> stringResource(R.string.settings)
                else -> ""
            }

            SobrietyTheme(darkTheme = uiState.preferences.isDarkTheme) {
                Scaffold(
                    topBar = {
                        if (showNavigation) {
                            TopAppBar(
                                title = { 
                                    Text(
                                        screenTitle, 
                                        color = if (currentRoute == Screen.Dashboard.route) Primary else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    ) 
                                },
                                actions = {
                                    IconButton(onClick = { viewModel.toggleTheme() }) {
                                        Icon(
                                            if (uiState.preferences.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                            contentDescription = stringResource(R.string.toggle_theme)
                                        )
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (showNavigation) {
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
