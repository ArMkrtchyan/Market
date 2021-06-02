package com.armboldmind.grandmarket.ui.categories.adapters

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Category
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterSubcategoryItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class AdapterSubCategories(private val onItemClick: (Category) -> Unit) : BaseAdapter<Category, AdapterSubCategories.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterSubcategoryItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterSubcategoryItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            getItem(position)?.let { item ->
                line1.isVisible = position == 0
                name.text = item.categoryName
                root.setOnClickListener { onItemClick.invoke(item) }
            }
        }
    }
}