package com.batyaboyo.bibleapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "About The Word",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            AboutSection(
                title = "Our Mission",
                content = "The Word is a modern, user-friendly Bible study application designed to make scripture accessible to everyone. Whether you're studying, researching, or seeking inspiration, our app provides a seamless reading experience across all devices."
            )
        }

        item {
            AboutSection(
                title = "Features",
                content = "• Complete Bible text with all 66 books\n" +
                        "• Multiple Bible translations\n" +
                        "• Classic commentaries\n" +
                        "• Powerful search functionality\n" +
                        "• Bookmark and save your favorite verses\n" +
                        "• Organize bookmarks into collections\n" +
                        "• Highlight verses and add study notes\n" +
                        "• Track your Bible reading progress\n" +
                        "• Fully responsive design\n" +
                        "• Dark and light mode"
            )
        }

        item {
            AboutSection(
                title = "Data Source",
                content = "All Bible text and commentaries are provided by the HelloAO Bible API, a free and open-source API serving hundreds of Bible translations in many languages. We are grateful for their service in making scripture freely accessible."
            )
        }

        item {
            AboutSection(
                title = "Privacy",
                content = "Your privacy is important to us. All bookmarks, notes, and preferences are stored locally on your device. We do not collect, store, or share any personal information."
            )
        }
    }
}

@Composable
private fun AboutSection(title: String, content: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
        Text(content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}
