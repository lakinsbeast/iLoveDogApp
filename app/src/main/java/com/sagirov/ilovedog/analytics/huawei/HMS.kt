package com.sagirov.ilovedog.analytics.huawei

import android.content.Context
import android.util.Log
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import javax.inject.Inject

class HMS @Inject constructor() {
    fun checkHuaweiApi(ctx: Context): Boolean {
        Log.d(
            "isHMSavailable",
            (HuaweiApiAvailability.getInstance()
                .isHuaweiMobileServicesAvailable(ctx) == ConnectionResult.SUCCESS).toString()
        )
        return HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(ctx) == ConnectionResult.SUCCESS
    }
    private lateinit var HMSinstance: HiAnalyticsInstance

    fun enableHuaweiAnalytics(ctx: Context) {
        HiAnalyticsTools.enableLog()
        HMSinstance = HiAnalytics.getInstance(ctx)
        HMSinstance.setAnalyticsEnabled(true)
    }
}
