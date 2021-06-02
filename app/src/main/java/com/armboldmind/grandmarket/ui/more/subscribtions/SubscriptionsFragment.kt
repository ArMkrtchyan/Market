package com.armboldmind.grandmarket.ui.more.subscribtions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.PagingData
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Subscription
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentSubscriptionsBinding
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.launch

class SubscriptionsFragment : BaseFragment<FragmentSubscriptionsBinding>() {
    private val mAdapter by lazy {
        SubscriptionAdapter {
            view?.findNavController()
                ?.navigate(SubscriptionsFragmentDirections.actionSubscriptionsFragmentToSubscriptionDetailsFragment())
        }
    }
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSubscriptionsBinding
        get() = FragmentSubscriptionsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentSubscriptionsBinding, keysFromDb: Keys) {
        binding.apply {
            subscriptions.adapter = mAdapter.apply {
                lifecycleScope.launch {
                    submitData(PagingData.from(arrayListOf<Subscription>().apply {
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
                        add(Subscription(0))
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