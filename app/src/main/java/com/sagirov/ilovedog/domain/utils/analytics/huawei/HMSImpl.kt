package com.sagirov.ilovedog.domain.utils.analytics.huawei

import android.content.Context
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import javax.inject.Inject

class HMSImpl @Inject constructor() : HMS {
    private lateinit var HMSinstance: HiAnalyticsInstance

    override fun checkHuaweiApi(ctx: Context): Boolean {
        return HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(ctx) == ConnectionResult.SUCCESS
    }

    override fun enableHuaweiAnalytics(ctx: Context) {
        HiAnalyticsTools.enableLog()
        this.HMSinstance = HiAnalytics.getInstance(ctx)
        this.HMSinstance.setAnalyticsEnabled(true)
    }
}