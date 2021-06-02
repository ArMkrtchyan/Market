package com.armboldmind.grandmarket.base

import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), IBaseView {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun showError(keys: Keys?, message: Throwable, onRetryClick: (() -> Unit)?) {

    }

    override fun showToast(message: String) {

    }

    override fun showToast(resId: Int) {

    }

    override fun showSnackBar(message: String) {

    }

    override fun showSnackBar(resId: Int) {

    }

    override fun setLightStatusBar() {

    }

    override fun clearLightStatusBar() {

    }

    override fun hasPermission(permission: String): Boolean {
        return (activity as BaseActivity<*>).hasPermission(permission)
    }

    override fun hideSoftInput() {

    }

    override fun hideBottomNavigation() {

    }

    override fun showBottomNavigation() {

    }

    override fun handleViewState(viewState: ViewState) {

    }
}