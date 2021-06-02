package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import androidx.paging.PagingData
import com.armboldmind.grandmarket.data.IRequestProductRepository
import com.armboldmind.grandmarket.data.mappers.RequestProductMapper
import com.armboldmind.grandmarket.data.models.domain.RequestProduct
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.IRequestProductService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class RequestProductRepositoryRemote @Inject constructor(
    private val mIRequestProductService: IRequestProductService,
    private val mRequestProductMapper: RequestProductMapper,
    mApplication: Context,
) : BaseDataSource(mApplication), IRequestProductRepository {
    override suspend fun getRequests(): Flow<PagingData<RequestProduct>> {
        return getPagingResult(mRequestProductMapper) { page, size -> mIRequestProductService.getRequests(page, size) }
    }

    override suspend fun createRequestForProduct(
        contactName: String,
        contactInformation: String,
        productName: String,
        description: String?,
        categoryId: Long,
        brandId: Long,
        files: List<MultipartBody.Part?>?,
    ): Flow<Long> {
        return getResult {
            if (files != null && files.isNotEmpty()) mIRequestProductService.createRequestForProduct(contactName,
                                                                                                     contactInformation,
                                                                                                     productName,
                                                                                                     description,
                                                                                                     categoryId,
                                                                                                     brandId,
                                                                                                     files)
            else mIRequestProductService.createRequestForProduct(contactName, contactInformation, productName, description, categoryId, brandId)
        }
    }

    override suspend fun getRequestById(id: Long): Flow<RequestProduct> {
        return getResultWithMapper(mRequestProductMapper) { mIRequestProductService.getRequestById(id) }
    }
}