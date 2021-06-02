package com.armboldmind.grandmarket.data

import com.armboldmind.grandmarket.data.models.domain.Banner
import kotlinx.coroutines.flow.Flow

interface IBannersRepository {
    suspend fun getAllBanners(): Flow<List<Banner>>
}