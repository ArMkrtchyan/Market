package com.armboldmind.grandmarket.data.models.domain

import com.armboldmind.gsport24.root.utils.DifItem

data class Card(val id: Int, val isDefault: Boolean = false) : DifItem<Card> {
    override fun areItemsTheSame(second: Card): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Card): Boolean {
        return id == second.id && isDefault == second.isDefault
    }

}
