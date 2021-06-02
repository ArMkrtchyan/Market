package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.ui.categories.CategoryViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class])
interface CategoriesComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): CategoriesComponent
    }

    fun inject(categoriesViewModel: CategoryViewModel)
}