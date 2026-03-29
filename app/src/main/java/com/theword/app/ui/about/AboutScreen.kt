package com.theword.app.ui.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    SelectionContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("About The Word", style = MaterialTheme.typography.headlineSmall)

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Our Mission", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "The Word is a free Bible study app designed to make Scripture accessible, engaging, and meaningful for everyone. Whether you're a seasoned theologian or just beginning your spiritual journey, our tools are designed to help you grow in faith.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Features", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val features = listOf(
                        "📖 Complete Bible Reading" to "Access all 66 books with multiple translations",
                        "🔖 Smart Bookmarks" to "Save, organize, and revisit favorite verses",
                        "🎨 Highlights & Notes" to "Annotate verses with colors and personal notes",
                        "📝 Commentaries" to "Study with classic and modern commentaries",
                        "❓ Daily Quiz" to "Test your Bible knowledge with new questions daily",
                        "🌈 Bible Stories" to "Kid-friendly summaries of key Bible stories",
                        "🙏 Prayer Timer" to "Morning and evening prayers with guided timer",
                        "📊 Reading Progress" to "Track your journey through the Bible",
                        "⚖️ Translation Comparison" to "Compare verses across multiple translations",
                        "🌓 Dark Mode" to "Comfortable reading in any lighting"
                    )
                    features.forEach { (title, desc) ->
                        Text(title, style = MaterialTheme.typography.labelLarge)
                        Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Data Source", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Bible text and translations are provided by the HelloAO Bible API (bible.helloao.org), a free and open Bible data source.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Privacy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your data stays on your device. We do not collect, store, or share any personal information. Your bookmarks, highlights, notes, and reading progress are stored locally and never leave your device.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Text(
                "Version 1.0 • Made with ❤️ and faith",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
