package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.ui.MainActivity
import dagger.Subcomponent

@Subcomponent
interface MainActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainActivityComponent
    }

    fun inject(mainActivity: MainActivity)
}