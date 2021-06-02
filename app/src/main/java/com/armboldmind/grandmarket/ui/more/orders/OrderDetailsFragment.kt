package com.armboldmind.grandmarket.ui.more.orders

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentOrderDetailsBinding
import com.armboldmind.grandmarket.shared.customview.DialogFactoryCancelOrder
import com.armboldmind.grandmarket.shared.customview.DialogFactoryRate
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.launch

class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding>() {

    private val mOrderHeaderAdapter by lazy { OrdersDetailsHeaderAdapter() }
    private val mOrderFooterAdapter by lazy {
        OrdersDetailsFooterAdapter {
            DialogFactoryCancelOrder.Builder(requireContext())
                .positiveButtonClick {

                }
                .build()
        }
    }
    private val mOrderProductsAdapter by lazy {
        OrdersDetailsProductAdapter(onItemClick = {}, onRateClick = {
            DialogFactoryRate.Builder(requireContext())
                .title(getString(R.string.rate_the_product))
                .negativeButtonText(getString(R.string.cancel))
                .positiveButtonText(getString(R.string.confirm))
                .positiveButtonClick {

                }
                .build()
        })
    }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderDetailsBinding
        get() = FragmentOrderDetailsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentOrderDetailsBinding, keysFromDb: Keys) {
        binding.apply {
            rate.paintFlags = rate.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            rate.setOnClickListener {
                DialogFactoryRate.Builder(requireContext())
                    .title(getString(R.string.rate_our_service))
                    .negativeButtonText(getString(R.string.cancel))
                    .positiveButtonText(getString(R.string.confirm))
                    .positiveButtonClick {

                    }
                    .build()
            }
            orderDetails.adapter = ConcatAdapter(mOrderHeaderAdapter, mOrderProductsAdapter, mOrderFooterAdapter)
        }
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