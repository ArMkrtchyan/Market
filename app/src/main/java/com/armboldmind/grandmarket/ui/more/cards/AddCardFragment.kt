package com.armboldmind.grandmarket.ui.more.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentAddCardBinding
import com.armboldmind.grandmarket.shared.enums.EmptyStatesEnum
import com.armboldmind.grandmarket.shared.mvi.states.ViewState


class AddCardFragment : BaseFragment<FragmentAddCardBinding>() {
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddCardBinding
        get() = FragmentAddCardBinding::inflate


    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentAddCardBinding, keysFromDb: Keys) {
        binding.apply {
            mBinding.loadingView.apply {
                setViewState(ViewState.EmptyState)
                setEmpty(EmptyStatesEnum.CARDS.emptyModel)
            }
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