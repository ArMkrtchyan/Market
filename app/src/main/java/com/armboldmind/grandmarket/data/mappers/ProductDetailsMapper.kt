package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.ProductDetails
import com.armboldmind.grandmarket.data.models.responsemodels.ProductDetailsResponseModel
import com.armboldmind.grandmarket.shared.enums.ImageSizesEnum
import com.armboldmind.grandmarket.shared.globalextensions.format

class ProductDetailsMapper : IMapper<ProductDetailsResponseModel, ProductDetails> {
    override fun map(data: ProductDetailsResponseModel): ProductDetails {
        return ProductDetails(productId = data.productId ?: 0L,
                              productDescription = data.productDescription ?: "",
                              discountedPrice = data.discountedPrice ?: 0.0,
                              productItemCode = data.productItemCode ?: "",
                              discountedPriceFormatted = (data.discountedPrice ?: 0.0).format(),
                              productPriceFormatted = (data.price ?: 0.0).format(),
                              productName = data.productName ?: "",
                              countryName = data.countryName ?: "",
                              brandName = data.brandName ?: "",
                              countryId = data.countryId ?: 0L,
                              brandId = data.brandId ?: 0L,
                              quantity = data.quantity ?: 0,
                              price = data.price ?: 0.0,
                              productClusteredPriceId = data.productClusteredPriceId ?: 0L,
                              attachedAttribute = data.attachedAttribute ?: false,
                              favorite = data.favorite ?: false,

                              combinationAttributeGroups = arrayListOf<ProductDetails.CombinationAttributeGroup>().apply {
                                  data.combinationAttributeGroups?.map { combinationAttributeGroup ->
                                      add(ProductDetails.CombinationAttributeGroup(
                                          attributeGroupId = combinationAttributeGroup.attributeGroupId ?: 0L,
                                          attributeGroupOption = combinationAttributeGroup.attributeGroupOption ?: 0,
                                          attributeGroupName = combinationAttributeGroup.attributeGroupName ?: "",
                                          colorPicker = combinationAttributeGroup.colorPicker ?: false,
                                          combinationAttributes = arrayListOf<ProductDetails.CombinationAttribute>().apply {
                                              combinationAttributeGroup.combinationAttributes?.map { combinationAttribute ->
                                                  add(ProductDetails.CombinationAttribute(
                                                      productAttributeId = combinationAttribute.productAttributeId ?: 0L,
                                                      attributeName = combinationAttribute.attributeName ?: "",
                                                      attributeId = combinationAttribute.attributeId ?: 0L,
                                                      color = combinationAttribute.color ?: "",
                                                      chosen = combinationAttribute.chosen ?: false,
                                                  ))
                                              }
                                          },
                                      ))
                                  }
                              },
                              productMedias = arrayListOf<ProductDetails.ProductMedia>().apply {
                                  data.productMedias?.map { productMedia ->
                                      add(ProductDetails.ProductMedia(
                                          mediaUrl = productMedia.mediaUrl?.let { it + ImageSizesEnum.LARGE.size } ?: "",
                                          coverPhoto = productMedia.coverPhoto ?: false,
                                      ))
                                  }
                              },
                              characteristicGroupModels = arrayListOf<ProductDetails.CharacteristicGroupModel>().apply {
                                  data.characteristicGroupModels?.map { characteristicGroupModel ->
                                      add(ProductDetails.CharacteristicGroupModel(
                                          characteristicGroupId = characteristicGroupModel.characteristicGroupId ?: 0L,
                                          selected = characteristicGroupModel.selected ?: false,
                                          characteristicGroupName = characteristicGroupModel.characteristicGroupName ?: "",
                                          characteristicModels = arrayListOf<ProductDetails.CharacteristicModel>().apply {
                                              characteristicGroupModel.characteristicModels?.map { characteristicModel ->
                                                  add(ProductDetails.CharacteristicModel(
                                                      characteristicId = characteristicModel.characteristicId ?: 0L,
                                                      characteristicName = characteristicModel.characteristicName ?: "",
                                                      selected = characteristicModel.selected ?: false,
                                                  ))
                                              }
                                          },
                                      ))
                                  }
                              },
                              dimensionModels = arrayListOf<ProductDetails.DimensionModel>().apply {
                                  data.dimensionModels?.map { dimensionModel ->
                                      add(ProductDetails.DimensionModel(
                                          dimensionId = dimensionModel.dimensionId ?: 0L,
                                          dimensionName = dimensionModel.dimensionName ?: "",
                                          settingsDimensionModelInCombinations = arrayListOf<ProductDetails.SettingsDimensionModelInCombination>().apply {
                                              dimensionModel.settingsDimensionModelInCombinations?.map { settingsDimensionModelInCombination ->
                                                  add(ProductDetails.SettingsDimensionModelInCombination(
                                                      settingsDimensionId = settingsDimensionModelInCombination.settingsDimensionId ?: 0L,
                                                      settingsDimensionName = settingsDimensionModelInCombination.settingsDimensionName ?: "",
                                                      settingsDimensionValue = settingsDimensionModelInCombination.settingsDimensionValue ?: 0.0,
                                                  ))
                                              }
                                          },
                                      ))
                                  }
                              },
                              categoryHierarchy = ProductDetails.CategoryHierarchy(
                                  categoryId = data.categoryHierarchy?.categoryId ?: 0L,
                                  categoryName = data.categoryHierarchy?.categoryName ?: "",
                                  childCategory = mapCategoryHierarchy(data.categoryHierarchy?.childCategory),
                              )

        )
    }

    private fun mapCategoryHierarchy(childCategory: ProductDetailsResponseModel.CategoryHierarchy?): ProductDetails.CategoryHierarchy? {
        return childCategory?.let {
            ProductDetails.CategoryHierarchy(
                categoryId = childCategory.categoryId ?: 0L,
                categoryName = childCategory.categoryName ?: "",
                childCategory = mapCategoryHierarchy(childCategory.childCategory),
            )
        }
    }
}
