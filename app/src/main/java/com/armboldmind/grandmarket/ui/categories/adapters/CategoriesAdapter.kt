package com.armboldmind.grandmarket.ui.categories.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Category
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterCategoriesItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.load
import com.armboldmind.grandmarket.shared.globalextensions.setDimensionRatio
import com.armboldmind.grandmarket.shared.globalextensions.show

class CategoriesAdapter(private val onItemClick: (Category) -> Unit) : BaseAdapter<Category, CategoriesAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterCategoriesItemBinding) : BaseViewHolder(binding) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterCategoriesItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            getItem(position)?.let { item ->
                image.load(item.coverPhoto, shimmer) {
                    name.text = item.categoryName
                    name.show()
                }
                root.setOnClickListener { onItemClick.invoke(item) }
                when (position % 5) {
                    0 -> card.setDimensionRatio("16:20")
                    1 -> card.setDimensionRatio("16:26")
                    2 -> card.setDimensionRatio("16:10")
                    3 -> card.setDimensionRatio("16:24")
                    else -> card.setDimensionRatio("16:18.3")
                }
            }
        }
    }
}