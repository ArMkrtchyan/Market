package com.armboldmind.grandmarket.ui.products

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.IProductsRepository
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.requestmodels.ProductDetailsRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.SearchProductsModel
import com.armboldmind.grandmarket.data.models.requestmodels.productDetailsRequestModel
import com.armboldmind.grandmarket.shared.globalextensions.appComponent
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsViewModel : BaseViewModel() {
    companion object {
        val favoritesMap = HashMap<Long, Boolean>()
        val productLiveData = MutableLiveData(favoritesMap)
    }

    @Inject
    lateinit var mProductsRepository: IProductsRepository

    private val filtersLiveData by lazy { MutableLiveData<Filters>() }
    val mFiltersLiveData: LiveData<Filters>
        get() = filtersLiveData

    init {
        appComponent().productComponent.build()
                .inject(this)
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.i("ExceptionTAg", throwable.localizedMessage ?: "")
            setState.invoke(ViewState.ErrorState(throwable))
        }) {
            when (actionIntent) {
                is ActionIntent.GetProducts -> {
                    getProducts(actionIntent.searchProductsModel)
                }
                is ActionIntent.GetProductsCount -> getProductsCount(actionIntent.searchProductsModel)
                is ActionIntent.GetFavoriteProducts -> getFavoriteProducts()
                is ActionIntent.GetAttributeIds -> getAttributeIds(actionIntent.productAttributeIds)
                is ActionIntent.FavoriteProductActionIntent -> favoriteProduct(actionIntent.productId)
                is ActionIntent.GetProductDetails -> getProductDetails(actionIntent.productDetailsRequestModel)
                else -> Unit
            }
        }
    }

    private suspend fun getProducts(searchProductsModel: SearchProductsModel) {
        mProductsRepository.getProducts(searchProductsModel)
                .zip(mProductsRepository.getProductFilters(com.armboldmind.grandmarket.data.models.requestmodels.searchProductsModel {
                    discounted = searchProductsModel.discounted
                    categoryIdList = searchProductsModel.categoryIdList
                    collectionId = searchProductsModel.collectionId
                    text = searchProductsModel.text
                })) { products, filters ->
                    setState.invoke(ViewState.FetchProducts(products))
                    filtersLiveData.postValue(filters)
                }
                .collect {

                }
    }

    private suspend fun getProductsCount(searchProductsModel: SearchProductsModel) {
        mProductsRepository.getProductsCount(searchProductsModel)
                .collect {
                    setState.invoke(ViewState.FetchProductsCount(it))
                }
    }


    private suspend fun getFavoriteProducts() {
        mProductsRepository.getFavoriteProducts()
                .onStart {
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
                }
                .collect {
                    setState.invoke(ViewState.FetchProducts(it))
                }
    }

    private suspend fun getProductDetails(requestModel: ProductDetailsRequestModel) {
        mProductsRepository.getProductDetails(requestModel)
                .zip(mProductsRepository.getSimilarProducts(productDetailsRequestModel { this.excludeId = requestModel.productId })) { details, simalProducts ->
                    setState.invoke(ViewState.FetchProductDetails(details, simalProducts))
                }
                .onStart { sendLoadingState() }
                .flowOn(Dispatchers.Main)
                .collect { }
    }

    private suspend fun getAttributeIds(productAttributeIds: List<Long>) {
        mProductsRepository.getAttributeIds(productAttributeIds)
                .onStart { sendLoadingState() }
                .flowOn(Dispatchers.Main)
                .collect {
                    setState.invoke(ViewState.UpdateAttributesByIds(it))
                }
    }

    private suspend fun favoriteProduct(productId: Long) {
        mProductsRepository.favoriteProduct(productId)
                .onStart { sendLoadingState() }
                .flowOn(Dispatchers.Main)
                .collectLatest {
                    Log.i("FavoriteTag", "ProductsViewModel: productId -> $productId")
                    if (favoritesMap.containsKey(productId)) {
                        favoritesMap[productId] = !favoritesMap[productId]!!
                        productLiveData.postValue(favoritesMap)
                    }
                    setState.invoke(ViewState.FavoriteProduct)
                }
    }
}