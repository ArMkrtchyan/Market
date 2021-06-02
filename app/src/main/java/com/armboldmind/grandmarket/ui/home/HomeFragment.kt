package com.armboldmind.grandmarket.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewpager2.widget.ViewPager2
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Banner
import com.armboldmind.grandmarket.data.models.domain.Category
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.searchProductsModel
import com.armboldmind.grandmarket.databinding.FragmentHomeBinding
import com.armboldmind.grandmarket.shared.enums.BannerActionEnum
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.LabelsEnum
import com.armboldmind.grandmarket.shared.enums.MenuEnum
import com.armboldmind.grandmarket.shared.globalextensions.invisible
import com.armboldmind.grandmarket.shared.globalextensions.onNetworkStatusChange
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.MainActivity
import com.armboldmind.grandmarket.ui.home.adapters.*
import com.armboldmind.grandmarket.ui.productdetails.ProductDetailsActivity
import com.armboldmind.grandmarket.ui.products.ProductsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val mHomeViewModel by lazy { createViewModel(HomeViewModel::class.java, this) }
    private val mProductsViewModel by lazy { createViewModel(ProductsViewModel::class.java, this) }

    private val mBannersAdapter by lazy { BannersAdapter(::onBannerClick) }
    private val mCategoriesAdapter by lazy { CategoriesAdapter(::onCategoryClick) }
    private val mNewArrivalsAdapter by lazy { AdapterHorizontalProducts(this::onProductClick, this::onFavoriteClick) }
    private val mBestSellersAdapter by lazy { BestSellersAdapter (this::onProductClick, this::onFavoriteClick) }
    private val mDiscountsAdapter by lazy { AdapterHorizontalProducts(this::onProductClick, this::onFavoriteClick) }

    private var isInitialized = false
    private var mCurrentActionIntent: ActionIntent = ActionIntent.InitHomeData

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun initView(binding: FragmentHomeBinding, keysFromDb: Keys) {
        binding.apply {

            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            if (!isInitialized) {
                isInitialized = true
                onNetworkStatusChange(viewLifecycleOwner) {
                    mHomeViewModel.sendIntent(ActionIntent.InitHomeData)
                }
                mHomeViewModel.sendIntent(mCurrentActionIntent)
                bannersPager.apply {
                    adapter = mBannersAdapter
                    orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    TabLayoutMediator(tabLayout, this) { _, _ -> }.attach()
                    lifecycleScope.launch {
                        (0..10000).asFlow()
                                .onEach { delay(10000) }
                                .flowOn(Dispatchers.Main)
                                .collect {
                                    if (mBannersAdapter.itemCount > 1) {
                                        withContext(Dispatchers.Main) {
                                            setCurrentItem(if (currentItem < mBannersAdapter.itemCount - 1) currentItem + 1 else 0, true)
                                        }
                                    }
                                }
                    }
                }

                search.setOnClickListener { (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES) }
                categoriesViewAll.setOnClickListener { (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES) }
                newArrivalsAll.setOnClickListener {
                    (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
                    (activity as MainActivity).categoriesNavController()
                            .navigate(R.id.productsFragment, bundleOf(BundleKeysEnum.SEARCH_PRODUCT_MODEL.key to searchProductsModel {
                                categoryName = mKeys.new_arrivals
                                labelType = LabelsEnum.NEW.type
                            }))
                }
                bestsellersAll.setOnClickListener {
                    (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
                    (activity as MainActivity).categoriesNavController()
                            .navigate(R.id.productsFragment, bundleOf(BundleKeysEnum.SEARCH_PRODUCT_MODEL.key to searchProductsModel {
                                categoryName = mKeys.bestsellers
                                labelType = LabelsEnum.BEST_SELL.type
                            }))
                }
                discountsAll.setOnClickListener {
                    (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
                    (activity as MainActivity).categoriesNavController()
                            .navigate(R.id.productsFragment, bundleOf(BundleKeysEnum.SEARCH_PRODUCT_MODEL.key to searchProductsModel {
                                discounted = true
                                categoryName = mKeys.discounts
                            }))
                }
                request.setOnClickListener {
                    (activity as MainActivity).moreNavController()
                            .navigate(R.id.requestProductInfoFragment)
                    (activity as MainActivity).navigateToTab(MenuEnum.MORE)
                }
            }
        }
    }

    private fun setAdapters() {
        mBinding.apply {
            categoriesList.adapter = ConcatAdapter(mCategoriesAdapter, ViewAllAdapter { (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES) })
            newArrivalsList.adapter = ConcatAdapter(mNewArrivalsAdapter, ViewAllAdapter {
                (activity as MainActivity).categoriesNavController()
                        .navigate(R.id.productsFragment, bundleOf(BundleKeysEnum.SEARCH_PRODUCT_MODEL.key to searchProductsModel {
                            categoryName = mKeys.new_arrivals
                            labelType = LabelsEnum.NEW.type
                        }))
                (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
            })
            bestsellersList.adapter = ConcatAdapter(mBestSellersAdapter, ViewAllAdapter {
                (activity as MainActivity).categoriesNavController()
                        .navigate(R.id.productsFragment, bundleOf(BundleKeysEnum.SEARCH_PRODUCT_MODEL.key to searchProductsModel {
                            categoryName = mKeys.bestsellers
                            labelType = LabelsEnum.BEST_SELL.type
                        }))
                (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
            })
            discountsList.adapter = ConcatAdapter(mDiscountsAdapter, ViewAllAdapter {
                (activity as MainActivity).categoriesNavController()
                        .navigate(R.id.productsFragment, bundleOf(BundleKeysEnum.SEARCH_PRODUCT_MODEL.key to searchProductsModel {
                            discounted = true
                            categoryName = mKeys.discounts
                        }))
                (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
            })
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> {
                setLoading(true)
                if (mCurrentActionIntent == ActionIntent.InitHomeData) mBinding.mainContent.invisible()
            }
            is ViewState.ErrorState -> lifecycleScope.launch {
                mBinding.loadingView.setViewState(ViewState.ErrorState(viewState.exception))
                mBinding.loadingView.setOnButtonClick(mKeys.retry) { mHomeViewModel.sendIntent(ActionIntent.InitHomeData) }
            }
            is ViewState.FetchHomePageData -> lifecycleScope.launch {
                mBannersAdapter.submitList(viewState.banners)
                mCategoriesAdapter.submitList(viewState.categories)
                mNewArrivalsAdapter.submitList(viewState.newArrivals)
                mBestSellersAdapter.submitList(viewState.bestSellers)
                mDiscountsAdapter.submitList(viewState.discounts)
                delay(500)
                setAdapters()
                mBinding.apply {
                    bannersPagerCard.isVisible = viewState.banners.isNotEmpty()
                    tabLayout.isVisible = viewState.banners.size > 1
                    categoriesGroup.isVisible = viewState.categories.isNotEmpty()
                    newArrivalsGroup.isVisible = viewState.newArrivals.isNotEmpty()
                    bestsellersGroup.isVisible = viewState.bestSellers.isNotEmpty()
                    discountGroup.isVisible = viewState.discounts.isNotEmpty()
                    mainContent.show()
                }
                setLoading(false)
            }
            else -> setLoading(false)
        }
    }

    private fun onBannerClick(banner: Banner) {
        when (banner.bannerActionEnum) {
            BannerActionEnum.GLOBAL.action -> {
            }
            BannerActionEnum.BLOG.action -> {
            }
            BannerActionEnum.BRAND.action -> {
            }
            BannerActionEnum.CATEGORY.action -> {
                (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
            }
            BannerActionEnum.EVENT.action -> {
            }
            BannerActionEnum.LINK.action -> {
                startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(banner.link)))
            }
            BannerActionEnum.NEWS.action -> {
                (activity as MainActivity).moreNavController()
                        .navigate(R.id.newsDetailsFragment, bundleOf("id" to banner.bannerActionId))
                (activity as MainActivity).navigateToTab(MenuEnum.MORE)
            }
            BannerActionEnum.PRODUCT.action -> {
                (activity as MainActivity).categoriesNavController()
                        .navigate(R.id.productsFragment, bundleOf(BundleKeysEnum.SEARCH_PRODUCT_MODEL.key to searchProductsModel { }))
                (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
            }
        }
    }

    private fun onCategoryClick(category: Category) {
        val bundle = bundleOf("category" to category)
        if (category.subCategories.isEmpty()) (activity as MainActivity).categoriesNavController()
                .navigate(R.id.productsFragment, bundle)
        else (activity as MainActivity).categoriesNavController()
                .navigate(R.id.subCategoriesFragment, bundle)
        (activity as MainActivity).navigateToTab(MenuEnum.CATEGORIES)
    }

    private fun onProductClick(product: Product) {
        ProductDetailsActivity.start(requireActivity(), bundleOf(BundleKeysEnum.PRODUCT.key to product.productId))
    }

    private fun onFavoriteClick(product: Product) {
        mCurrentActionIntent = ActionIntent.FavoriteProductActionIntent(productId = product.productId)
        mProductsViewModel.sendIntent(mCurrentActionIntent)
    }

    override fun onResume() {
        super.onResume()
        mNewArrivalsAdapter.notifyDataSetChanged()
        mDiscountsAdapter.notifyDataSetChanged()
        mBestSellersAdapter.notifyDataSetChanged()
    }
}