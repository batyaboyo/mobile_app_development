package com.theword.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import java.util.Calendar

sealed class Screen(val route: String, val label: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Outlined.Home, Icons.Filled.Home)
    data object Bible : Screen("bible", "Bible", Icons.AutoMirrored.Outlined.MenuBook, Icons.AutoMirrored.Filled.MenuBook)
    data object Study : Screen("study", "Study", Icons.AutoMirrored.Outlined.LibraryBooks, Icons.AutoMirrored.Filled.LibraryBooks)
    data object Bookmarks : Screen("bookmarks", "Bookmarks", Icons.Outlined.Bookmark, Icons.Filled.Bookmark)
    data object Quiz : Screen("quiz", "Quiz", Icons.Outlined.Quiz, Icons.Filled.Quiz)
    data object Stories : Screen("stories", "Stories", Icons.Outlined.AutoStories, Icons.Filled.AutoStories)
    data object Prayer : Screen("prayer", "Prayer", Icons.Outlined.SelfImprovement, Icons.Filled.SelfImprovement)
    data object About : Screen("about", "About", Icons.Outlined.Info, Icons.Filled.Info)
    data object Progress : Screen("progress", "Progress", Icons.Outlined.BarChart, Icons.Filled.BarChart)
}

val bottomNavItems = listOf(Screen.Home, Screen.Bible, Screen.Study, Screen.About)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheWordApp() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val app = TheWordApplication.instance
    val isDarkMode by app.preferencesManager.darkMode.collectAsState(initial = false)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    // Check if we should show a back button
    val canPop = navController.previousBackStackEntry != null
    val isHome = currentRoute == Screen.Home.route
    val showBack = !isHome

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = {
                            if (canPop) {
                                navController.popBackStack()
                            } else {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                                }
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
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
                HomeScreen(
                    viewModel = vm,
                    onNavigateToBible = { bookId, chapter ->
                        val route = if (bookId != null && chapter != null) {
                            Screen.Bible.route + "?bookId=$bookId&chapter=$chapter"
                        } else {
                            Screen.Bible.route
                        }
                        navController.navigate(route)
                    },
                    onNavigateToStories = { storyId ->
                        val route = if (storyId != null) Screen.Stories.route + "?storyId=$storyId" else Screen.Stories.route
                        navController.navigate(route)
                    },
                    onNavigateToPrayer = { isEvening, index ->
                        val route = if (isEvening != null && index != null) {
                            Screen.Prayer.route + "?isEvening=$isEvening&index=$index"
                        } else {
                            Screen.Prayer.route
                        }
                        navController.navigate(route)
                    },
                    onNavigateToDevotion = { navController.navigate("devotion") },
                    onNavigateToProgress = { navController.navigate(Screen.Progress.route) }
                )
            }
            composable(Screen.Bible.route + "?bookId={bookId}&chapter={chapter}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId")
                val chapter = backStackEntry.arguments?.getString("chapter")?.toIntOrNull()
                
                val vm: BibleViewModel = viewModel(factory = BibleViewModel.Factory)
                
                // If deep linking, select the chapter
                LaunchedEffect(bookId, chapter) {
                    if (bookId != null && chapter != null) {
                        vm.selectChapterDeepLink(bookId, chapter)
                    }
                }
                
                BibleScreen(vm)
            }
            composable(Screen.Study.route) {
                com.theword.app.ui.study.StudyScreen(
                    onNavigateToQuiz = { navController.navigate(Screen.Quiz.route) },
                    onNavigateToBookmarks = { navController.navigate(Screen.Bookmarks.route) }
                )
            }
            composable(Screen.Bookmarks.route) {
                val vm: BookmarksViewModel = viewModel(factory = BookmarksViewModel.Factory)
                BookmarksScreen(vm, onNavigateToBible = { navController.navigate(Screen.Bible.route) })
            }
            composable(Screen.Quiz.route) {
                val vm: QuizViewModel = viewModel(factory = QuizViewModel.Factory)
                QuizScreen(vm)
            }
            composable("devotion") {
                // Share the HomeViewModel from the Home backstack entry
                val homeEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry(Screen.Home.route)
                }
                val vm: HomeViewModel = viewModel(viewModelStoreOwner = homeEntry, factory = HomeViewModel.Factory)
                com.theword.app.ui.home.DevotionScreen(viewModel = vm, onBack = { navController.popBackStack() })
            }
            composable(Screen.Stories.route + "?storyId={storyId}") { backStackEntry ->
                val storyId = backStackEntry.arguments?.getString("storyId")
                StoriesScreen(initialStoryId = storyId)
            }
            composable(Screen.Prayer.route + "?isEvening={isEvening}&index={index}") { backStackEntry ->
                val isEvening = backStackEntry.arguments?.getString("isEvening")?.toBoolean()
                val index = backStackEntry.arguments?.getString("index")?.toIntOrNull()
                PrayerScreen(initialIsEvening = isEvening, initialIndex = index)
            }
            composable(Screen.About.route) {
                AboutScreen()
            }
            composable(Screen.Progress.route) {
                val vm: ProgressViewModel = viewModel(factory = ProgressViewModel.Factory)
                ProgressScreen(vm)
            }
        }
    }
}
