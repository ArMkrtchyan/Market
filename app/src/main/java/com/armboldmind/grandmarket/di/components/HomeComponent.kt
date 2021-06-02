package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.ui.home.HomeViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class])
interface HomeComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): HomeComponent
    }

    fun inject(homeViewModel: HomeViewModel)
}