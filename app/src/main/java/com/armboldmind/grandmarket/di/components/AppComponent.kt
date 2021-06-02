package com.armboldmind.grandmarket.di.components

import android.content.Context
import com.armboldmind.grandmarket.GrandMarketApp
import com.armboldmind.grandmarket.data.database.GrandMarketDB
import com.armboldmind.grandmarket.di.modules.AppModule
import com.armboldmind.grandmarket.di.modules.DatabaseModule
import com.armboldmind.grandmarket.di.modules.MapperModule
import com.armboldmind.grandmarket.di.modules.NetworkModule
import com.armboldmind.grandmarket.shared.managers.PreferencesManager
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class, DatabaseModule::class, MapperModule::class])
interface AppComponent {
    fun context(): Context
    fun retrofit(): Retrofit
    fun preferences(): PreferencesManager
    fun grandMarketDb(): GrandMarketDB

    fun inject(grandMarketApp: GrandMarketApp)

    val authorizationComponent: AuthorizationComponent.Builder
    val moreComponent: MoreComponent.Builder
    val addressComponent: AddressComponent.Builder
    val notificationsComponent: NotificationsComponent.Builder
    val languagesComponent: LanguagesComponent.Builder
    val infoComponent: InfoComponent.Builder
    val homeComponent: HomeComponent.Builder
    val categoriesComponent: CategoriesComponent.Builder
    val productComponent: ProductsComponent.Builder
    val requestsProductComponent: RequestsProductComponent.Builder
    val mainActivityComponent: MainActivityComponent.Builder
}