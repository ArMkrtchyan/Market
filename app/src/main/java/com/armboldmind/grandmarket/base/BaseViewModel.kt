package com.armboldmind.grandmarket.base

import androidx.lifecycle.ViewModel
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState

abstract class BaseViewModel : ViewModel() {
    abstract fun sendIntent(actionIntent: ActionIntent)
    protected lateinit var setState: (ViewState) -> Unit

    fun setStateHandler(setState: (ViewState) -> Unit) {
        this.setState = setState
    }

    protected fun sendLoadingState() {
        setState.invoke(ViewState.LoadingState)
    }

    protected fun sendErrorState(throwable: Throwable) {
        setState.invoke(ViewState.ErrorState(throwable))
    }
}