package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import com.armboldmind.grandmarket.data.ICategoriesRepository
import com.armboldmind.grandmarket.data.mappers.CategoryMapper
import com.armboldmind.grandmarket.data.models.domain.Category
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.ICategoryService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoriesRepositoryRemote @Inject constructor(
    private val mICategoryService: ICategoryService,
    private val mCategoryMapper: CategoryMapper,
    context: Context,
) : BaseDataSource(context), ICategoriesRepository {
    override suspend fun getAllCategories(): Flow<List<Category>> {
        return getListResult(mCategoryMapper) { mICategoryService.getTopCategories() }
    }

}