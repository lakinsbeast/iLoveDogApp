package com.sagirov.ilovedog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    onPrimary = Color(0xFF000000),
    primaryVariant = Purple700,

    secondary = Teal200,

    background = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF121212),
    onSurface = Color(0xFFFFFFFF),
    error = Color(0xFFF297A2),
    onError = Color(0xFF000000),

    )

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = Color.White

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun NewTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = CheckDarkMode.isDarkMode(darkTheme)


    MaterialTheme() {

    }
}

@Composable
fun ILoveDogTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}