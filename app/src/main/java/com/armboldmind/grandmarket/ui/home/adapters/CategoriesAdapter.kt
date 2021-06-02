package com.armboldmind.grandmarket.ui.home.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Category
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterHomeCategoriesBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.load

class CategoriesAdapter(private val onItemClick: (Category) -> Unit) : BaseAdapter<Category, CategoriesAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterHomeCategoriesBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterHomeCategoriesBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            getItem(position)?.let { item ->
                name.text = item.categoryName
                image.load(item.coverPhoto, shimmer)
                root.setOnClickListener { onItemClick.invoke(item) }
            }
        }
    }
}