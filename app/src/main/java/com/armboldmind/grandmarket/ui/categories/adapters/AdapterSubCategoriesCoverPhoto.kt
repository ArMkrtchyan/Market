package com.armboldmind.grandmarket.ui.categories.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.databinding.AdapterSubcategoryCoverPhotoBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.load

class AdapterSubCategoriesCoverPhoto(private val mCoverPhoto: String) : RecyclerView.Adapter<AdapterSubCategoriesCoverPhoto.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterSubcategoryCoverPhotoBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterSubcategoryCoverPhotoBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply { image.load(mCoverPhoto, shimmer) }

    }

    override fun getItemCount() = 1
}