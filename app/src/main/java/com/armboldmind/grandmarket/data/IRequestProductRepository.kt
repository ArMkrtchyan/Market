package com.armboldmind.grandmarket.data

import androidx.paging.PagingData
import com.armboldmind.grandmarket.data.models.domain.RequestProduct
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface IRequestProductRepository {
    suspend fun getRequests(): Flow<PagingData<RequestProduct>>
    suspend fun createRequestForProduct(
        contactName: String,
        contactInformation: String,
        productName: String,
        description: String? = null,
        categoryId: Long,
        brandId: Long,
        files: List<MultipartBody.Part?>?,
    ): Flow<Long>

    suspend fun getRequestById(id: Long): Flow<RequestProduct>
}