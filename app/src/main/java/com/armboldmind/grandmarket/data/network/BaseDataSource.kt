package com.armboldmind.grandmarket.data.network

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.armboldmind.grandmarket.GrandMarketApp
import com.armboldmind.grandmarket.data.mappers.IMapper
import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModel
import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModelWithSkip
import com.armboldmind.grandmarket.shared.managers.ConnectionLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.UnknownHostException

open class BaseDataSource(private val application: Context) {
    protected suspend fun <T : Any> getResult(call: suspend () -> Response<ResponseModel<T>>): Flow<T> {
        return flow {
            if (ConnectionLiveData(application).value == false) {
                throw UnknownHostException()
            }
            val response = call()
            if (response.isSuccessful) {
                if (response.body()?.success == true) {
                    response.body()?.data?.let {
                        emit(it)
                    } ?: run { throw Exception("SomeThing went wrong") }
                } else {
                    val message = if (response.body() != null) response.body()!!.message
                    else "SomeThing went wrong"
                    throw SuccessException(message)
                }
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED || response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                GrandMarketApp.getInstance().unAuthorizationLiveDate.postValue(true)
                throw HttpException(response)
            } else {
                val message = if (response.body() != null) response.body()!!.message
                else "SomeThing went wrong"
                throw Exception(message)
            }

        }.flowOn(Dispatchers.IO)
    }

    protected suspend fun <T : Any, K : Any> getResultWithMapper(mapper: IMapper<T, K>, call: suspend () -> Response<ResponseModel<T>>): Flow<K> {
        return flow {
            if (ConnectionLiveData(application).value == false) {
                throw UnknownHostException()
            }
            val response = call()
            if (response.isSuccessful) {
                if (response.body()?.success == true) {
                    response.body()?.data?.let {
                        emit(mapper.map(it))
                    } ?: run { throw Exception("SomeThing went wrong") }
                } else {
                    val message = if (response.body() != null) response.body()!!.message
                    else "SomeThing went wrong"
                    throw SuccessException(message)
                }
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED || response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                GrandMarketApp.getInstance().unAuthorizationLiveDate.postValue(true)
                throw HttpException(response)
            } else {
                val message = if (response.body() != null) response.body()!!.message
                else "SomeThing went wrong"
                throw Exception(message)
            }
        }.flowOn(Dispatchers.IO)
    }

    protected suspend fun <T : Any, K : Any> getListResult(mapper: IMapper<T, K>, call: suspend () -> Response<ResponseModel<List<T>>>): Flow<List<K>> {
        return flow {
            if (ConnectionLiveData(application).value == false) {
                throw UnknownHostException()
            }
            val response = call()
            if (response.isSuccessful) {
                if (response.body()?.success == true) {
                    response.body()?.data?.let {
                        emit(it.map { item -> mapper.map(item) })
                    } ?: run { throw Exception("SomeThing went wrong") }
                } else {
                    val message = if (response.body() != null) response.body()!!.message
                    else "SomeThing went wrong"
                    throw SuccessException(message)
                }
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED || response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                GrandMarketApp.getInstance().unAuthorizationLiveDate.postValue(true)
                throw HttpException(response)
            } else {
                val message = if (response.body() != null) response.body()!!.message
                else "SomeThing went wrong"
                throw Exception(message)
            }
        }.flowOn(Dispatchers.IO)
    }

    protected suspend fun <T : Any, K : Any> getListResultForOnePage(
        mapper: IMapper<T, K>,
        call: suspend () -> Response<ResponseModel<PaginationResponseModel<T>>>,
    ): Flow<List<K>> {
        return flow {
            if (ConnectionLiveData(application).value == false) {
                throw UnknownHostException()
            }
            val response = call()
            if (response.isSuccessful) {
                if (response.body()?.success == true) {
                    response.body()?.data?.content?.let {
                        emit(it.map { item -> mapper.map(item) })
                    } ?: run { throw Exception("SomeThing went wrong") }
                } else {
                    val message = if (response.body() != null) response.body()!!.message
                    else "SomeThing went wrong"
                    throw SuccessException(message)
                }
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED || response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                GrandMarketApp.getInstance().unAuthorizationLiveDate.postValue(true)
                throw HttpException(response)
            } else {
                val message = if (response.body() != null) response.body()!!.message
                else "SomeThing went wrong"
                throw Exception(message)
            }
        }.flowOn(Dispatchers.IO)
    }

    protected fun <T : Any, K : Any> getPagingResult(
        mapper: IMapper<T, K>,
        call: suspend (page: Int, size: Int) -> Response<ResponseModel<PaginationResponseModel<T>>>,
    ): Flow<PagingData<K>> {
        return Pager(createDefaultPagingConfig()) {
            PagingDataSource(mapper, call)
        }.flow
    }

    protected fun <T : Any, K : Any> getPagingResultWithSkip(
        mapper: IMapper<T, K>,
        call: suspend (skip: Int, size: Int) -> Response<ResponseModel<PaginationResponseModelWithSkip<T>>>,
    ): Flow<PagingData<K>> {
        return Pager(createDefaultPagingConfig()) {
            PagingDataSourceWithSkip(mapper, call)
        }.flow
    }

    private fun createDefaultPagingConfig(): PagingConfig {
        return PagingConfig(20, 5, enablePlaceholders = false)
    }

    class SuccessException(mMessage: String?) : Exception(mMessage)
}