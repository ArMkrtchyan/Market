package com.armboldmind.grandmarket.ui.more

import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.database.repositories.MoreRepositoryLocal
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.globalextensions.appComponent
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoreViewModel : BaseViewModel() {
    @Inject
    lateinit var mMoreRepositoryLocal: MoreRepositoryLocal

    init {
        appComponent().moreComponent.build()
            .inject(this)
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch {
            when (actionIntent) {
                is ActionIntent.FetchUserItems -> fetchUserItems()
                is ActionIntent.FetchGlobalItems -> fetchGlobalItems()
                else -> Unit
            }
        }
    }

    private suspend fun fetchUserItems() {
        mMoreRepositoryLocal.createUserItems()
            .collect { setState.invoke(ViewState.FetchUserItemsState(it)) }
    }

    private suspend fun fetchGlobalItems() {
        mMoreRepositoryLocal.createGlobalItems()
            .collect { setState.invoke(ViewState.FetchGlobalItemsState(it)) }
    }

    suspend fun getKeys(): Flow<Keys> {
        return mMoreRepositoryLocal.getKeys()
    }
}