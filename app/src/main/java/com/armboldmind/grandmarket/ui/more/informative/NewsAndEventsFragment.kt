package com.armboldmind.grandmarket.ui.more.informative

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentNewsAndEventsBinding
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.MainActivity
import kotlinx.coroutines.launch


class NewsAndEventsFragment : BaseFragment<FragmentNewsAndEventsBinding>() {
    private val mInfoViewModel by lazy { createViewModel(InfoViewModel::class.java, this) }
    private val mAdapter by lazy {
        NewsAndEventsAdapter { navigator, extras ->
            if (!MainActivity.isAnimated) view?.findNavController()
                    ?.navigate(NewsAndEventsFragmentDirections.actionNewsAndEventsFragmentToNewsDetailsFragment(navigator), extras)
        }
    }
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsAndEventsBinding
        get() = FragmentNewsAndEventsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentNewsAndEventsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            newsAndEvents.adapter = mAdapter.withLoadStateHeader(BasePagingAdapter.LoadStateAdapter {
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
                        mBinding.loadingView.setOnButtonClick(mKeys.retry) { mInfoViewModel.sendIntent(ActionIntent.FetchNewsAndEventsData) }
                    }
                }
            }
            mInfoViewModel.sendIntent(ActionIntent.FetchNewsAndEventsData)
            initAnimation()
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
                mBinding.loadingView.setOnButtonClick(mKeys.retry) { mInfoViewModel.sendIntent(ActionIntent.FetchNewsAndEventsData) }
            }
            is ViewState.FetchNewsData -> lifecycleScope.launch { setLoading(false); mAdapter.submitData(viewState.news) }
            else -> Unit
        }
    }

}