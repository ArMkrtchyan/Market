package com.armboldmind.grandmarket.data.models.responsemodels

import com.armboldmind.gsport24.root.utils.DifItem

data class CardResponseModel(val id: Int, val isDefault: Boolean = false) : DifItem<CardResponseModel> {
    override fun areItemsTheSame(second: CardResponseModel): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: CardResponseModel): Boolean {
        return id == second.id && isDefault == second.isDefault
    }

}
