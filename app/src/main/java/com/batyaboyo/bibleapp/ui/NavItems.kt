package com.batyaboyo.bibleapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class TabItem(val title: String) {
    Home("Home"),
    Bible("Bible"),
    Bookmarks("Bookmarks"),
    Progress("Progress"),
    Stories("Stories"),
    Prayer("Prayer"),
    Quiz("Quiz"),
    About("About")
}

fun tabIcon(tab: TabItem): ImageVector = when (tab) {
    TabItem.Home -> Icons.Outlined.Home
    TabItem.Bible -> Icons.AutoMirrored.Outlined.MenuBook
    TabItem.Bookmarks -> Icons.Outlined.BookmarkBorder
    TabItem.Progress -> Icons.Outlined.Timeline
    TabItem.Stories -> Icons.Outlined.AutoStories
    TabItem.Prayer -> Icons.Outlined.SelfImprovement
    TabItem.Quiz -> Icons.Outlined.Psychology
    TabItem.About -> Icons.Outlined.Info
}
