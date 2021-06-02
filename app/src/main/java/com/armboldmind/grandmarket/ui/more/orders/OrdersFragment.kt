package com.armboldmind.grandmarket.ui.more.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.PagingData
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Order
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentOrdersBinding
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.launch

class OrdersFragment : BaseFragment<FragmentOrdersBinding>() {
    private val mAdapter by lazy {
        OrdersAdapter {
            view?.findNavController()
                ?.navigate(OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment())
        }
    }
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrdersBinding
        get() = FragmentOrdersBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentOrdersBinding, keysFromDb: Keys) {
        binding.apply {
            orders.adapter = mAdapter.apply {
                lifecycleScope.launch {
                    submitData(PagingData.from(arrayListOf<Order>().apply {
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                        add(Order(0))
                    }))
                }
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