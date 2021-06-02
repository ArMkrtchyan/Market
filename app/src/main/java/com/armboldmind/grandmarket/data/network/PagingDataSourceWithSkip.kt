package com.armboldmind.grandmarket.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.armboldmind.grandmarket.data.mappers.IMapper
import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModelWithSkip
import com.armboldmind.grandmarket.shared.managers.NetworkStatusManager
import retrofit2.Response

class PagingDataSourceWithSkip<T : Any, K : Any>(
    private val mapper: IMapper<T, K>,
    private val call: suspend (skip: Int, size: Int) -> Response<ResponseModel<PaginationResponseModelWithSkip<T>>>,
) : PagingSource<Int, K>() {
    private val data = arrayListOf<T>()
    override fun getRefreshKey(state: PagingState<Int, K>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, K> {
        if (!NetworkStatusManager.isNetworkAvailable()) {
            return LoadResult.Error(Exception())
        }
        try {
            val response = call(params.key ?: 0, 20)
            if (response.isSuccessful) {
                response.body()?.data?.favoriteProducts?.let {
                    data.addAll(it)
                    return LoadResult.Page(data = it.map { item -> mapper.map(item) },
                                           prevKey = if (data.size - 20 <= 0) null else data.size - 20,
                                           nextKey = if (response.body()?.data?.elementsLeft ?: 0 == 0) null else data.size)
                } ?: return LoadResult.Error(Exception())
            } else return LoadResult.Error(Exception())
        } catch (e: Exception) {
            return LoadResult.Error(Exception())
        }
    }
}