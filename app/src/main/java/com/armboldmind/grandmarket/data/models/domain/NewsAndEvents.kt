package com.armboldmind.grandmarket.data.models.domain

import android.os.Parcelable
import com.armboldmind.gsport24.root.utils.DifItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsAndEvents(
    val id: Long,
    val title: String,
    val description: String,
    val newsCoverPhoto: String,
    val createdDate: Long,
) : Parcelable, DifItem<NewsAndEvents> {
    override fun areItemsTheSame(second: NewsAndEvents): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: NewsAndEvents): Boolean {
        return id == second.id && title == second.title && description == second.description && newsCoverPhoto == second.newsCoverPhoto && createdDate == second.createdDate
    }
}