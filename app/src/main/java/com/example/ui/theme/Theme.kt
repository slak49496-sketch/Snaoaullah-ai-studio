package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkStudioColorScheme = darkColorScheme(
    primary = Indigo500,
    onPrimary = Color.White,
    primaryContainer = Slate800,
    onPrimaryContainer = Slate100,
    secondary = TikTokPink,
    onSecondary = Color.White,
    secondaryContainer = TikTokPink.copy(alpha = 0.2f),
    onSecondaryContainer = TikTokPink,
    tertiary = TikTokCyan,
    onTertiary = Slate950,
    background = Slate950,
    onBackground = Slate100,
    surface = Slate900,
    onSurface = Slate100,
    surfaceVariant = Slate850,
    onSurfaceVariant = Slate300,
    outline = Slate700,
    outlineVariant = Slate800,
    error = TikTokPink
)

val LightStudioColorScheme = lightColorScheme(
    primary = Indigo600,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEFF6FF),
    onPrimaryContainer = Color(0xFF1E293B),
    secondary = TikTokPink,
    onSecondary = Color.White,
    secondaryContainer = TikTokPink.copy(alpha = 0.12f),
    onSecondaryContainer = TikTokPink,
    tertiary = Indigo500,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF334155),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    error = TikTokPink
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkStudioColorScheme else LightStudioColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
