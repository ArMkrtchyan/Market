package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.Brand
import com.armboldmind.grandmarket.data.models.domain.RadioButtonModel

class BrandsToRadioButtonMapper : IMapper<Brand, RadioButtonModel> {
    override fun map(data: Brand): RadioButtonModel {
        return RadioButtonModel(data.id, data.title, false)
    }
}