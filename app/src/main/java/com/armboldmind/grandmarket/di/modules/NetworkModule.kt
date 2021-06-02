package com.armboldmind.grandmarket.di.modules

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.armboldmind.grandmarket.BuildConfig
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.network.services.*
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager //import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(context: Context): OkHttpClient {
        @SuppressLint("HardwareIds")
        val deviceID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val mobileModel: String = String.format("%s %s", Build.MANUFACTURER, Build.MODEL)
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer ${preferencesManager().findByKey<String>(BundleKeysEnum.ACCESS_TOKEN.key)}")
                    .header("DeviceId", deviceID)
                    .header("DeviceToken", preferencesManager().findByKey(BundleKeysEnum.DEVICE_TOKEN.key))
                    .header("osVersion", Build.VERSION.SDK_INT.toString())
                    .header("appVersion", BuildConfig.VERSION_NAME + ", " + BuildConfig.VERSION_CODE)
                    .header("OsTypeId", "1")
                    .header("Model", mobileModel + ", " + Build.VERSION.RELEASE)
                    .header("LanguageId", "${preferencesManager().findByKey<Language?>(BundleKeysEnum.APP_LANGUAGE.key)?.id ?: 1}")
                    .build()
                val response = chain.proceed(request)
                response
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS) //.addInterceptor(ChuckInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASEURL_STAGING)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesUserService(retrofit: Retrofit): IUserService {
        return retrofit.create(IUserService::class.java)
    }

    @Provides
    fun providesAddressService(retrofit: Retrofit): IAddressService {
        return retrofit.create(IAddressService::class.java)
    }

    @Provides
    fun providesNotificationService(retrofit: Retrofit): INotificationService {
        return retrofit.create(INotificationService::class.java)
    }

    @Provides
    fun providesInfoService(retrofit: Retrofit): IInfoService {
        return retrofit.create(IInfoService::class.java)
    }

    @Provides
    fun providesLanguagesService(retrofit: Retrofit): ILanguageService {
        return retrofit.create(ILanguageService::class.java)
    }

    @Provides
    fun providesRequestProductService(retrofit: Retrofit): IRequestProductService {
        return retrofit.create(IRequestProductService::class.java)
    }

    @Provides
    fun providesBannerService(retrofit: Retrofit): IBannerService {
        return retrofit.create(IBannerService::class.java)
    }

    @Provides
    fun providesProductService(retrofit: Retrofit): IProductService {
        return retrofit.create(IProductService::class.java)
    }

    @Provides
    fun providesCategoryService(retrofit: Retrofit): ICategoryService {
        return retrofit.create(ICategoryService::class.java)
    }

    @Provides
    fun providesBrandService(retrofit: Retrofit): IBrandService {
        return retrofit.create(IBrandService::class.java)
    }
}