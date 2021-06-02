package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.ui.init.LanguageViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class])
interface LanguagesComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): LanguagesComponent
    }

    fun inject(languageViewModel: LanguageViewModel)
}