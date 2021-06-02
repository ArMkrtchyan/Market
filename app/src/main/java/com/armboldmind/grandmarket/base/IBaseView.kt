package com.armboldmind.grandmarket.base

import androidx.annotation.StringRes
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.mvi.states.ViewState

interface IBaseView {

    fun showError(keys: Keys? = null, message: Throwable, onRetryClick: (() -> Unit)? = null)
    fun showToast(message: String)
    fun showToast(@StringRes resId: Int)
    fun showSnackBar(message: String)
    fun showSnackBar(@StringRes resId: Int)
    fun setLightStatusBar()
    fun clearLightStatusBar()
    fun hasPermission(permission: String): Boolean
    fun hideSoftInput()
    fun hideBottomNavigation()
    fun showBottomNavigation()
    fun handleViewState(viewState: ViewState)
}