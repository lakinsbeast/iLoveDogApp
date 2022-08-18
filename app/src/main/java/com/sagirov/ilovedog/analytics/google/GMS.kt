package com.sagirov.ilovedog.analytics.google

import android.content.Context
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight

class GMS {
    fun checkGoogleApi(ctx: Context): Boolean {
        Log.d(
            "isGMSavailable",
            (GoogleApiAvailabilityLight.getInstance()
                .isGooglePlayServicesAvailable(ctx) == ConnectionResult.SUCCESS).toString()
        )
        return GoogleApiAvailabilityLight.getInstance()
            .isGooglePlayServicesAvailable(ctx) == ConnectionResult.SUCCESS
    }
}