package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.Category
import com.armboldmind.grandmarket.data.models.domain.RadioButtonModel

class CategoriesToRadioButtonMapper : IMapper<Category, RadioButtonModel> {
    override fun map(data: Category): RadioButtonModel {
        return RadioButtonModel(data.id, data.categoryName, false)
    }
}