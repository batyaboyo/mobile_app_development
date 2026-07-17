package com.uganda.learningapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uganda.learningapp.data.AppDatabase
import com.uganda.learningapp.ui.screens.MainScreen
import com.uganda.learningapp.ui.screens.ModuleDetailScreen
import com.uganda.learningapp.ui.screens.QuizScreen

@Composable
fun AppNavigation(
    database: AppDatabase,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            MainScreen(
                database = database,
                onModuleClick = { moduleId ->
                    navController.navigate("module/$moduleId")
                },
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange
            )
        }
        composable(
            "module/{moduleId}",
            arguments = listOf(navArgument("moduleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getInt("moduleId") ?: return@composable

            ModuleDetailScreen(
                database = database,
                moduleId = moduleId,
                onBack = { navController.popBackStack() },
                onTakeQuiz = { weekId ->
                    navController.navigate("quiz/$weekId")
                }
            )
        }
        composable(
            "quiz/{weekId}",
            arguments = listOf(navArgument("weekId") { type = NavType.IntType })
        ) { backStackEntry ->
            val weekId = backStackEntry.arguments?.getInt("weekId") ?: return@composable
            QuizScreen(
                database = database,
                weekId = weekId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
