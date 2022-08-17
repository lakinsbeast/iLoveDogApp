package com.sagirov.ilovedog

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesUtils @Inject constructor(@ApplicationContext ctx: Context) {
    private val PREF_NAME = "first_launch"
    private val PREF_NAME_DATES = "dates"
    private val MODE = ComponentActivity.MODE_PRIVATE
    var contextOfApplication: Context = ctx

    @Inject
    private lateinit var prefsEdit: SharedPreferences

    fun getBoolean(nameOfPrefs: String, name: String, bool: Boolean): Boolean {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.getBoolean(name, bool)
    }

    fun putBoolean(nameOfPrefs: String, key: String, value: Boolean): Unit {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.edit().putBoolean(key, value).apply()
    }

    fun getString(nameOfPrefs: String, name: String, standard: String): String? {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.getString(name, standard)
    }

    fun putString(nameOfPrefs: String, key: String, value: String): Unit {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.edit().putString(key, value).apply()
    }

}