package com.sagirov.ilovedog.analytics.google

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class GMSImpl @Inject constructor() : GMS {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun checkGoogleApi(ctx: Context): Boolean {
        return GoogleApiAvailabilityLight.getInstance()
            .isGooglePlayServicesAvailable(ctx) == ConnectionResult.SUCCESS
    }

    override fun enableGoogleAnalytics(ctx: Context) {
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
    }
}