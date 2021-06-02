package com.armboldmind.grandmarket.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.findNavController
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseActivity
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.databinding.ActivityAuthorizationBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager

class AuthorizationActivity : BaseActivity<ActivityAuthorizationBinding>() {

    override val inflate: (LayoutInflater) -> ActivityAuthorizationBinding
        get() = ActivityAuthorizationBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        updateResources(preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).uniqueSeoCode)
        super.onCreate(savedInstanceState)
        clearLightStatusBar()
    }

    override fun setLoading(isLoading: Boolean) {
    }

    override fun onBackPressed() {
        if (!findNavController(R.id.nav_host_auth_fragment).navigateUp()) {
            finish()
        }
    }
}