package com.armboldmind.grandmarket.data.models.responsemodels

data class ProductDetailsResponseModel(
    val productId: Long?,
    val productDescription: String?,
    val discountedPrice: Double?,
    val productItemCode: String?,
    val productName: String?,
    val countryName: String?,
    val brandName: String?,
    val countryId: Long?,
    val brandId: Long?,
    val quantity: Int?,
    val price: Double?,
    val productClusteredPriceId: Long?,
    val attachedAttribute: Boolean?,
    val favorite: Boolean?,

    val combinationAttributeGroups: List<CombinationAttributeGroup>?,
    val productMedias: List<ProductMedia>?,
    val characteristicGroupModels: List<CharacteristicGroupModel>?,
    val dimensionModels: List<DimensionModel>?,
    val categoryHierarchy: CategoryHierarchy?,
) {
    data class CombinationAttributeGroup(
        val attributeGroupOption: Int?,
        val attributeGroupName: String?,
        val attributeGroupId: Long?,
        val colorPicker: Boolean?,
        val combinationAttributes: List<CombinationAttribute>?,
    )

    data class CombinationAttribute(
        val productAttributeId: Long?,
        val attributeName: String?,
        val attributeId: Long?,
        val color: String?,
        val chosen: Boolean?,
    )

    data class ProductMedia(
        val mediaUrl: String?,
        val coverPhoto: Boolean?,
    )

    data class DimensionModel(
        val dimensionId: Long?,
        val dimensionName: String?,
        val settingsDimensionModelInCombinations: List<SettingsDimensionModelInCombination>?,
    )

    data class SettingsDimensionModelInCombination(
        val settingsDimensionId: Long?,
        val settingsDimensionName: String?,
        val settingsDimensionValue: Double?,
    )

    data class CharacteristicGroupModel(
        val characteristicGroupId: Long?,
        val characteristicGroupName: String?,
        val selected: Boolean?,
        val characteristicModels: List<CharacteristicModel>?,
    )

    data class CharacteristicModel(
        val characteristicId: Long?,
        val characteristicName: String?,
        val selected: Boolean?,
    )

    data class CategoryHierarchy(
        val categoryId: Long?,
        val categoryName: String?,
        val childCategory: CategoryHierarchy?,
    )
}