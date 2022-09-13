package com.sagirov.ilovedog.di

import com.sagirov.ilovedog.analytics.google.GMS
import com.sagirov.ilovedog.analytics.google.GMSImpl
import com.sagirov.ilovedog.analytics.huawei.HMS
import com.sagirov.ilovedog.analytics.huawei.HMSImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    fun bindHuaweiAnalytics(huaweiAnalytics: HMSImpl): HMS = huaweiAnalytics

    @Provides
    fun bindGoogleAnalytics(googleAnalytics: GMSImpl): GMS = googleAnalytics

}