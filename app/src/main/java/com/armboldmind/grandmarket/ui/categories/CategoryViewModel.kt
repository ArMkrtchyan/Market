package com.armboldmind.grandmarket.ui.categories

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.ICategoriesRepository
import com.armboldmind.grandmarket.shared.globalextensions.appComponent
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryViewModel : BaseViewModel() {
    @Inject
    lateinit var mCategoriesRepository: ICategoriesRepository

    init {
        appComponent().categoriesComponent.build()
            .inject(this)
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i("ExceptionTAg", throwable.localizedMessage ?: "")
            setState.invoke(ViewState.ErrorState(throwable))
        }) {
            when (actionIntent) {
                is ActionIntent.GetCategoriesData -> getCategories()
                else -> Unit
            }
        }
    }

    private suspend fun getCategories() {
        mCategoriesRepository.getAllCategories()
            .onStart {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
            }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.FetchCategoriesData(it)) }
            }
    }
}