package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.ui.products.ProductsViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class])
interface ProductsComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ProductsComponent
    }

    fun inject(productsViewModel: ProductsViewModel)
}