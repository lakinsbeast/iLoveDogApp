package com.sagirov.ilovedog.analytics.google

import android.content.Context

interface GMS {
    fun checkGoogleApi(ctx: Context): Boolean
    fun enableGoogleAnalytics(ctx: Context)
}