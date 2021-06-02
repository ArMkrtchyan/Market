package com.armboldmind.grandmarket.data.models.domain

import android.os.Parcelable
import com.armboldmind.gsport24.root.utils.DifItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RadioButtonModel(val id: Long, val title: String, var isSelected: Boolean) : Parcelable, DifItem<RadioButtonModel> {
    override fun areItemsTheSame(second: RadioButtonModel): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: RadioButtonModel): Boolean {
        return id == second.id && title == second.title && isSelected == second.isSelected
    }

}
