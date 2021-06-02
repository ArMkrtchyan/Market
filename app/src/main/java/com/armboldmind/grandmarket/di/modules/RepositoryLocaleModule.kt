package com.armboldmind.grandmarket.di.modules

import com.armboldmind.grandmarket.data.IMoreRepository
import com.armboldmind.grandmarket.data.database.repositories.MoreRepositoryLocal
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryLocaleModule {

    @Binds
    abstract fun provideMoreRepositoryLocal(moreRepositoryLocal: MoreRepositoryLocal): IMoreRepository
}