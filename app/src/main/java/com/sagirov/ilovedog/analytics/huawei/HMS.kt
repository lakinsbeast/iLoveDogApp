package com.sagirov.ilovedog.analytics.huawei

import android.content.Context


interface HMS {
    fun checkHuaweiApi(ctx: Context): Boolean
    fun enableHuaweiAnalytics(ctx: Context)
}
