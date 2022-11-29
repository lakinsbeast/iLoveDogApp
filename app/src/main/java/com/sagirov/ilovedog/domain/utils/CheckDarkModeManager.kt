package com.sagirov.ilovedog.domain.utils

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.sagirov.ilovedog.domain.utils.theme.CheckDarkMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
//TODO{ПЕРЕВЕСТИ В STATIC(COMPANION/INNER)}
class CheckDarkModeManager @Inject constructor(@ApplicationContext ctx: Context) {
    @Inject
    lateinit var newPrefs: PreferencesUtils
    var isNightMode = mutableStateOf(false)
    val context = ctx

    fun checkDarkMode() {
        newPrefs = PreferencesUtils(context)
        isNightMode.value = newPrefs.getBoolean(PreferencesUtils.PREF_NIGHT_MODE, "isNightModeOn", false)
        CheckDarkMode.isDarkMode(isNightMode.value)
    }
    fun isDarkMode(): Boolean {
        return isNightMode.value
    }

    fun setDarkMode(boolean: Boolean) {
        isNightMode.value = boolean
    }
}