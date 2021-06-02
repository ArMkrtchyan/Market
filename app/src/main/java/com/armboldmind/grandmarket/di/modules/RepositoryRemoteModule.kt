package com.armboldmind.grandmarket.di.modules

import com.armboldmind.grandmarket.data.*
import com.armboldmind.grandmarket.data.network.repositories.*
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryRemoteModule {

    @Binds
    abstract fun provideUserRepositoryRemote(userRepositoryRemote: UserRepositoryRemote): IUserRepository

    @Binds
    abstract fun provideAddressRepositoryRemote(addressRepositoryRemote: AddressRepositoryRemote): IAddressRepository

    @Binds
    abstract fun provideNotificationRepositoryRemote(notificationRepositoryRemote: NotificationRepositoryRemote): INotificationRepository

    @Binds
    abstract fun provideInfoRepositoryRemote(infoRepository: InfoRepository): IInfoRepository

    @Binds
    abstract fun provideLanguagesRepositoryRemote(languagesRepositoryRemote: LanguagesRepositoryRemote): ILanguageRepository

    @Binds
    abstract fun provideRequestProductRepositoryRemote(requestProductRepositoryRemote: RequestProductRepositoryRemote): IRequestProductRepository

    @Binds
    abstract fun provideBannersRepositoryRemote(bannersRepositoryRemote: BannersRepositoryRemote): IBannersRepository

    @Binds
    abstract fun provideProductsRepositoryRemote(productRepositoryRemote: ProductsRepositoryRemote): IProductsRepository

    @Binds
    abstract fun provideCategoriesRepositoryRemote(categoriesRepositoryRemote: CategoriesRepositoryRemote): ICategoriesRepository

    @Binds
    abstract fun provideBrandsRepositoryRemote(brandsRepositoryRemote: BrandsRepositoryRemote): IBrandsRepository
}