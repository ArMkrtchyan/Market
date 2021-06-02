package com.armboldmind.grandmarket.data.mappers

import com.armboldmind.grandmarket.data.models.domain.FAQ
import com.armboldmind.grandmarket.data.models.responsemodels.FAQResponseModel

class FAQMapper : IMapper<FAQResponseModel, FAQ> {
    override fun map(data: FAQResponseModel): FAQ {
        return FAQ(id = data.id ?: 0, question = data.question ?: "", answer = data.answer ?: "")
    }

    fun mapToAnswer(data: FAQ): FAQ {
        return FAQ(parentId = data.id, answer = data.answer)
    }

    fun mapToLine(data: FAQ): FAQ {
        return FAQ(parentId = -2)
    }
}