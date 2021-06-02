package com.armboldmind.grandmarket.data.models.domain

import androidx.annotation.DrawableRes
import com.armboldmind.gsport24.root.utils.DifItem

data class Notification(
    val id: Long,
    @DrawableRes val icon: Int,
    @DrawableRes val background: Int,
    val title: String,
    val message: String,
    val sendDate: Long,
    val action: Int,
    val seen: Boolean,
    val actionId: Long,
) : DifItem<Notification> {
    override fun areItemsTheSame(second: Notification): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: Notification): Boolean {
        return id == second.id && title == second.title && message == second.message && sendDate == second.sendDate && seen == second.seen && action == second.action && actionId == second.actionId
    }
}