package com.armboldmind.grandmarket.ui.more.requests

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.IBrandsRepository
import com.armboldmind.grandmarket.data.ICategoriesRepository
import com.armboldmind.grandmarket.data.IRequestProductRepository
import com.armboldmind.grandmarket.data.models.requestmodels.brandsRequestModel
import com.armboldmind.grandmarket.shared.globalextensions.appComponent
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class RequestProductViewModel : BaseViewModel() {
    @Inject
    lateinit var mRequestProductRepositoryRemote: IRequestProductRepository

    @Inject
    lateinit var mCategoriesRepository: ICategoriesRepository

    @Inject
    lateinit var mBrandsRepositoryRemote: IBrandsRepository

    init {
        appComponent().requestsProductComponent.build()
            .inject(this)
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.i("ExceptionTAg", throwable.localizedMessage ?: "")
            setState.invoke(ViewState.ErrorState(throwable))
        }) {
            when (actionIntent) {
                is ActionIntent.GetRequestedProductsActionIntent -> getRequestedProducts()
                is ActionIntent.GetRequestById -> getRequestedProductById(actionIntent.id)
                is ActionIntent.GetCategoriesWithBrands -> getCategoriesWithBrands()
                is ActionIntent.CreateRequestForProduct -> createRequestForProduct(actionIntent.contactName,
                                                                                   actionIntent.contactInformation,
                                                                                   actionIntent.productName,
                                                                                   actionIntent.description,
                                                                                   actionIntent.categoryId,
                                                                                   actionIntent.brandId,
                                                                                   actionIntent.files)
                else -> Unit
            }
        }
    }

    private suspend fun getRequestedProducts() {
        mRequestProductRepositoryRemote.getRequests()
            .onStart {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
            }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                setState.invoke(ViewState.FetchRequests(it))
            }
    }

    private suspend fun getRequestedProductById(id: Long) {
        mRequestProductRepositoryRemote.getRequestById(id)
            .onStart {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
            }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                setState.invoke(ViewState.FetchRequestById(it))
            }
    }

    private suspend fun createRequestForProduct(
        contactName: String,
        contactInformation: String,
        productName: String,
        description: String? = null,
        categoryId: Long,
        brandId: Long,
        files: List<MultipartBody.Part?>?,
    ) {
        mRequestProductRepositoryRemote.createRequestForProduct(contactName, contactInformation, productName, description, categoryId, brandId, files)
            .onStart {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
            }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                setState.invoke(ViewState.RequestForProductCreated)
            }
    }

    private suspend fun getCategoriesWithBrands() {
        val categoriesResponse = mCategoriesRepository.getAllCategories()
        val brandsResponse = mBrandsRepositoryRemote.getBrandsList(brandsRequestModel { })
        categoriesResponse.zip(brandsResponse) { categories, brands ->
            setState.invoke(ViewState.FetchCategoriesWithBrands(brands, categories))
        }
            .onStart {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
            }
            .collect {

            }
    }
}