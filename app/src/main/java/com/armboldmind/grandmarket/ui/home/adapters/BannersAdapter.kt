package com.armboldmind.grandmarket.ui.home.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Banner
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterHomeBannersBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.load

class BannersAdapter(private val onItemClick: (Banner) -> Unit) : BaseAdapter<Banner, BannersAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterHomeBannersBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterHomeBannersBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { banner ->
            holder.binding.apply {
                title.text = banner.bannerTitle
                image.load(banner.mobileMedia.mediaUrl, shimmer)
                root.setOnClickListener { onItemClick.invoke(banner) }
            }
        }
    }
}