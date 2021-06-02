package com.armboldmind.grandmarket.ui.more.informative

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.armboldmind.grandmarket.BuildConfig
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentAboutAppBinding
import com.armboldmind.grandmarket.shared.globalextensions.applicationContext
import com.armboldmind.grandmarket.shared.mvi.states.ViewState

class AboutAppFragment : BaseFragment<FragmentAboutAppBinding>() {
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAboutAppBinding
        get() = FragmentAboutAppBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentAboutAppBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            appVersion = BuildConfig.VERSION_NAME
            rateApp.setOnClickListener {
                try {
                    val uri: Uri = Uri.parse("market://details?id=${applicationContext().packageName}")
                    startActivity(Intent(Intent.ACTION_VIEW, uri).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    })
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$${applicationContext().packageName}")))
                }
            }
            findUs.setOnClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1949960381919416")))
                } catch (e: Exception) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/armboldmind")))
                }
            }
            layoutAbm.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.armboldmind.com"))) }
            logo.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.armboldmind.com"))) }
        }
    }

    override fun setLoading(isLoading: Boolean) {

    }

    override fun handleViewState(viewState: ViewState) {
    }


}