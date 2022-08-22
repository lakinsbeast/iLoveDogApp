package com.sagirov.ilovedog.analytics.google

import android.content.Context
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class GMS @Inject constructor() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun checkGoogleApi(ctx: Context): Boolean {
        Log.d(
            "isGMSavailable",
            (GoogleApiAvailabilityLight.getInstance()
                .isGooglePlayServicesAvailable(ctx) == ConnectionResult.SUCCESS).toString()
        )
        return GoogleApiAvailabilityLight.getInstance()
            .isGooglePlayServicesAvailable(ctx) == ConnectionResult.SUCCESS
    }

    fun enableGoogleAnalytics(ctx: Context) {
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
    }
}