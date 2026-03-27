package com.theword.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.theword.app.TheWordApplication
import com.theword.app.ui.about.AboutScreen
import com.theword.app.ui.bible.BibleScreen
import com.theword.app.ui.bible.BibleViewModel
import com.theword.app.ui.bookmarks.BookmarksScreen
import com.theword.app.ui.bookmarks.BookmarksViewModel
import com.theword.app.ui.home.HomeScreen
import com.theword.app.ui.home.HomeViewModel
import com.theword.app.ui.prayer.PrayerScreen
import com.theword.app.ui.progress.ProgressScreen
import com.theword.app.ui.progress.ProgressViewModel
import com.theword.app.ui.quiz.QuizScreen
import com.theword.app.ui.quiz.QuizViewModel
import com.theword.app.ui.stories.StoriesScreen
import kotlinx.coroutines.launch

import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook

sealed class Screen(val route: String, val label: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Outlined.Home, Icons.Filled.Home)
    data object Bible : Screen("bible", "Bible", Icons.AutoMirrored.Outlined.MenuBook, Icons.AutoMirrored.Filled.MenuBook)
    data object Bookmarks : Screen("bookmarks", "Bookmarks", Icons.Outlined.Bookmark, Icons.Filled.Bookmark)
    data object Progress : Screen("progress", "Progress", Icons.Outlined.BarChart, Icons.Filled.BarChart)
    data object Quiz : Screen("quiz", "Quiz", Icons.Outlined.Quiz, Icons.Filled.Quiz)
    data object Stories : Screen("stories", "Stories", Icons.Outlined.AutoStories, Icons.Filled.AutoStories)
    data object Prayer : Screen("prayer", "Prayer", Icons.Outlined.SelfImprovement, Icons.Filled.SelfImprovement)
    data object About : Screen("about", "About", Icons.Outlined.Info, Icons.Filled.Info)
}

val bottomNavItems = listOf(Screen.Home, Screen.Bible, Screen.Bookmarks, Screen.Progress)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheWordApp() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val app = TheWordApplication.instance
    val isDarkMode by app.preferencesManager.darkMode.collectAsState(initial = false)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📖 The Word") },
                actions = {
                    // Dark mode toggle
                    IconButton(onClick = {
                        scope.launch { app.preferencesManager.setDarkMode(!isDarkMode) }
                    }) {
                        Icon(
                            if (isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Toggle theme"
                        )
                    }
                    // More menu
                    var showMenu by remember { mutableStateOf(false) }
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Quiz") },
                            onClick = { showMenu = false; navController.navigate(Screen.Quiz.route) },
                            leadingIcon = { Icon(Icons.Outlined.Quiz, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Stories") },
                            onClick = { showMenu = false; navController.navigate(Screen.Stories.route) },
                            leadingIcon = { Icon(Icons.Outlined.AutoStories, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Prayer") },
                            onClick = { showMenu = false; navController.navigate(Screen.Prayer.route) },
                            leadingIcon = { Icon(Icons.Outlined.SelfImprovement, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("About") },
                            onClick = { showMenu = false; navController.navigate(Screen.About.route) },
                            leadingIcon = { Icon(Icons.Outlined.Info, null) }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selected) screen.selectedIcon else screen.icon,
                                contentDescription = screen.label
                            )
                        },
                        label = { Text(screen.label) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val vm: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
                HomeScreen(vm, onNavigateToBible = { navController.navigate(Screen.Bible.route) })
            }
            composable(Screen.Bible.route) {
                val vm: BibleViewModel = viewModel(factory = BibleViewModel.Factory)
                BibleScreen(vm)
            }
            composable(Screen.Bookmarks.route) {
                val vm: BookmarksViewModel = viewModel(factory = BookmarksViewModel.Factory)
                BookmarksScreen(vm, onNavigateToBible = { navController.navigate(Screen.Bible.route) })
            }
            composable(Screen.Progress.route) {
                val vm: ProgressViewModel = viewModel(factory = ProgressViewModel.Factory)
                ProgressScreen(vm)
            }
            composable(Screen.Quiz.route) {
                val vm: QuizViewModel = viewModel(factory = QuizViewModel.Factory)
                QuizScreen(vm)
            }
            composable(Screen.Stories.route) {
                StoriesScreen()
            }
            composable(Screen.Prayer.route) {
                PrayerScreen()
            }
            composable(Screen.About.route) {
                AboutScreen()
            }
        }
    }
}
