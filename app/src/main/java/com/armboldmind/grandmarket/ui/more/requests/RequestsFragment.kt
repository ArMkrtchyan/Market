package com.armboldmind.grandmarket.ui.more.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.data.models.domain.RequestProduct
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentRequestsBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.EmptyStatesEnum
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.more.requests.adapters.RequestsAdapter
import kotlinx.coroutines.launch

class RequestsFragment : BaseFragment<FragmentRequestsBinding>() {

    private val mRequestProductViewModel by lazy { createViewModel(RequestProductViewModel::class.java, this) }
    private val mAdapter by lazy { RequestsAdapter(this::navigateToRequestDetailsPage) }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRequestsBinding
        get() = FragmentRequestsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentRequestsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            mRequestProductViewModel.sendIntent(ActionIntent.GetRequestedProductsActionIntent)
            loadingView.setEmpty(EmptyStatesEnum.REQUESTS.emptyModel)
            requests.adapter = mAdapter.withLoadStateFooter(BasePagingAdapter.LoadStateAdapter {
                mAdapter.retry()
            })
            mAdapter.addLoadStateListener {
                when (it.refresh) {
                    !is LoadState.Error -> {
                        when {
                            it.prepend.endOfPaginationReached -> {
                                if (mAdapter.itemCount == 0) loadingView.setViewState(ViewState.EmptyState) else setLoading(false)
                            }
                            else -> setLoading(true)
                        }
                    }
                    is LoadState.Error -> if (mAdapter.itemCount == 0) lifecycleScope.launch {
                        mBinding.loadingView.setViewState(ViewState.ErrorState((it.refresh as LoadState.Error).error))
                        mBinding.loadingView.setOnButtonClick(mKeys.retry) { mRequestProductViewModel.sendIntent(ActionIntent.GetRequestedProductsActionIntent) }
                    }
                }
            }
            add.setOnClickListener {
                view?.findNavController()
                    ?.navigate(RequestsFragmentDirections.actionRequestsFragmentToRequestAProductFragment())
            }
            setFragmentResultListener(BundleKeysEnum.ADD_REQUEST_RESULT_KEY.key) { key, bundle ->
                if (key == BundleKeysEnum.ADD_REQUEST_RESULT_KEY.key && bundle.containsKey(BundleKeysEnum.IS_REQUEST_ADDED.key) && bundle.getBoolean(BundleKeysEnum.IS_REQUEST_ADDED.key)) {
                    mRequestProductViewModel.sendIntent(ActionIntent.GetRequestedProductsActionIntent)
                    requests.smoothScrollToPosition(0)
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
                mBinding.loadingView.setOnButtonClick(mKeys.retry) { mRequestProductViewModel.sendIntent(ActionIntent.GetRequestedProductsActionIntent) }
            }
            is ViewState.FetchRequests -> lifecycleScope.launch { mAdapter.submitData(viewState.list) }
            else -> setLoading(false)
        }
    }

    private fun navigateToRequestDetailsPage(requestProduct: RequestProduct) {
        view?.findNavController()
            ?.navigate(RequestsFragmentDirections.actionRequestsFragmentToRequestDetailsFragment(requestProduct))
    }
}