package com.armboldmind.grandmarket.ui.more.requests.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.RequestProduct
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterRequestProductImagesItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.load

class RequestProductImagesAdapter : BaseAdapter<RequestProduct.AttachedPictures, RequestProductImagesAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterRequestProductImagesItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterRequestProductImagesItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            getItem(position)?.let { image.load(it.mediaUrl, shimmer) }
        }
    }
}