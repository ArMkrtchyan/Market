package com.armboldmind.grandmarket.data.models.domain

import android.os.Parcelable
import com.armboldmind.gsport24.root.utils.DifItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Brand(val id: Long, val title: String, val logo: String) : Parcelable, DifItem<Brand> {
    override fun areItemsTheSame(second: Brand): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Brand): Boolean {
        return id == second.id && title == second.title && logo == second.logo
    }

}
