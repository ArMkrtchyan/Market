package com.armboldmind.grandmarket.shared.globalextensions

import com.armboldmind.grandmarket.GrandMarketApp
import com.armboldmind.grandmarket.base.BaseActivity
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.di.components.AppComponent

fun BaseViewModel.appComponent(): AppComponent = GrandMarketApp.getInstance().mAppComponent
fun BaseActivity<*>.appComponent(): AppComponent = GrandMarketApp.getInstance().mAppComponent
fun BaseFragment<*>.appComponent(): AppComponent = GrandMarketApp.getInstance().mAppComponent