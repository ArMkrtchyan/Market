package com.armboldmind.grandmarket.di.modules

import com.armboldmind.grandmarket.data.mappers.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapperModule {
    @Provides
    @Singleton
    fun provideUserMapper() = UserMapper()

    @Provides
    @Singleton
    fun provideNotificationMapper() = NotificationMapper()

    @Provides
    @Singleton
    fun provideNewsMapper() = NewsAndEventsMapper()

    @Provides
    @Singleton
    fun provideNewsDetailsMapper() = NewsAndEventsDetailsMapper()

    @Provides
    @Singleton
    fun provideRequestProductMapper() = RequestProductMapper()

    @Provides
    @Singleton
    fun provideBannerMapper() = BannerMapper()

    @Provides
    @Singleton
    fun provideCategoryMapper() = CategoryMapper()

    @Provides
    @Singleton
    fun provideProductMapper() = ProductMapper()

    @Provides
    @Singleton
    fun provideFavoritesMapper() = FavoritesMapper()

    @Provides
    @Singleton
    fun provideBrandMapper() = BrandMapper()

    @Provides
    @Singleton
    fun provideFilterMapper() = FilterMapper()

    @Provides
    @Singleton
    fun provideProductDetailsMapper() = ProductDetailsMapper()
}