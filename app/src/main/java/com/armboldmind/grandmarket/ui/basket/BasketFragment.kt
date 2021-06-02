package com.armboldmind.grandmarket.ui.basket

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentBasketBinding
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.productdetails.ProductDetailsActivity

class BasketFragment : BaseFragment<FragmentBasketBinding>() {
    private val mAdapter by lazy {
        BasketAdapter {
            ProductDetailsActivity.start(requireActivity(), bundleOf())
        }
    }
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBasketBinding
        get() = FragmentBasketBinding::inflate

    override fun initView(binding: FragmentBasketBinding, keysFromDb: Keys) {
        binding.apply {
            deleteBasket.paintFlags = deleteBasket.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            basket.adapter = mAdapter
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> {
                setLoading(true)
            }
            is ViewState.ErrorState -> {
                showError(message = viewState.exception) { }
            }
            is ViewState.SuccessState -> {
                setLoading(false)
            }
            else -> {
            }
        }
    }
}