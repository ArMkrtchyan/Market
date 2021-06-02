package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.Category
import com.armboldmind.grandmarket.data.models.responsemodels.CategoryResponseModel

class CategoryMapper : IMapper<CategoryResponseModel, Category> {
    override fun map(data: CategoryResponseModel): Category {
        return Category(id = data.id ?: 0L, coverPhoto = data.coverImage ?: "", categoryName = data.name ?: "", subCategories = arrayListOf<Category>().apply {
            data.subCategories?.let {
                it.map { cat ->
                    this.add(Category(cat.id ?: 0L, coverPhoto = cat.coverImage ?: "", categoryName = cat.name ?: "", subCategories = arrayListOf()))
                }
            }
        })
    }
}