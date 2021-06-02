package com.armboldmind.grandmarket.ui.more.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentNotificationsBinding
import com.armboldmind.grandmarket.shared.enums.EmptyStatesEnum
import com.armboldmind.grandmarket.shared.enums.MenuEnum
import com.armboldmind.grandmarket.shared.enums.NotificationActionEnum.*
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.MainActivity
import com.armboldmind.grandmarket.ui.productdetails.ProductDetailsActivity
import kotlinx.coroutines.launch


class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {

    private val mNotificationViewModel: NotificationsViewModel by lazy { (activity as MainActivity).createViewModel(NotificationsViewModel::class.java, this) }
    private val mAdapter by lazy { NotificationsAdapter(this::navigateWithAction) }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNotificationsBinding
        get() = FragmentNotificationsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentNotificationsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            mNotificationViewModel.sendIntent(ActionIntent.GetNotifications)
            loadingView.setEmpty(EmptyStatesEnum.NOTIFICATIONS.emptyModel)
            notifications.adapter = mAdapter.withLoadStateFooter(BasePagingAdapter.LoadStateAdapter { mAdapter.retry() })
            mAdapter.addLoadStateListener {
                when (it.refresh) {
                    !is LoadState.Error -> {
                        when {
                            it.prepend.endOfPaginationReached -> {
                                if (mAdapter.itemCount == 0) loadingView.setViewState(ViewState.EmptyState) else setLoading(false)
                                mNotificationViewModel.sendIntent(ActionIntent.GetUnseenNotifications)
                            }
                            else -> setLoading(true)
                        }
                    }
                    is LoadState.Error -> if (mAdapter.itemCount == 0) lifecycleScope.launch {
                        mBinding.loadingView.setViewState(ViewState.ErrorState((it.refresh as LoadState.Error).error))
                        mBinding.loadingView.setOnButtonClick(mKeys.retry) { mNotificationViewModel.sendIntent(ActionIntent.GetNotifications) }
                    }
                }
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> lifecycleScope.launch {
                mBinding.loadingView.setViewState(ViewState.ErrorState(viewState.exception))
                mBinding.loadingView.setOnButtonClick(mKeys.retry) { mNotificationViewModel.sendIntent(ActionIntent.GetNotifications) }
            }
            is ViewState.FetchNotifications -> lifecycleScope.launch { mAdapter.submitData(viewState.list) }
            else -> setLoading(false)
        }
    }

    private fun navigateWithAction(action: Int, actionId: Long) {
        when (action) {
            NEWS.action -> {
                navigateTo(NotificationsFragmentDirections.actionNotificationsFragmentToNewsDetailsFragment(id = actionId))
            }
            PRODUCT.action -> {
                ProductDetailsActivity.start(requireActivity(), bundleOf())
            }
            ORDERS.action -> {
                navigateTo(NotificationsFragmentDirections.actionNotificationsFragmentToOrderDetailsFragment(id = actionId))
            }
            BASKET.action -> {
                if (activity is MainActivity) (activity as MainActivity).navigateToTab(MenuEnum.BASKET)
            }
            SUBSCRIPTIONS.action -> {
                navigateTo(NotificationsFragmentDirections.actionNotificationsFragmentToSubscriptionDetailsFragment(id = actionId))
            }
            DISCOUNT.action -> {
            }
            GLOBAL.action -> {
            }
            BLOG.action -> {
            }
            CATEGORY.action -> {
                (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
            }
            EVENT.action -> {
                navigateTo(NotificationsFragmentDirections.actionNotificationsFragmentToNewsDetailsFragment(id = actionId))
            }
            BRAND.action -> {
            }
            LINK.action -> { //  startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(pageModel.pageName)))
            }
        }
    }

    private fun navigateTo(action: NavDirections) {
        view?.findNavController()
                ?.navigate(action)
    }

    override fun onResume() {
        super.onResume()
        mAdapter.refresh()
    }
}