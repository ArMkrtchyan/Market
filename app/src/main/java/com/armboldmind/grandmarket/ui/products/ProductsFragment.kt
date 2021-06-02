package com.armboldmind.grandmarket.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentProductsBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.EmptyStatesEnum
import com.armboldmind.grandmarket.shared.globalextensions.gone
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.more.addresses.dialogs.EditAddressDialog
import com.armboldmind.grandmarket.ui.productdetails.ProductDetailsActivity
import kotlinx.coroutines.launch

class ProductsFragment : BaseFragment<FragmentProductsBinding>() {
    private val mProductsViewModel by lazy { createViewModel(ProductsViewModel::class.java, this) }
    private val mAdapter by lazy { ProductsAdapter(this::onProductClick, this::onFavoriteClick) }
    private val mArgs: ProductsFragmentArgs by navArgs()

    private var mFiltersDialog: FiltersBottomSheetDialog? = null

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductsBinding
        get() = FragmentProductsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentProductsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            toolbarTitle.text = mArgs.searchProductModel.categoryName ?: ""
            mBinding.sort.gone()
            mBinding.filters.gone()
            loadingView.setEmpty(EmptyStatesEnum.PRODUCTS.emptyModel)
            mProductsViewModel.sendIntent(ActionIntent.GetProducts(mArgs.searchProductModel))
            products.adapter = mAdapter.withLoadStateFooter(BasePagingAdapter.LoadStateAdapter { mAdapter.retry() })
            mAdapter.addLoadStateListener {
                when (it.refresh) {
                    !is LoadState.Error -> {
                        when {
                            it.prepend.endOfPaginationReached -> {
                                if (mAdapter.itemCount == 0) loadingView.setViewState(ViewState.EmptyState) else setLoading(false)
                                mBinding.sort.show()
                                mBinding.filters.show()
                            }
                            else -> setLoading(true)
                        }
                    }
                    is LoadState.Error -> if (mAdapter.itemCount == 0) lifecycleScope.launch {
                        mBinding.loadingView.setViewState(ViewState.ErrorState((it.refresh as LoadState.Error).error))
                        mBinding.loadingView.setOnButtonClick(mKeys.retry) { mProductsViewModel.sendIntent(ActionIntent.GetProducts(mArgs.searchProductModel)) }
                        mBinding.sort.gone()
                        mBinding.filters.gone()
                    }
                }
            }
            sort.setOnClickListener {

            }
            filters.setOnClickListener {
                showFiltersDialog()
            }
        }
    }

    private fun showFiltersDialog() {
        if (mFiltersDialog == null || mFiltersDialog?.isVisible == false) {
            mFiltersDialog = FiltersBottomSheetDialog(mProductsViewModel, mArgs.searchProductModel)
            mFiltersDialog?.show(childFragmentManager, EditAddressDialog::class.java.name)
            mFiltersDialog?.dialog?.setOnDismissListener { mFiltersDialog = null }
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
                mBinding.loadingView.setOnButtonClick(mKeys.retry) { mProductsViewModel.sendIntent(ActionIntent.GetProducts(mArgs.searchProductModel)) }
                mBinding.sort.gone()
                mBinding.filters.gone()
            }
            is ViewState.FetchProducts -> lifecycleScope.launch {
                mAdapter.submitData(viewState.list)
            }
            is ViewState.FavoriteProduct -> lifecycleScope.launch {
                mAdapter.refresh()
                setLoading(false)
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
        mAdapter.refresh()
    }
}