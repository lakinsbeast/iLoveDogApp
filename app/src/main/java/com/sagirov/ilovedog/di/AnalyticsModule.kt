package com.sagirov.ilovedog.di

import com.sagirov.ilovedog.analytics.google.GMS
import com.sagirov.ilovedog.analytics.huawei.HMS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideHuaweiAnalytics(): HMS {
        return HMS()
    }

    @Provides
    @Singleton
    fun provideGoogleAnalytics(): GMS {
        return GMS()
    }
}