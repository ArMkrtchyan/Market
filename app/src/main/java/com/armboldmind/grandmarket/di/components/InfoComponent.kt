package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.ui.more.informative.InfoViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class])
interface InfoComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): InfoComponent
    }

    fun inject(infoViewModel: InfoViewModel)
}