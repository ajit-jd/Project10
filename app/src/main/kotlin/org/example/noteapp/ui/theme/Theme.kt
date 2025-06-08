package org.example.noteapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BrightBlueButton,       // For "New Note" FAB, AppBar
    secondary = DarkerBlueButton,     // For "Clip Webpage" FAB
    tertiary = BrightBlueButton,      // Can be same as primary or another accent
    background = NavyBlue,
    surface = NavyBlue,
    onPrimary = LightColorText,
    onSecondary = LightColorText,
    onTertiary = LightColorText,
    onBackground = LightColorText,
    onSurface = LightColorText
)

// LightColorScheme remains as is for now, but won't be used by default.
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun NoteAppTheme(
    // darkTheme: Boolean = isSystemInDarkTheme(), // Forced to true
    // dynamicColor: Boolean = true, // Dynamic color disabled for now
    content: @Composable () -> Unit
) {
    // Force dark theme
    val darkTheme = true
    val colorScheme = DarkColorScheme // Always use the new DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color to match the primary color of the dark theme (BrightBlueButton)
            window.statusBarColor = colorScheme.primary.toArgb()
            // Ensure status bar icons are light for dark theme
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 