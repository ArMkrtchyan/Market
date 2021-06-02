package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModel
import com.armboldmind.grandmarket.data.models.paginatonModels.PaginationResponseModelWithSkip
import com.armboldmind.grandmarket.data.models.requestmodels.ProductDetailsRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.SearchProductsModel
import com.armboldmind.grandmarket.data.models.responsemodels.FiltersResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.ProductDetailsResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.ProductResponseModel
import com.armboldmind.grandmarket.data.network.ResponseModel
import retrofit2.Response
import retrofit2.http.*

interface IProductService {
    @POST("product/getAllProductsForMobile/{page}/{size}")
    suspend fun getProducts(
        @Body searchProductsModel: SearchProductsModel,
        @Path("page") page: Int,
        @Path("size") size: Int,
    ): Response<ResponseModel<PaginationResponseModel<ProductResponseModel>>>

    @GET("product/getAllFavoriteProducts/{skip}/{size}")
    suspend fun getFavoriteProducts(
        @Path("skip") skip: Int,
        @Path("size") size: Int,
    ): Response<ResponseModel<PaginationResponseModelWithSkip<ProductResponseModel>>>

    @POST("product/getSimilarProductsByTagIdList")
    suspend fun getSimilarProducts(
        @Body productDetailsRequestModel: ProductDetailsRequestModel,
    ): Response<ResponseModel<List<ProductResponseModel>>>

    @PUT("product/setOrRemoveFavoriteProductToUser")
    suspend fun favoriteProduct(@Query("productId") productId: Long): Response<ResponseModel<Boolean>>

    @POST("product/getAllProductsFilterForMobile")
    suspend fun getProductFilters(@Body searchProductsModel: SearchProductsModel): Response<ResponseModel<FiltersResponseModel>>

    @POST("product/getByIdForWeb")
    suspend fun getProductDetails(@Body productDetailsRequestModel: ProductDetailsRequestModel): Response<ResponseModel<ProductDetailsResponseModel>>

    @POST("product/getAttributeIdsByProductAttributeIds")
    suspend fun getAttributeIds(@Body productAttributeIds: List<Long>): Response<ResponseModel<List<Long>>>
}