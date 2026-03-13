package com.b7b.sobriety.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel
import com.b7b.sobriety.ui.screens.OnboardingScreen
import com.b7b.sobriety.ui.screens.DashboardScreen
import com.b7b.sobriety.ui.screens.CalendarScreen
import com.b7b.sobriety.ui.screens.HealthTimelineScreen
import com.b7b.sobriety.ui.screens.CopingToolsScreen
import com.b7b.sobriety.ui.screens.MilestonesScreen
import com.b7b.sobriety.ui.screens.JournalScreen
import com.b7b.sobriety.ui.screens.ResourcesScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object Calendar : Screen("calendar")
    object Health : Screen("health")
    object Coping : Screen("coping")
    object Milestones : Screen("milestones")
    object Journal : Screen("journal")
    object Resources : Screen("resources")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: SobrietyViewModel,
    uiState: SobrietyUiState
) {
    val startDestination = if (uiState.preferences.quitDate == null) {
        Screen.Onboarding.route
    } else {
        Screen.Dashboard.route
    }

     NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(viewModel)
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(viewModel, uiState)
        }
        composable(Screen.Calendar.route) {
            CalendarScreen(viewModel, uiState)
        }
        composable(Screen.Health.route) {
            HealthTimelineScreen(uiState)
        }
        composable(Screen.Coping.route) {
            CopingToolsScreen(viewModel, uiState)
        }
        composable(Screen.Milestones.route) {
            MilestonesScreen(uiState)
        }
        composable(Screen.Journal.route) {
            JournalScreen(uiState)
        }
        composable(Screen.Resources.route) {
            ResourcesScreen(viewModel, uiState)
        }
    }
}
