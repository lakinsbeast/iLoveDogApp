package com.sagirov.ilovedog.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

var mainTextColor = Color(0xFF000000)
var mainBackgroundColor = Color(0xFFB8D0B3)
var mainSecondColor = Color(0xFFD0E0CC)
var homeButtonColor = Color(0xFF8AB181)
var bottomNavBackgroundColor = Color(0xFFB8D0B3)
var healthBarPastReminderColor = Color(0xFFdfeadc)
var encyclopediaDogBarColor = Color(0xFF706F8E)
var circularColor = Color(0xFF22222E)
var switcherColor = Color.Black
var textFieldUnFocusedIndicatorColor = Color.Gray

class ColorPalette(
    var mainTextColor: Color,
    var mainBackgroundColor: Color,
    var mainSecondColor: Color,
    var homeButtonColor: Color,
    var bottomNavBackgroundColor: Color,
    var healthBarPastReminderColor: Color,
    var encyclopediaDogBarColor: Color,
    var circularColor: Color,
    var switcherColor: Color,
    var textFieldUnFocusedIndicatorColor: Color
)

val lightColor = ColorPalette(
    Color(0xFFFFFFFF), Color(0xFFE9E9E9), Color(0xFF000000), Color(0xFF92B4EC),
    Color(0xFFFFFFFF), Color(0xFFADA9BA), Color(0xFF706F8E), Color(0xFF22222E),
    Color.Black, Color.Gray
)
val darkTheme = ColorPalette(
    Color(0xFF121212), Color(0xFF323232), Color(0xFFFFFFFF), Color(0xFF465164),
    Color(0xFF121212), Color(0xFFADA9BA), Color(0xFF706F8E), Color(0xFF8A8ABB),
    Color.White, Color.Gray
)


@Composable
fun ApplicationTheme(darkThemeBool: Boolean, content: @Composable () -> Unit) {
    val appTheme = if (darkThemeBool) {
        compositionLocalOf<ColorPalette> { darkTheme }
    } else {
        compositionLocalOf<ColorPalette> { lightColor }
    }
    CompositionLocalProvider(appTheme provides lightColor) {
        MaterialTheme(
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

class CheckDarkMode() {
    companion object {
        fun isDarkMode(state: Boolean) {
            if (!state) {
                mainBackgroundColor = Color(0xFFFFFFFF)
                mainSecondColor = Color(0xFFE9E9E9)
                mainTextColor = Color(0xFF000000)
                homeButtonColor = Color(0xFF92B4EC)
                bottomNavBackgroundColor = Color(0xFFFFFFFF)
                healthBarPastReminderColor = Color(0xFFADA9BA)
                encyclopediaDogBarColor = Color(0xFF706F8E)
                circularColor = Color(0xFF22222E)
                switcherColor = Color.Black
                textFieldUnFocusedIndicatorColor = Color.Gray
            } else {
                mainBackgroundColor = Color(0xFF121212)
                mainSecondColor = Color(0xFF323232)
                mainTextColor = Color(0xFFFFFFFF)
                homeButtonColor = Color(0xFF465164)
                bottomNavBackgroundColor = Color(0xFF121212)
                healthBarPastReminderColor = Color(0xFFADA9BA)
                encyclopediaDogBarColor = Color(0xFF706F8E)
                circularColor = Color(0xFF8A8ABB)
                switcherColor = Color.White
                textFieldUnFocusedIndicatorColor = Color.Gray
            }
        }
    }
}


