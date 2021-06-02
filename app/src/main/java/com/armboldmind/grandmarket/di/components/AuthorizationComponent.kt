package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.di.modules.validators.ValidatorModule
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class, ValidatorModule::class])
interface AuthorizationComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): AuthorizationComponent
    }

    fun inject(userViewModel: UserViewModel)
}