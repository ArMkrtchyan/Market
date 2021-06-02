package com.armboldmind.grandmarket.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentProductDetailsBinding
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.launch

class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding>() {

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductDetailsBinding
        get() = FragmentProductDetailsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentProductDetailsBinding, keysFromDb: Keys) {
        binding.apply { }
    }

    override fun setLoading(isLoading: Boolean) {

    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> lifecycleScope.launch { setLoading(false);showError(message = viewState.exception) { } }
            else -> Unit
        }
    }

}