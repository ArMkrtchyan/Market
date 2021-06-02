package com.armboldmind.grandmarket.ui.categories.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.databinding.AdapterCategoryTitleBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class AdapterCategoriesTitle(private val mTitle: String) : RecyclerView.Adapter<AdapterCategoriesTitle.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterCategoryTitleBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterCategoryTitleBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = mTitle

    }

    override fun getItemCount() = 1
}