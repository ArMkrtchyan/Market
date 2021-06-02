package com.armboldmind.grandmarket.di.modules

import android.app.Application
import android.content.Context
import com.armboldmind.grandmarket.shared.managers.PreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providePreferences(): PreferencesManager {
        return PreferencesManager(application)
    }
}