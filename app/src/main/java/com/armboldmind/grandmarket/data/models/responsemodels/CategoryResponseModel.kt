package com.armboldmind.grandmarket.data.models.responsemodels

data class CategoryResponseModel(val id: Long?, val coverImage: String?, val name: String?, val subCategories: List<CategoryResponseModel>?)