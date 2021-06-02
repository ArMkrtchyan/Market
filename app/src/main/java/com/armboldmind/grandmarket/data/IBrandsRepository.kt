package com.armboldmind.grandmarket.data

import com.armboldmind.grandmarket.data.models.domain.Brand
import com.armboldmind.grandmarket.data.models.requestmodels.BrandsRequestModel
import kotlinx.coroutines.flow.Flow

interface IBrandsRepository {
    suspend fun getBrandsList(requestModel: BrandsRequestModel): Flow<List<Brand>>
}