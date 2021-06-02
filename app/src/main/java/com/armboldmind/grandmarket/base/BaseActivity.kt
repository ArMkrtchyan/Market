package com.armboldmind.grandmarket.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.shared.customview.DialogFactory
import com.armboldmind.grandmarket.shared.enums.LanguagesEnum
import com.armboldmind.grandmarket.shared.globalextensions.getColorCompat
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.google.android.material.snackbar.Snackbar
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), IBaseView {

    inline fun <reified T : BaseViewModel> createViewModel(modelClass: Class<T>, view: IBaseView? = null): T {
        return ViewModelProvider(this).get(modelClass)
            .apply { view?.let { setStateHandler(it::handleViewState) } }
    }

    private lateinit var _binding: VB
    protected val mBinding: VB
        get() = _binding

    protected abstract val inflate: (LayoutInflater) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflate(layoutInflater)
        setContentView(_binding.root)
        setLightStatusBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    abstract fun setLoading(isLoading: Boolean)
    override fun showError(keys: Keys?, message: Throwable, onRetryClick: (() -> Unit)?) {
        if (message is SocketTimeoutException || message is UnknownHostException) DialogFactory.Builder(this)
            .imageRes(R.drawable.ic_network_error)
            .title(keys?.network_error ?: "Null")
            .description(keys?.default_network_error_description ?: "Null")
            .positiveButtonText(keys?.retry ?: "Null")
            .positiveButtonClick {
                onRetryClick?.let { it.invoke() }
            }
            .build()
        else if (message is BaseDataSource.SuccessException) {
            showSnackBar(message.localizedMessage ?: keys?.default_error_message ?: "Null")
        } else DialogFactory.Builder(this)
            .imageRes(R.drawable.ic_server_error)
            .title(keys?.server_error ?: "Null")
            .description(keys?.default_error_message ?: "Null")
            .positiveButtonText(keys?.retry ?: "Null")
            .positiveButtonClick {
                onRetryClick?.let { it.invoke() }
            }
            .build()
        setLoading(false)
        Log.i("ExceptionTAg", message.localizedMessage ?: "")
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
            .setTextColor(getColorCompat(android.R.color.white))
            .setBackgroundTint(getColorCompat(android.R.color.black))
            .show()
    }

    override fun showSnackBar(@StringRes resId: Int) {
        showSnackBar(resources.getString(resId))
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }

    override fun showToast(resId: Int) {
        showToast(resources.getString(resId))
    }

    override fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun hideSoftInput() {
        val view: View? = currentFocus
        if (view != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun setLightStatusBar() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            else -> {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
        setStatusBarColor(R.color.white)
    }

    override fun clearLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        setStatusBarColor(R.color.colorPrimaryDark)
    }

    fun setStatusBarColor(id: Int) {
        window?.statusBarColor = ContextCompat.getColor(this, id)
    }

    fun updateResources(language: String) {
        val res = this.resources
        val conf = res.configuration
        val newLocale = when (language) {
            LanguagesEnum.ARMENIAN.code -> Locale(language, "AM")
            LanguagesEnum.ENGLISH.code -> Locale(language, "GB")
            LanguagesEnum.RUSSIAN.code -> Locale(language, "RU")
            else -> Locale.getDefault()
        }
        conf.setLocale(newLocale)
        Locale.setDefault(newLocale)
        this.baseContext.resources.updateConfiguration(conf, this.baseContext.resources.displayMetrics)
        this.resources.updateConfiguration(conf, this.resources.displayMetrics)
        res.updateConfiguration(conf, res.displayMetrics)
    }

    override fun hideBottomNavigation() {

    }

    override fun showBottomNavigation() {

    }

    override fun handleViewState(viewState: ViewState) {

    }
}