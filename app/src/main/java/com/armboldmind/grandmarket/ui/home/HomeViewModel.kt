package com.armboldmind.grandmarket.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.IBannersRepository
import com.armboldmind.grandmarket.data.ICategoriesRepository
import com.armboldmind.grandmarket.data.IProductsRepository
import com.armboldmind.grandmarket.data.models.domain.Banner
import com.armboldmind.grandmarket.data.models.domain.Category
import com.armboldmind.grandmarket.data.models.domain.Product
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
import javax.inject.Inject

class HomeViewModel : BaseViewModel() {

    @Inject
    lateinit var mBannersRepository: IBannersRepository

    @Inject
    lateinit var mCategoriesRepository: ICategoriesRepository

    @Inject
    lateinit var mProductsRepository: IProductsRepository

    init {
        appComponent().homeComponent.build()
            .inject(this)
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i("ExceptionTAg", throwable.localizedMessage ?: "")
        }) {
            when (actionIntent) {
                is ActionIntent.InitHomeData -> initHome()
                else -> Unit
            }
        }
    }

    private suspend fun initHome() {
        val bannersResponse = mBannersRepository.getAllBanners()
        val categoriesResponse = mCategoriesRepository.getAllCategories()
        val newArrivalsResponse = mProductsRepository.getNewArrivals()
        val bestsellersResponse = mProductsRepository.getBestSellers()
        val discountsResponse = mProductsRepository.getDiscounts()
        val b = arrayListOf<Banner>()
        val c = arrayListOf<Category>()
        var d = arrayListOf<Product>()
        var e = arrayListOf<Product>()
        var f = arrayListOf<Product>()
        bannersResponse.zip(categoriesResponse) { banners, categories ->
            b.addAll(banners)
            c.addAll(categories)
        }
            .zip(newArrivalsResponse) { _, newArrivals ->
                d.addAll(newArrivals)
            }
            .zip(bestsellersResponse) { _, bestsellers ->
                e.addAll(bestsellers)
            }
            .zip(discountsResponse) { _, discounts ->
                f.addAll(discounts)
            }
            .onStart {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
            }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                Log.i("HomePageTag", "collect -> $b")
                Log.i("HomePageTag", "collect -> $c")
                Log.i("HomePageTag", "collect -> $d")
                Log.i("HomePageTag", "collect -> $e")
                Log.i("HomePageTag", "collect -> $f")
                withContext(Dispatchers.Main) { setState.invoke(ViewState.FetchHomePageData(b, c, d, e, f)) }
            }
    }
}