package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.ui.more.MoreViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class])
interface MoreComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MoreComponent
    }

    fun inject(moreViewModel: MoreViewModel)
}