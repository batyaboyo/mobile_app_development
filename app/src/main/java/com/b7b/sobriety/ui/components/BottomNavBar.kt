package com.b7b.sobriety.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.b7b.sobriety.ui.navigation.Screen

data class NavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

val navItems = listOf(
    NavItem("Dashboard", Screen.Dashboard.route, Icons.Default.Dashboard),
    NavItem("Calendar", Screen.Calendar.route, Icons.Default.CalendarMonth),
    NavItem("Health", Screen.Health.route, Icons.Default.Timeline),
    NavItem("Coping", Screen.Coping.route, Icons.Default.CheckCircle),
    NavItem("Milestones", Screen.Milestones.route, Icons.Default.EmojiEvents),
    NavItem("Journal", Screen.Journal.route, Icons.Default.Edit),
    NavItem("Resources", Screen.Resources.route, Icons.Default.Help)
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                label = { Text(item.name, softWrap = false) },
                icon = { Icon(item.icon, contentDescription = item.name) }
            )
        }
    }
}
