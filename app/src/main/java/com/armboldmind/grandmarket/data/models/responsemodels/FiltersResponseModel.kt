package com.armboldmind.grandmarket.data.models.responsemodels

data class FiltersResponseModel(
    val priceFrom: Double?,
    val priceTo: Double?,
    val filterPreviews: List<FilterPreview>?,
    val brandModels: List<BrandModel>?,
    val characteristicGroupFilter: List<CharacteristicGroupFilter>?,
) {
    data class FilterPreview(
        val attributeGroupName: String?,
        val colorPicker: Boolean?,
        val attributeGroupId: Long?,
        val attributeModelList: List<AttributeModel>?,
    )

    data class AttributeModel(
        val attributeName: String?,
        val color: String?,
        val attributeId: Long?,
        val chosen: Boolean?,
    )

    data class CharacteristicGroupFilter(
        val characteristicGroupName: String?,
        val characteristicGroupId: Long?,
        val characteristicFilter: List<CharacteristicFilter>?,
    )

    data class CharacteristicFilter(
        val characteristicName: String?,
        val characteristicId: Long?,
        val chosen: Boolean?,
    )

    data class BrandModel(
        val brandName: String?,
        val brandId: Long?,
        val chosen: Boolean?,
    )
}