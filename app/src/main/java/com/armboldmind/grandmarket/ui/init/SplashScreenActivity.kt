package com.armboldmind.grandmarket.ui.init

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseActivity
import com.armboldmind.grandmarket.databinding.ActivitySplashScreenBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.UserRoleEnum
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {
    private val mLanguagesViewModel by lazy { createViewModel(LanguageViewModel::class.java, this) }
    override val inflate: (LayoutInflater) -> ActivitySplashScreenBinding
        get() = ActivitySplashScreenBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLanguagesViewModel.sendIntent(com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent.GetAllKeysFromDB)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                preferencesManager().saveByKey(BundleKeysEnum.DEVICE_TOKEN.key, it.result)
            }
        }
        setStatusBarColor(R.color.black)
        lifecycleScope.launch {
            delay(2500)
            if (preferencesManager().findByKey<UserRoleEnum?>(BundleKeysEnum.USER_ROLE.key) != null) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            } else startActivity(Intent(this@SplashScreenActivity, ChooseLanguageActivity::class.java))
            overridePendingTransition(R.anim.fade_in_500, 0)
            finish()
        }
    }

    override fun setLoading(isLoading: Boolean) {

    }

}