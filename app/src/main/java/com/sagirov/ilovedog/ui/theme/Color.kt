package com.sagirov.ilovedog.ui.theme

import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)


var mainTextColor = Color(0xFF000000)
var secondaryTextColor = Color(0xFF888888)
var mainBackgroundColor = Color(0xFFB8D0B3)
var mainSecondColor = Color(0xFFD0E0CC)
var homeButtonColor = Color(0xFF8AB181)
var healthBarPastReminderColor = Color(0xFFdfeadc)


class CheckDarkMode(){
    companion object {
        fun isDarkMode(state: Boolean) {
            if (state) {
                mainBackgroundColor = Color(0xFFB8D0B3)
                mainSecondColor = Color(0xFFD0E0CC)
                homeButtonColor = Color(0xFF8AB181)
            } else {
                mainBackgroundColor = Color(0xFF2D302D)
                mainSecondColor = Color(0xFF2E2E2E)
                homeButtonColor = Color(0xFF8AB181)
            }
        }
    }
}


