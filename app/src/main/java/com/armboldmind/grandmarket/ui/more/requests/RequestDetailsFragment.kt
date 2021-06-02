package com.armboldmind.grandmarket.ui.more.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentRequestDetailsBinding
import com.armboldmind.grandmarket.shared.globalextensions.invisible
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.more.requests.adapters.RequestProductImagesAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class RequestDetailsFragment : BaseFragment<FragmentRequestDetailsBinding>() {

    private val mRequestProductViewModel by lazy { createViewModel(RequestProductViewModel::class.java, this) }
    private val mAdapter by lazy { RequestProductImagesAdapter() }
    private val mArgs: RequestDetailsFragmentArgs by navArgs()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRequestDetailsBinding
        get() = FragmentRequestDetailsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentRequestDetailsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            mRequestProductViewModel.sendIntent(ActionIntent.GetRequestById(mArgs.request.id))
            images.apply {
                adapter = mAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                TabLayoutMediator(tabLayout, images) { _, _ -> }.attach()
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
            is ViewState.FetchRequestById -> lifecycleScope.launch {
                mBinding.apply {
                    setLoading(false)
                    content.show()
                    request = viewState.list
                    mAdapter.submitList(viewState.list.attachedPictures)
                    if (viewState.list.attachedPictures.size > 1) tabLayout.show() else tabLayout.invisible()
                }
            }
            else -> setLoading(false)
        }
    }
}