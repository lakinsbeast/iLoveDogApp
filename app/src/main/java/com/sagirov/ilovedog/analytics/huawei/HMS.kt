package com.sagirov.ilovedog.analytics.huawei

import android.content.Context
import android.util.Log
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability

class HMS {
    fun checkHuaweiApi(ctx: Context): Boolean {
        Log.d(
            "isHMSavailable",
            (HuaweiApiAvailability.getInstance()
                .isHuaweiMobileServicesAvailable(ctx) == ConnectionResult.SUCCESS).toString()
        )
//        var isAvailable = false
//        var result = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(ctx)
//        isAvailable = ConnectionResult.SUCCESS == result
        return HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(ctx) == ConnectionResult.SUCCESS
    }
}


//fun getInstance(var0: Activity?): HiAnalyticsInstance? {
//    return HiAnalyticsInstance.lmn(var0)
//}