package com.quickmath.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val Accent = Color(0xFF4F46E5)
private val AccentLight = Color(0xFFEEF2FF)
private val Surface = Color(0xFFF8F8FC)
private val Border = Color(0xFFE5E5F0)
private val Ink = Color(0xFF0D0D14)
private val Secondary = Color(0xFF6B6B80)
private val Success = Color(0xFF16A34A)
private val Danger = Color(0xFFDC2626)
private val Gold = Color(0xFFF59E0B)

private val LightColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Color.White,
    primaryContainer = AccentLight,
    onPrimaryContainer = Accent,
    surface = Surface,
    onSurface = Ink,
    outline = Border,
    error = Danger,
    onError = Color.White,
    tertiary = Success,
)

@Composable
fun QuickMathTheme(
    content: @Composable () -> Unit,
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode && view.context is Activity) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuickMathTypography,
        content = content,
    )
}

object AppColors {
    val accent = Accent
    val accentLight = AccentLight
    val surface = Surface
    val border = Border
    val ink = Ink
    val secondary = Secondary
    val success = Success
    val danger = Danger
    val gold = Gold
}
