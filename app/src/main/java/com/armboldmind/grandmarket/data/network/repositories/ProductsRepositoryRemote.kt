package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import androidx.paging.PagingData
import com.armboldmind.grandmarket.data.IProductsRepository
import com.armboldmind.grandmarket.data.mappers.FavoritesMapper
import com.armboldmind.grandmarket.data.mappers.FilterMapper
import com.armboldmind.grandmarket.data.mappers.ProductDetailsMapper
import com.armboldmind.grandmarket.data.mappers.ProductMapper
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.domain.ProductDetails
import com.armboldmind.grandmarket.data.models.requestmodels.ProductDetailsRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.SearchProductsModel
import com.armboldmind.grandmarket.data.models.requestmodels.searchProductsModel
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.IProductService
import com.armboldmind.grandmarket.shared.enums.LabelsEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepositoryRemote @Inject constructor(
    private val mIProductService: IProductService,
    private val mProductMapper: ProductMapper,
    private val mFavoritesMapper: FavoritesMapper,
    private val mFilterMapper: FilterMapper,
    private val mProductDetailsMapper: ProductDetailsMapper,
    context: Context,
) : BaseDataSource(context), IProductsRepository {
    override suspend fun getNewArrivals(): Flow<List<Product>> {
        return getListResultForOnePage(mProductMapper) {
            mIProductService.getProducts(searchProductsModel {
                labelType = LabelsEnum.NEW.type
            }, 0, 10)
        }
    }

    override suspend fun getBestSellers(): Flow<List<Product>> {
        return getListResultForOnePage(mProductMapper) {
            mIProductService.getProducts(searchProductsModel {
                labelType = LabelsEnum.BEST_SELL.type
            }, 0, 10)
        }
    }

    override suspend fun favoriteProduct(productId: Long): Flow<Boolean> {
        return getResult { mIProductService.favoriteProduct(productId) }
    }

    override suspend fun getDiscounts(): Flow<List<Product>> {
        return getListResultForOnePage(mProductMapper) {
            mIProductService.getProducts(searchProductsModel {
                discounted = true
            }, 0, 10)
        }
    }

    override suspend fun getFavoriteProducts(): Flow<PagingData<Product>> {
        return getPagingResultWithSkip(mFavoritesMapper) { skip, size -> mIProductService.getFavoriteProducts(skip, size) }
    }

    override suspend fun getProducts(searchProductsModel: SearchProductsModel): Flow<PagingData<Product>> {
        return getPagingResult(mProductMapper) { page, size -> mIProductService.getProducts(searchProductsModel, page, size) }
    }

    override suspend fun getProductsCount(searchProductsModel: SearchProductsModel): Flow<Long> {
        return flow {
            getResult { mIProductService.getProducts(searchProductsModel, 0, 1) }.collect {
                emit(it.totalElements.toLong())
            }
        }
    }

    override suspend fun getProductFilters(searchProductsModel: SearchProductsModel): Flow<Filters> {
        return getResultWithMapper(mFilterMapper) { mIProductService.getProductFilters(searchProductsModel) }
    }

    override suspend fun getProductDetails(productDetailsRequestModel: ProductDetailsRequestModel): Flow<ProductDetails> {
        return getResultWithMapper(mProductDetailsMapper) { mIProductService.getProductDetails(productDetailsRequestModel) }
    }

    override suspend fun getSimilarProducts(productDetailsRequestModel: ProductDetailsRequestModel): Flow<List<Product>> {
        return getListResult(mProductMapper) { mIProductService.getSimilarProducts(productDetailsRequestModel) }
    }

    override suspend fun getAttributeIds(productAttributeIds: List<Long>): Flow<List<Long>> {
        return getResult { mIProductService.getAttributeIds(productAttributeIds) }
    }
}