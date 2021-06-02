package com.armboldmind.grandmarket.ui.products

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.armboldmind.grandmarket.base.BaseBottomSheetDialogFragment
import com.armboldmind.grandmarket.data.models.requestmodels.SearchProductsModel
import com.armboldmind.grandmarket.data.models.requestmodels.createNewFrom
import com.armboldmind.grandmarket.data.models.requestmodels.searchProductsModel
import com.armboldmind.grandmarket.databinding.BottomSheetProductFiltersBinding
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.products.adapters.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class FiltersBottomSheetDialog(private val mProductsViewModel: ProductsViewModel, private val searchProductModel: SearchProductsModel) : BaseBottomSheetDialogFragment() {
    private val mViewModel by lazy {
        ViewModelProvider(this).get(ProductsViewModel::class.java)
                .apply { setStateHandler(::handleViewState) }
    }
    private val mCurrentSearchModel = searchProductsModel { }
    private val mConcatAdapter by lazy { ConcatAdapter() }
    private lateinit var mBinding: BottomSheetProductFiltersBinding
    private var isRequested = false
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme).apply {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return BottomSheetProductFiltersBinding.inflate(inflater, container, false)
                .apply {
                    mBinding = this
                    Log.i("FiltersTag", "${mProductsViewModel.mFiltersLiveData.value}")
                    keysLiveData().observe(viewLifecycleOwner) {
                        mProductsViewModel.mFiltersLiveData.value?.let { filters ->
                            Log.i("FiltersTag", "$filters")
                            Log.i("FiltersTag", "$searchProductModel")
                            lifecycleOwner = viewLifecycleOwner
                            keys = it
                            mCurrentSearchModel.apply {
                                priceFrom = searchProductModel.priceFrom
                                priceTo = searchProductModel.priceTo
                                searchProductModel.brandIds?.let { brand ->
                                    brand.map {
                                        brandIds?.let { brands -> brands.add(it) } ?: run {
                                            brandIds = arrayListOf<Long>().apply { add(it) }
                                        }
                                    }
                                }
                                searchProductModel.searchAttributes?.let { brand ->
                                    brand.map {
                                        searchAttributes?.let { brands -> brands.add(it) } ?: run {
                                            searchAttributes = arrayListOf<SearchProductsModel.SearchAttributes>().apply {
                                                add(SearchProductsModel.SearchAttributes()
                                                            .apply {
                                                                attributeGroupId = it.attributeGroupId
                                                                attributeIds = it.attributeIds
                                                            })
                                            }
                                        }
                                    }
                                }
                                searchProductModel.searchCharacteristic?.let { brand ->
                                    brand.map {
                                        searchCharacteristic?.let { brands -> brands.add(it) } ?: run {
                                            searchCharacteristic = arrayListOf<SearchProductsModel.SearchCharacteristic>().apply {
                                                add(SearchProductsModel.SearchCharacteristic()
                                                            .apply {
                                                                characteristicGroupId = it.characteristicGroupId
                                                                characteristicIds = it.characteristicIds
                                                            })
                                            }
                                        }
                                    }
                                }
                                text = searchProductModel.text
                                categoryIdList = searchProductModel.categoryIdList
                                collectionId = searchProductModel.collectionId
                                discounted = searchProductModel.discounted
                                categoryName = searchProductModel.categoryName

                            }
                            viewItems.setText("${it.view_items}")
                            filter.adapter = mConcatAdapter.apply {
                                if (filters.priceFrom < filters.priceTo) addAdapter(PriceRangeAdapter(
                                    filters = filters,
                                    searchProductModel = mCurrentSearchModel,
                                    search = ::search,
                                ))
                            }
                            if (filters.brandModels.size > 1) mConcatAdapter.addAdapter(BrandsGroupAdapter(
                                brandModels = filters.brandModels,
                                isFirstPosition = filters.priceFrom >= filters.priceTo,
                                searchProductModel = mCurrentSearchModel,
                                search = ::search,
                            ))
                            filters.filterPreviews.map {
                                val searchAttributes = SearchProductsModel.SearchAttributes()
                                searchAttributes.attributeGroupId = it.attributeGroupId
                                searchAttributes.attributeIds = arrayListOf()
                                mCurrentSearchModel.searchAttributes?.let { att -> att.add(searchAttributes) } ?: run {
                                    mCurrentSearchModel.searchAttributes = arrayListOf<SearchProductsModel.SearchAttributes>().apply {
                                        add(searchAttributes)
                                    }
                                }
                                if (it.colorPicker) {
                                    if (it.attributeModelList.size > 1) mConcatAdapter.addAdapter(ColorsGroupAdapter(
                                        filterPreviews = it,
                                        isFirstPosition = filters.priceFrom >= filters.priceTo,
                                        attributeIds = searchAttributes.attributeIds ?: arrayListOf(),
                                        search = ::search,
                                    ))
                                } else {
                                    if (it.attributeModelList.size > 1) mConcatAdapter.addAdapter(SizesGroupAdapter(
                                        filterPreviews = it,
                                        isFirstPosition = filters.priceFrom >= filters.priceTo,
                                        attributeIds = searchAttributes.attributeIds ?: arrayListOf(),
                                        search = ::search,
                                    ))
                                }
                            }
                            filters.characteristicGroupFilter.map {
                                val searchCharacteristic = SearchProductsModel.SearchCharacteristic()
                                searchCharacteristic.characteristicGroupId = it.characteristicGroupId
                                searchCharacteristic.characteristicIds = arrayListOf()
                                mCurrentSearchModel.searchCharacteristic?.let { att -> att.add(searchCharacteristic) } ?: run {
                                    mCurrentSearchModel.searchCharacteristic = arrayListOf<SearchProductsModel.SearchCharacteristic>().apply {
                                        add(searchCharacteristic)
                                    }
                                }
                                if (it.characteristicFilter.size > 1) mConcatAdapter.addAdapter(CharacteristicsGroupAdapter(
                                    characteristicGroupFilter = it,
                                    isFirstPosition = filters.priceFrom >= filters.priceTo,
                                    characteristicIds = searchCharacteristic.characteristicIds ?: arrayListOf(),
                                    search = ::search,
                                ))
                            }

                            reset.setOnClickListener {
                                mConcatAdapter.adapters.map { adapter ->
                                    if (adapter is Reset) adapter.reset()
                                    search(true)
                                }
                            }
                            viewItems.setOnClickListener {
                                mProductsViewModel.sendIntent(ActionIntent.GetProducts(mCurrentSearchModel.createNewFrom()))
                                dismiss()
                            }
                        }
                    }
                }.root
    }

    private fun search(isReset: Boolean = false) {
        if (!isRequested) mViewModel.sendIntent(ActionIntent.GetProductsCount(if (isReset) searchProductsModel {
            discounted = mCurrentSearchModel.discounted
            categoryIdList = mCurrentSearchModel.categoryIdList
            collectionId = mCurrentSearchModel.collectionId
            text = mCurrentSearchModel.text
            also {
                searchProductModel.discounted = it.discounted
                searchProductModel.categoryIdList = it.categoryIdList
                searchProductModel.collectionId = it.collectionId
                searchProductModel.text = it.text
                searchProductModel.searchAttributes = null
                searchProductModel.searchCharacteristic = null
                searchProductModel.priceFrom = null
                searchProductModel.priceTo = null
            }
        } else mCurrentSearchModel.createNewFrom()
                .apply {
                    searchProductModel.discounted = discounted
                    searchProductModel.categoryIdList = categoryIdList
                    searchProductModel.collectionId = collectionId
                    searchProductModel.text = text
                    searchProductModel.searchAttributes = searchAttributes
                    searchProductModel.searchCharacteristic = searchCharacteristic
                    searchProductModel.priceFrom = priceFrom
                    searchProductModel.priceTo = priceTo
                }))
        isRequested = true
    }

    private fun setLoading(isLoading: Boolean) {
        isRequested = false
        mBinding.viewItems.setIsLoading(isLoading)
        mBinding.filter.isClickable = isLoading
        mBinding.filter.isFocusable = isLoading
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> lifecycleScope.launch {
                isRequested = false
                setLoading(false)
            }
            is ViewState.FetchProductsCount -> lifecycleScope.launch {
                isRequested = false
                mBinding.viewItems.setText("${keysLiveData().value?.view_items ?: "view_items"} ${viewState.count}")
            }
            else -> setLoading(false)
        }
    }
}