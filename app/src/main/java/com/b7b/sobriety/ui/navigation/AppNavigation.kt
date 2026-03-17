package com.b7b.sobriety.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel
import com.b7b.sobriety.ui.screens.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object Calendar : Screen("calendar")
    object Health : Screen("health") // Grouped under Progress in UI but route preserved for simplicity
    object Journal : Screen("journal")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: SobrietyViewModel,
    uiState: SobrietyUiState
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(Screen.Splash.route) {
            SplashScreen()
            LaunchedEffect(uiState.isLoading) {
                if (!uiState.isLoading) {
                    val destination = if (uiState.preferences.quitDate == null) {
                        Screen.Onboarding.route
                    } else {
                        Screen.Dashboard.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(viewModel) {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            }
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(viewModel, uiState)
        }
        composable(Screen.Calendar.route) {
            CalendarScreen(viewModel, uiState)
        }
        composable(Screen.Health.route) {
            ProgressScreen(uiState)
        }
        composable(Screen.Journal.route) {
            JournalScreen(viewModel, uiState)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(viewModel, uiState)
        }
    }
}
