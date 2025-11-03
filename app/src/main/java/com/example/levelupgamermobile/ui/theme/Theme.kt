package com.example.levelupgamermobile.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// (1) Define el ColorScheme usando tus colores
private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface
    // Puedes definir "tertiary", "surfaceVariant", etc.,
    // o dejar que Material 3 los genere automáticamente.
)

@Composable
fun LevelUpGamerMobileTheme(
    // (2) Forzamos el tema oscuro, ya que es la identidad de tu marca
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Desactivamos colores dinámicos de Android
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // Usamos siempre el tema oscuro

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // (Definiremos esto en el Paso 2)
        content = content
    )
}