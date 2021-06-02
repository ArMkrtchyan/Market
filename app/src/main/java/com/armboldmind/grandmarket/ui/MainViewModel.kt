package com.armboldmind.grandmarket.ui

import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.shared.managers.PreferencesManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import javax.inject.Inject

class MainViewModel : BaseViewModel() {
    @Inject
    lateinit var mPreferencesManager: PreferencesManager
    override fun sendIntent(actionIntent: ActionIntent) {

    }
}