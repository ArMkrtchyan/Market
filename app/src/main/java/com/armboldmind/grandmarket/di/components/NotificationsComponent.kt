package com.armboldmind.grandmarket.di.components

import com.armboldmind.grandmarket.di.modules.RepositoryLocaleModule
import com.armboldmind.grandmarket.di.modules.RepositoryRemoteModule
import com.armboldmind.grandmarket.ui.more.notifications.NotificationsViewModel
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryRemoteModule::class, RepositoryLocaleModule::class])
interface NotificationsComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): NotificationsComponent
    }

    fun inject(notificationsViewModel: NotificationsViewModel)
}