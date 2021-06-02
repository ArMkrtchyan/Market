package com.armboldmind.grandmarket.data.models.domain

import android.os.Parcelable
import com.armboldmind.gsport24.root.utils.DifItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(val id: Long, val coverPhoto: String, val categoryName: String, val subCategories: List<Category>) : Parcelable, DifItem<Category> {
    override fun areItemsTheSame(second: Category): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Category): Boolean {
        return id == second.id && coverPhoto == second.coverPhoto && categoryName == second.categoryName
    }

}
