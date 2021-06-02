package com.armboldmind.grandmarket.ui.more.informative

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentAboutUsBinding
import com.armboldmind.grandmarket.shared.mvi.states.ViewState

class AboutUsFragment : BaseFragment<FragmentAboutUsBinding>() {
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAboutUsBinding
        get() = FragmentAboutUsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentAboutUsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
        }
    }

    override fun setLoading(isLoading: Boolean) {}
    override fun handleViewState(viewState: ViewState) {}
}