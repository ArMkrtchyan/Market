package com.armboldmind.grandmarket.ui.productdetails

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewpager2.widget.ViewPager2
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseActivity
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.domain.ProductDetails
import com.armboldmind.grandmarket.data.models.requestmodels.productDetailsRequestModel
import com.armboldmind.grandmarket.databinding.ActivityProductDetailsBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.globalextensions.*
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.home.adapters.AdapterHorizontalProducts
import com.armboldmind.grandmarket.ui.productdetails.adapters.AdapterProductImages
import com.armboldmind.grandmarket.ui.productdetails.adapters.AttributesAdapter
import com.armboldmind.grandmarket.ui.productdetails.adapters.CharacteristicsAdapter
import com.armboldmind.grandmarket.ui.products.ProductsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ProductDetailsActivity : BaseActivity<ActivityProductDetailsBinding>() {

    private val mProductsViewModel by lazy { createViewModel(ProductsViewModel::class.java, this) }
    private val mProductId: Long? by lazy {
        intent?.getBundleExtra(BundleKeysEnum.PRODUCT.key)
                ?.getLong(BundleKeysEnum.PRODUCT.key)
    }
    private val mProductDetailsRequestModel = productDetailsRequestModel { }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val mProductImagesAdapter by lazy { AdapterProductImages() }
    private val mAttributesConcatAdapter by lazy { ConcatAdapter() }
    private val mAttributesAdapter by lazy {
        AttributesAdapter { id, list ->
            mProductDetailsRequestModel.apply {
                productAttributeIds?.let {
                    if (!it.contains(id)) {
                        it.removeAll(list)
                        it.add(id)
                        mProductsViewModel.sendIntent(ActionIntent.GetProductDetails(mProductDetailsRequestModel))
                    }
                } ?: run {
                    productAttributeIds = arrayListOf<Long>().apply {
                        add(id)
                        mProductsViewModel.sendIntent(ActionIntent.GetProductDetails(mProductDetailsRequestModel))
                    }
                }
            }
        }
    }
    private val mCharacteristicsAdapter by lazy { CharacteristicsAdapter() }
    private val mRelatedListAdapter by lazy { AdapterHorizontalProducts(this::onProductClick, this::onFavoriteClick) }
    private var mCount = 1
    private var mProductDetails: ProductDetails? = null

    companion object {
        fun start(activity: Activity, bundle: Bundle) {
            activity.startActivity(Intent(activity, ProductDetailsActivity::class.java).apply { putExtra(BundleKeysEnum.PRODUCT.key, bundle) })
            activity.overridePendingTransition(R.anim.fade_in_500, R.anim.fade_out_500)
        }
    }

    override val inflate: (LayoutInflater) -> ActivityProductDetailsBinding
        get() = ActivityProductDetailsBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar()
        setStatusBarColor(R.color.transparent)
        withFullScreen()
        setSupportActionBar(mBinding.toolbar)
        initModalBottomSheet()
        mProductId?.let { mProductsViewModel.sendIntent(ActionIntent.GetProductDetails(mProductDetailsRequestModel.apply { productId = it })) }
        mBinding.apply {
            oldPrice.paintFlags = oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            attributes.adapter = mAttributesConcatAdapter
            minus.setOnClickListener { count.text = "${if (mCount > 1) --mCount else mCount}" }
            plus.setOnClickListener { count.text = "${++mCount}" }
            mAttributesConcatAdapter.addAdapter(mAttributesAdapter)
            mAttributesConcatAdapter.addAdapter(mCharacteristicsAdapter)
            relatedList.adapter = mRelatedListAdapter
            ProductsViewModel.productLiveData.observe(this@ProductDetailsActivity) {
                Log.i("FavoriteTag", "ProductDetailsActivity: productId ->${mProductDetails?.productId}, isFavorite-> ${it[mProductDetails?.productId]}")
                isfavorite = it[mProductDetails?.productId] ?: false
            }
            imagesPager.adapter = mProductImagesAdapter
            imagesPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            TabLayoutMediator(tabLayout, imagesPager) { tab, position -> }.attach()
            keysLiveData().observe(this@ProductDetailsActivity) {
                keys = it
            }
        }
    }

    private fun initModalBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet)
                .apply {
                    peekHeight = getDisplayHeight() * 55 / 100
                    onSlide { alpha -> mBinding.imagesLayout.alpha = alpha }
                    onState { state ->
                        Log.i("StateTag", "state ->${state == BottomSheetBehavior.STATE_EXPANDED}")
                    }
                }
        mBinding.imagesLayout.updateLayoutParams<CoordinatorLayout.LayoutParams> { height = getDisplayHeight() * 47 / 100 + 60 }
        mBinding.bottomSheet.updateLayoutParams<CoordinatorLayout.LayoutParams> { height = getDisplayHeight() - 81.0.dpToPx() }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> lifecycleScope.launch {
                showError(message = viewState.exception) { mProductsViewModel.sendIntent(ActionIntent.GetProductDetails(mProductDetailsRequestModel)) }
            }
            is ViewState.FetchProductDetails -> lifecycleScope.launch {
                mProductDetails = viewState.productDetails
                mBinding.productDetails = viewState.productDetails
                mAttributesAdapter.submitList(viewState.productDetails.combinationAttributeGroups)
                mCharacteristicsAdapter.submitList(viewState.productDetails.characteristicGroupModels)
                mRelatedListAdapter.submitList(viewState.similarProducts)
                mProductImagesAdapter.submitList(viewState.productDetails.productMedias)
                mBinding.isfavorite = viewState.productDetails.favorite
                mBinding.favorite.setOnClickListener { mProductsViewModel.sendIntent(ActionIntent.FavoriteProductActionIntent(productId = viewState.productDetails.productId)) }
                setLoading(false)
                showContent()
                if (viewState.similarProducts.isNotEmpty()) {
                    mBinding.relatedTitle.show()
                    mBinding.relatedList.show()
                }
                mBinding.tabLayout.isVisible = viewState.productDetails.productMedias.size > 1
            }
            is ViewState.UpdateAttributesByIds -> lifecycleScope.launch {
                Log.i("AttributeIdsTag", viewState.attributeIds.toString())
                setLoading(false)
            }
            else -> setLoading(false)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.fade_in_500, R.anim.fade_out_500)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun onProductClick(product: Product) {
        start(this, bundleOf(BundleKeysEnum.PRODUCT.key to product.id))
    }

    private fun onFavoriteClick(product: Product) {
        mProductsViewModel.sendIntent(ActionIntent.FavoriteProductActionIntent(productId = product.productId))
    }

    private fun showContent() {
        mBinding.apply {
            imagesLayout.show()
            bottomSheet.show()
            favorite.show()
            share.show()
            buttonsLayout.show()
        }
    }
}