package com.sagirov.ilovedog.domain.utils.analytics.google

import android.content.Context

interface GMS {
    fun checkGoogleApi(ctx: Context): Boolean
    fun enableGoogleAnalytics(ctx: Context)
}