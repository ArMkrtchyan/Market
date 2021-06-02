package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.ui.more.requests.RequestProductViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class])
interface RequestsProductComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): RequestsProductComponent
    }

    fun inject(requestProductViewModel: RequestProductViewModel)
}