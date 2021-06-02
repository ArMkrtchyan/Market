package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.responsemodels.FiltersResponseModel

class FilterMapper : IMapper<FiltersResponseModel, Filters> {
    override fun map(data: FiltersResponseModel): Filters {
        return Filters(
            priceFrom = data.priceFrom ?: 0.0,
            priceTo = data.priceTo ?: 0.0,
            filterPreviews = arrayListOf<Filters.FilterPreview>().apply {
                data.filterPreviews?.let { filterPreview ->
                    filterPreview.map {
                        add(Filters.FilterPreview(
                            attributeGroupName = it.attributeGroupName ?: "",
                            attributeGroupId = it.attributeGroupId ?: 0L,
                            attributeModelList = arrayListOf<Filters.AttributeModel>().apply {
                                it.attributeModelList?.let { attributeModelList ->
                                    attributeModelList.map { attributeModel ->
                                        add(Filters.AttributeModel(
                                            attributeName = attributeModel.attributeName ?: "",
                                            color = attributeModel.color ?: "",
                                            attributeId = attributeModel.attributeId ?: 0L,
                                            chosen = attributeModel.chosen ?: false,
                                        ))
                                    }
                                }
                            },
                            colorPicker = it.colorPicker ?: false,
                        ))
                    }
                }
            },
            brandModels = arrayListOf<Filters.BrandModel>().apply {
                data.brandModels?.let { brandModels ->
                    brandModels.map {
                        add(Filters.BrandModel(
                            brandName = it.brandName ?: "",
                            brandId = it.brandId ?: 0L,
                            chosen = it.chosen ?: false,
                        ))
                    }
                }
            },
            characteristicGroupFilter = arrayListOf<Filters.CharacteristicGroupFilter>().apply {
                data.characteristicGroupFilter?.let { characteristicGroupFilters ->
                    characteristicGroupFilters.map {
                        add(Filters.CharacteristicGroupFilter(
                            characteristicGroupName = it.characteristicGroupName ?: "",
                            characteristicGroupId = it.characteristicGroupId ?: 0L,
                            characteristicFilter = arrayListOf<Filters.CharacteristicFilter>().apply {
                                it.characteristicFilter?.let { characteristicFilter ->
                                    characteristicFilter.map { attributeModel ->
                                        add(Filters.CharacteristicFilter(
                                            characteristicName = attributeModel.characteristicName ?: "",
                                            characteristicId = attributeModel.characteristicId ?: 0L,
                                            chosen = attributeModel.chosen ?: false,
                                        ))
                                    }
                                }
                            },
                        ))
                    }
                }
            },
        )
    }
}