package com.example.levelupgamermobile.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * define la paleta de colores semantica para la app.
 *
 * material 3 usa este 'darkcolorscheme' para aplicar los colores
 * definidos en [Color.kt] (ej. [LvlUpBlue]) a los componentes
 * de forma estandar (ej. 'primary' para botones, 'surface' para tarjetas).
 */
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
    // material 3 genera automaticamente los colores "container"
    // y "variant" basados en estos colores base.
)

/**
 * el composable principal del tema de la aplicacion.
 *
 * esta funcion envuelve todo el contenido de la app (en [MainActivity])
 * y aplica nuestra paleta de colores ([DarkColorScheme]) y
 * tipografia ([Typography]) personalizadas.
 *
 * @param darktheme forzamos que sea 'true' para mantener la identidad
 * de marca (modo oscuro) de la app.
 * @param dynamiccolor se deshabilita ('false') para ignorar
 * los colores del sistema operativo (colores de wallpaper)
 * y usar siempre nuestros colores de marca.
 * @param content el contenido de la app que sera envuelto.
 */
@Composable
fun LevelUpGamerMobileTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // la app solo soporta el tema oscuro para mantener la
    // identidad "gamer" definida en el css.
    val colorScheme = DarkColorScheme

    // este bloque 'sideeffect' permite controlar vistas de android
    // que estan "fuera" de compose.
    // aqui lo usamos para cambiar el color de la barra de estado
    // del sistema (donde se ve la hora y la bateria) para
    // que coincida con el fondo de nuestra app.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // aplica el tema de material 3 a todo el '@composable content'
    // que esta dentro de el.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // definido en [Type.kt]
        content = content
    )
}