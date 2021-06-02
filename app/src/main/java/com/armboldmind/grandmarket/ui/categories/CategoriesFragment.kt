package com.armboldmind.grandmarket.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.searchProductsModel
import com.armboldmind.grandmarket.databinding.FragmentCategoriesBinding
import com.armboldmind.grandmarket.shared.globalextensions.onNetworkStatusChange
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.categories.adapters.CategoriesAdapter
import kotlinx.coroutines.launch

class CategoriesFragment : BaseFragment<FragmentCategoriesBinding>() {
    private val mCategoriesViewModel by lazy { createViewModel(CategoryViewModel::class.java, this) }
    private val mAdapter by lazy {
        CategoriesAdapter { category ->
            if (category.subCategories.isEmpty()) {
                searchProductsModel { }
                view?.findNavController()
                        ?.navigate(CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(searchProductsModel {
                            categoryIdList = arrayListOf<Long>().apply { add(category.id) }
                            categoryName = category.categoryName
                        }))
            } else {
                view?.findNavController()
                        ?.navigate(CategoriesFragmentDirections.actionCategoriesFragmentToSubCategoriesFragment(category))
            }
        }
    }

    private var isInitialized = false

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCategoriesBinding
        get() = FragmentCategoriesBinding::inflate

    override fun initView(binding: FragmentCategoriesBinding, keysFromDb: Keys) {
        binding.apply {
            onNetworkStatusChange(viewLifecycleOwner) {
                mCategoriesViewModel.sendIntent(ActionIntent.GetCategoriesData)
            }

            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            if (!isInitialized) {
                isInitialized = true
                mCategoriesViewModel.sendIntent(ActionIntent.GetCategoriesData)
                categories.adapter = mAdapter
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
                mBinding.loadingView.setOnButtonClick(mKeys.retry) { mCategoriesViewModel.sendIntent(ActionIntent.GetCategoriesData) }
            }
            is ViewState.FetchCategoriesData -> {
                mAdapter.submitList(viewState.categories)
                mBinding.categories.scrollToPosition(0)
                setLoading(false)
            }
            else -> setLoading(false)
        }
    }
}