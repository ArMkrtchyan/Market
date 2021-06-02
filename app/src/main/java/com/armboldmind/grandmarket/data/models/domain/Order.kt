package com.armboldmind.grandmarket.data.models.domain

import android.os.Parcelable
import com.armboldmind.gsport24.root.utils.DifItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(val id: Long) : Parcelable, DifItem<Order> {
    override fun areItemsTheSame(second: Order): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Order): Boolean {
        return id == second.id
    }

}
