package com.armboldmind.grandmarket.ui.productdetails.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.ProductDetails
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterProductDetailsImagesBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.load

class AdapterProductImages : BaseAdapter<ProductDetails.ProductMedia, AdapterProductImages.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterProductDetailsImagesBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterProductDetailsImagesBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { productMedia ->
            holder.binding.apply {
                image.load(productMedia.mediaUrl, shimmer)
            }
        }
    }
}