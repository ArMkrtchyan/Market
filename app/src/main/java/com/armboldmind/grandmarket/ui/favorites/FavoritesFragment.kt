package com.armboldmind.grandmarket.ui.favorites

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentFavoritesBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.EmptyStatesEnum
import com.armboldmind.grandmarket.shared.globalextensions.onNetworkStatusChange
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.productdetails.ProductDetailsActivity
import com.armboldmind.grandmarket.ui.products.ProductsAdapter
import com.armboldmind.grandmarket.ui.products.ProductsViewModel
import kotlinx.coroutines.launch

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {
    private val mProductsViewModel by lazy { createViewModel(ProductsViewModel::class.java, this) }
    private val mAdapter by lazy { ProductsAdapter(this::onProductClick, this::onFavoriteClick) }
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFavoritesBinding
        get() = FragmentFavoritesBinding::inflate

    override fun initView(binding: FragmentFavoritesBinding, keysFromDb: Keys) {
        onNetworkStatusChange(viewLifecycleOwner) {
            Log.i("NetworkState", it.toString())
        }
        binding.apply {
            loadingView.setEmpty(EmptyStatesEnum.FAVORITES.emptyModel)
            mProductsViewModel.sendIntent(ActionIntent.GetFavoriteProducts)
            favorites.adapter = mAdapter.withLoadStateFooter(BasePagingAdapter.LoadStateAdapter { mAdapter.retry() })
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
                        mBinding.loadingView.setOnButtonClick(mKeys.retry) { mProductsViewModel.sendIntent(ActionIntent.GetFavoriteProducts) }
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
                mBinding.loadingView.setOnButtonClick(mKeys.retry) { mProductsViewModel.sendIntent(ActionIntent.GetFavoriteProducts) }
            }
            is ViewState.FetchProducts -> lifecycleScope.launch { mAdapter.submitData(viewState.list) }
            is ViewState.FavoriteProduct -> lifecycleScope.launch {
                mProductsViewModel.sendIntent(ActionIntent.GetFavoriteProducts)
            }
            else -> setLoading(false)
        }
    }

    private fun onProductClick(product: Product) {
        ProductDetailsActivity.start(requireActivity(), bundleOf(BundleKeysEnum.PRODUCT.key to product.productId))
    }

    private fun onFavoriteClick(product: Product) {
        mProductsViewModel.sendIntent(ActionIntent.FavoriteProductActionIntent(productId = product.productId))
    }

    override fun onResume() {
        super.onResume()
        mProductsViewModel.sendIntent(ActionIntent.GetFavoriteProducts)
    }
}