package com.batyaboyo.bibleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.batyaboyo.bibleapp.ui.TheWordApp
import com.batyaboyo.bibleapp.ui.theme.TheWordTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheWordTheme {
                TheWordApp()
            }
        }
    }
}
