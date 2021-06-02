package com.armboldmind.grandmarket.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.armboldmind.grandmarket.data.mappers.IMapper
import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModel
import com.armboldmind.grandmarket.shared.managers.NetworkStatusManager
import retrofit2.Response

class PagingDataSource<T : Any, K : Any>(
    private val mapper: IMapper<T, K>,
    private val call: suspend (page: Int, size: Int) -> Response<ResponseModel<PaginationResponseModel<T>>>,
) : PagingSource<Int, K>() {

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
                response.body()?.data?.content?.let {
                    return LoadResult.Page(data = it.map { item -> mapper.map(item) },
                                           prevKey = if ((params.key ?: 0) == 0) null else (params.key ?: 0) - 1,
                                           nextKey = if (response.body()?.data?.totalPages == 0 || response.body()?.data?.totalPages == (params.key ?: 0) + 1) null else (params.key
                                               ?: 0) + 1)
                } ?: return LoadResult.Error(Exception())
            } else return LoadResult.Error(Exception())
        } catch (e: Exception) {
            return LoadResult.Error(Exception())
        }
    }
}