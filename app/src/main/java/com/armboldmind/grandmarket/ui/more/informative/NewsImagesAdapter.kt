package com.armboldmind.grandmarket.ui.more.informative

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.NewsAndEventsDetails
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterRequestProductImagesItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.load

class NewsImagesAdapter : BaseAdapter<NewsAndEventsDetails.MediaPreviews, NewsImagesAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterRequestProductImagesItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterRequestProductImagesItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setImage(getItem(position).mediaUrl, holder.binding)
    }

    private fun setImage(url: String, binding: AdapterRequestProductImagesItemBinding) {
        binding.apply { image.load(url, shimmer) }
    }

}