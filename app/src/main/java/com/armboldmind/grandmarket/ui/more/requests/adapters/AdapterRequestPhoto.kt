package com.armboldmind.grandmarket.ui.more.requests.adapters

import android.view.ViewGroup
import coil.load
import coil.transform.BlurTransformation
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.PhotoModel
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterRequestPhotoItemBinding
import com.armboldmind.grandmarket.shared.enums.PhotoUploadingStatusEnum
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.invisible
import com.armboldmind.grandmarket.shared.globalextensions.show

class AdapterRequestPhoto(private val addPhoto: () -> Unit, private val removePhoto: (Int) -> Unit) : BaseAdapter<PhotoModel, AdapterRequestPhoto.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterRequestPhotoItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterRequestPhotoItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { photoModel ->
            holder.binding.apply {
                if (photoModel.id == 0) {
                    image.setOnClickListener { addPhoto.invoke() }
                    remove.invisible()
                } else {
                    remove.show()
                    photoModel.status.observeForever { status ->
                        status?.let {
                            when (status) {
                                PhotoUploadingStatusEnum.ATTACHED -> {
                                    photoModel.bitmap?.let { bitmap -> image.setImageBitmap(bitmap) }
                                    photoModel.uri?.let { uri -> image.setImageURI(uri) }
                                }
                                PhotoUploadingStatusEnum.UPLOADING -> {
                                    photoModel.bitmap?.let { bitmap -> image.load(bitmap) { transformations(BlurTransformation(root.context, 9f, 1f)) } }
                                    photoModel.uri?.let { uri -> image.load(uri) { transformations(BlurTransformation(root.context, 9f, 1f)) } }
                                }
                                PhotoUploadingStatusEnum.UPLOADED -> {
                                    photoModel.bitmap?.let { bitmap -> image.setImageBitmap(bitmap) }
                                    photoModel.uri?.let { uri -> image.setImageURI(uri) }
                                }
                            }
                        }
                    }
                }
                remove.setOnClickListener { removePhoto.invoke(position) }
            }
        }
    }
}