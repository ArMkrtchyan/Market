package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.ui.more.addresses.AddressViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class])
interface AddressComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): AddressComponent
    }

    fun inject(addressViewModel: AddressViewModel)
}