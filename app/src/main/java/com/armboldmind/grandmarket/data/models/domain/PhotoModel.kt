package com.armboldmind.grandmarket.data.models.domain

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.armboldmind.grandmarket.shared.enums.PhotoUploadingStatusEnum
import com.armboldmind.gsport24.root.utils.DifItem

data class PhotoModel(
    val id: Int,
    val bitmap: Bitmap? = null,
    val uri: Uri? = null,
    val status: MutableLiveData<PhotoUploadingStatusEnum> = MutableLiveData(PhotoUploadingStatusEnum.ATTACHED),
) : DifItem<PhotoModel> {
    override fun areItemsTheSame(second: PhotoModel): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: PhotoModel): Boolean {
        return id == second.id
    }

}