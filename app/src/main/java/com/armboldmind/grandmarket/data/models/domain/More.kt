package com.armboldmind.grandmarket.data.models.domain

import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.armboldmind.gsport24.root.utils.DifItem

data class More(
    val id: Int,
    @DrawableRes val icon: Int,
    val title: String,
    val action: NavDirections?,
    val badgeCount: MutableLiveData<Int> = MutableLiveData<Int>(null),
) : DifItem<More> {
    override fun areItemsTheSame(second: More): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: More): Boolean {
        return id == second.id
    }

}