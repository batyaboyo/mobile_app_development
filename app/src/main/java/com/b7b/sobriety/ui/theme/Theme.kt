package com.b7b.sobriety.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = SurfaceDark,
    primaryContainer = PrimaryLightDark,
    onPrimaryContainer = TextMainDark,
    secondary = Success,
    onSecondary = SurfaceDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = TextMainDark,
    onSurface = TextMainDark,
    outline = BorderDark,
    error = Danger
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = SurfaceLight,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = TextMainLight,
    secondary = Success,
    onSecondary = SurfaceLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = TextMainLight,
    onSurface = TextMainLight,
    outline = BorderLight,
    error = Danger
)

@Composable
fun SobrietyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
