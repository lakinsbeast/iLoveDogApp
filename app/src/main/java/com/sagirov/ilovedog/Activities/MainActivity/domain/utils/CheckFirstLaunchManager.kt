package com.sagirov.ilovedog.Activities.MainActivity.domain.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.sagirov.ilovedog.Activities.FirstLaunchActivity
import com.sagirov.ilovedog.Utils.PreferencesUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CheckFirstLaunchManager @Inject constructor(@ApplicationContext ctx: Context) {
    @Inject
    lateinit var newPrefs: PreferencesUtils
    val context = ctx
    fun checkFirstLaunch() {
        newPrefs = PreferencesUtils(context)
        val frst_lnch = newPrefs.getBoolean(PreferencesUtils.PREF_NAME, "firstOpen", true)
        if (frst_lnch) {
            newPrefs.putLong(PreferencesUtils.PREF_SCORE, "time", System.currentTimeMillis())
            context.startActivity(Intent(context, FirstLaunchActivity::class.java))
            (context as Activity).finish()
        }
    }

}