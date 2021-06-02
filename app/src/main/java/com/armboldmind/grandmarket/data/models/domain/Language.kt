package com.armboldmind.grandmarket.data.models.domain

import com.armboldmind.gsport24.root.utils.DifItem

data class Language(val id: Int, val name: String, val uniqueSeoCode: String, var isSelected: Boolean) : DifItem<Language> {
    override fun areItemsTheSame(second: Language): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Language): Boolean {
        return id == second.id && isSelected == second.isSelected
    }
}
