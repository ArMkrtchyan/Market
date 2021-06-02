package com.armboldmind.grandmarket

import android.app.Application
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.di.components.AppComponent
import com.armboldmind.grandmarket.di.components.DaggerAppComponent
import com.armboldmind.grandmarket.di.modules.AppModule
import com.armboldmind.grandmarket.di.modules.NetworkModule
import com.armboldmind.grandmarket.shared.managers.ConnectivityManager
import com.armboldmind.grandmarket.shared.managers.PreferencesManager
import java.util.*
import javax.inject.Inject

class GrandMarketApp : Application() {

    lateinit var mAppComponent: AppComponent
    val connectivityManager by lazy { ConnectivityManager(this) }
    val unAuthorizationLiveDate by lazy { MutableLiveData(false) }
    val keysLiveData by lazy { MutableLiveData<Keys>() }

    @Inject
    lateinit var mPreferencesManager: PreferencesManager

    companion object {
        private lateinit var mInstance: GrandMarketApp
        fun getInstance(): GrandMarketApp {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        WebView(this).destroy()
        mInstance = this
        mAppComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .build()
            .apply { inject(this@GrandMarketApp) }
    }
}