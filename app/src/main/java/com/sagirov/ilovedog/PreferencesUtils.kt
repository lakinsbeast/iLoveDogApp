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

    fun getBoolean(nameOfPrefs: String, name: String, defValue: Boolean): Boolean {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.getBoolean(name, defValue)
    }

    fun putBoolean(nameOfPrefs: String, key: String, value: Boolean): Unit {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.edit().putBoolean(key, value).apply()
    }

    fun getString(nameOfPrefs: String, name: String, defValue: String): String? {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.getString(name, defValue)
    }

    fun putString(nameOfPrefs: String, key: String, value: String): Unit {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.edit().putString(key, value).apply()
    }

    fun getInt(nameOfPrefs: String, key: String, defValue: Int): Int {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.getInt(key, defValue)
    }

    fun putInt(nameOfPrefs: String, key: String, value: Int): Unit {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.edit().putInt(key, value).apply()
    }

    fun getLong(nameOfPrefs: String, key: String, defValue: Long): Long {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.getLong(key, defValue)
    }

    fun putLong(nameOfPrefs: String, key: String, value: Long): Unit {
        prefsEdit = contextOfApplication.applicationContext.getSharedPreferences(nameOfPrefs, MODE)
        return prefsEdit.edit().putLong(key, value).apply()
    }
}