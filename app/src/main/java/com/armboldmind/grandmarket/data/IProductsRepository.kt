package com.armboldmind.grandmarket.data

import androidx.paging.PagingData
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.domain.ProductDetails
import com.armboldmind.grandmarket.data.models.requestmodels.ProductDetailsRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.SearchProductsModel
import kotlinx.coroutines.flow.Flow

interface IProductsRepository {
    suspend fun getNewArrivals(): Flow<List<Product>>
    suspend fun getBestSellers(): Flow<List<Product>>
    suspend fun getDiscounts(): Flow<List<Product>>
    suspend fun favoriteProduct(productId: Long): Flow<Boolean>
    suspend fun getFavoriteProducts(): Flow<PagingData<Product>>
    suspend fun getProducts(searchProductsModel: SearchProductsModel): Flow<PagingData<Product>>
    suspend fun getProductsCount(searchProductsModel: SearchProductsModel): Flow<Long>
    suspend fun getProductFilters(searchProductsModel: SearchProductsModel): Flow<Filters>
    suspend fun getProductDetails(productDetailsRequestModel: ProductDetailsRequestModel): Flow<ProductDetails>
    suspend fun getSimilarProducts(productDetailsRequestModel: ProductDetailsRequestModel): Flow<List<Product>>
    suspend fun getAttributeIds(productAttributeIds: List<Long>): Flow<List<Long>>
}