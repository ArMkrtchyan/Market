package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import com.armboldmind.grandmarket.data.IBrandsRepository
import com.armboldmind.grandmarket.data.mappers.BrandMapper
import com.armboldmind.grandmarket.data.models.domain.Brand
import com.armboldmind.grandmarket.data.models.requestmodels.BrandsRequestModel
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.IBrandService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BrandsRepositoryRemote @Inject constructor(
    private val mIBrandService: IBrandService,
    private val mBrandMapper: BrandMapper,
    context: Context,
) : BaseDataSource(context), IBrandsRepository {
    override suspend fun getBrandsList(requestModel: BrandsRequestModel): Flow<List<Brand>> {
        return getListResultForOnePage(mBrandMapper) { mIBrandService.getBrands(0, 50, requestModel) }

    }
}