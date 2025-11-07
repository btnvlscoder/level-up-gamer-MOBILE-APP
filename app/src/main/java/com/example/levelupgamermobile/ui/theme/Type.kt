package com.example.levelupgamermobile.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.levelupgamermobile.R

/**
 * define la familia de fuentes 'orbitron' personalizada.
 *
 * carga los archivos .ttf (ej. 'orbitron_regular.ttf')
 * desde la carpeta de recursos 'res/font' usando sus
 * identificadores de [R.font].
 */
val Orbitron = FontFamily(
    Font(R.font.orbitron_regular, FontWeight.Normal),
    Font(R.font.orbitron_bold, FontWeight.Bold)
)

/**
 * define la familia de fuentes 'roboto'.
 *
 * usamos [FontFamily.Default] que es la fuente
 * estandar del sistema android (generalmente roboto).
 */
val Roboto = FontFamily.Default

/**
 * define la escala tipografica completa para la aplicacion,
 * siguiendo el estandar de [MaterialTheme].
 *
 * esta variable [Typography] es consumida por [LevelUpGamerMobileTheme].
 *
 * aqui mapeamos nuestras fuentes personalizadas:
 * - [Orbitron]: se usa para todos los titulos (headlines, titles).
 * - [Roboto]: se usa para el texto del cuerpo (body) y etiquetas (label).
 */
val Typography = Typography(
    // --- titulos (con orbitron) ---
    headlineLarge = TextStyle(
        fontFamily = Orbitron,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Orbitron,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Orbitron,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    // --- cuerpo (con roboto) ---
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)