package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import com.armboldmind.grandmarket.data.IBannersRepository
import com.armboldmind.grandmarket.data.mappers.BannerMapper
import com.armboldmind.grandmarket.data.models.domain.Banner
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.IBannerService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BannersRepositoryRemote @Inject constructor(
    private val mIBannerService: IBannerService,
    private val mBannerMapper: BannerMapper,
    context: Context,
) : BaseDataSource(context), IBannersRepository {
    override suspend fun getAllBanners(): Flow<List<Banner>> {
        return getListResultForOnePage(mBannerMapper) { mIBannerService.getBanners(0, 20) }
    }

}