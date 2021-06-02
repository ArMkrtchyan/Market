package com.armboldmind.grandmarket.data.models.domain

import android.os.Parcelable
import com.armboldmind.gsport24.root.utils.DifItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Subscription(val id: Long) : Parcelable, DifItem<Subscription> {
    override fun areItemsTheSame(second: Subscription): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Subscription): Boolean {
        return id == second.id
    }

}
