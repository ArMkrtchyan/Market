package com.armboldmind.grandmarket.shared.globalextensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.armboldmind.grandmarket.GrandMarketApp
import com.armboldmind.grandmarket.base.*
import com.armboldmind.grandmarket.di.modules.NetworkModule
import com.armboldmind.grandmarket.shared.customview.StateLayout
import com.armboldmind.grandmarket.shared.enums.EmptyStatesEnum
import com.armboldmind.grandmarket.shared.managers.FirebaseAuthenticationManager
import com.armboldmind.grandmarket.shared.managers.NetworkStatusManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun Context.inflater(): LayoutInflater = LayoutInflater.from(this)
fun ViewGroup.inflater(): LayoutInflater = LayoutInflater.from(this.context)


fun Context.createIntentToApplicationPackage(): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    return intent
}


infix fun Context.callTo(number: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: $number"))
    startActivity(intent)
}

fun BaseActivity<*>.preferencesManager() = GrandMarketApp.getInstance().mPreferencesManager
fun BaseFragment<*>.preferencesManager() = GrandMarketApp.getInstance().mPreferencesManager
fun BaseViewModel.preferencesManager() = GrandMarketApp.getInstance().mPreferencesManager
fun BaseActivity<*>.keysLiveData() = GrandMarketApp.getInstance().keysLiveData
fun BaseViewModel.keysLiveData() = GrandMarketApp.getInstance().keysLiveData
fun BaseFragment<*>.keysLiveData() = GrandMarketApp.getInstance().keysLiveData
fun BottomSheetDialogFragment.keysLiveData() = GrandMarketApp.getInstance().keysLiveData
fun FirebaseAuthenticationManager.keysLiveData() = GrandMarketApp.getInstance().keysLiveData
fun BasePagingAdapter<*, *>.keysLiveData() = GrandMarketApp.getInstance().keysLiveData
fun BaseAdapter<*, *>.keysLiveData() = GrandMarketApp.getInstance().keysLiveData
fun StateLayout.keysLiveData() = GrandMarketApp.getInstance().keysLiveData
fun EmptyStatesEnum.keysLiveData() = GrandMarketApp.getInstance().keysLiveData
fun NetworkModule.preferencesManager() = GrandMarketApp.getInstance().mPreferencesManager

fun BaseActivity<*>.applicationContext() = GrandMarketApp.getInstance().applicationContext
fun BaseViewModel.applicationContext() = GrandMarketApp.getInstance().applicationContext
fun BaseFragment<*>.applicationContext() = GrandMarketApp.getInstance().applicationContext
fun BaseFragment<*>.unAuthorizationLiveData() = GrandMarketApp.getInstance().unAuthorizationLiveDate
fun NetworkStatusManager.applicationContext() = GrandMarketApp.getInstance().applicationContext

fun BaseFragment<*>.connectivityManager() = GrandMarketApp.getInstance().connectivityManager
fun BaseActivity<*>.connectivityManager() = GrandMarketApp.getInstance().connectivityManager
fun BottomSheetDialogFragment.connectivityManager() = GrandMarketApp.getInstance().connectivityManager

fun BaseFragment<*>.onNetworkStatusChange(viewLifecycleOwner: LifecycleOwner, function: (Boolean) -> Unit) {
    var init = true
    var lastState = true
    connectivityManager().isNetworkAvailable.observe(viewLifecycleOwner) {
        if (lastState != it) {
            if (!init) {
                function.invoke(it)
            } else init = false
            lastState = it
        }
    }
}

fun BaseActivity<*>.onNetworkStatusChange(viewLifecycleOwner: LifecycleOwner, function: (Boolean) -> Unit) {
    var init = true
    var lastState = true
    connectivityManager().isNetworkAvailable.observe(viewLifecycleOwner) {
        if (lastState != it) {
            if (!init) {
                function.invoke(it)
            } else init = false
            lastState = it
        }
    }
}