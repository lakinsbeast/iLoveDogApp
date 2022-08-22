package com.sagirov.ilovedog.di

import android.content.Context
import com.sagirov.ilovedog.Utils.CheckDarkModeManager
import com.sagirov.ilovedog.Utils.CheckFirstLaunchManager
import com.sagirov.ilovedog.Utils.PreferencesUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesUtils(@ApplicationContext ctx: Context): PreferencesUtils {
        return PreferencesUtils(ctx)
    }

    @Provides
    @Singleton
    fun provideDarkModeManager(@ApplicationContext ctx: Context): CheckDarkModeManager {
        return CheckDarkModeManager(ctx)
    }
    @Provides
    @Singleton
    fun provideFirstLaunchManager(@ApplicationContext ctx: Context): CheckFirstLaunchManager {
        return CheckFirstLaunchManager(ctx)
    }

}