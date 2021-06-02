package com.armboldmind.grandmarket.ui.products.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterSizesBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class SizesAdapter(
    private val attributeIds: ArrayList<Long>,
    private val search: () -> Unit,
) : BaseAdapter<Filters.AttributeModel, SizesAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterSizesBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterSizesBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.size.apply {
                text = item.attributeName
                if (item.chosen) attributeIds.add(item.attributeId)
                else attributeIds.remove(item.attributeId)
                if (item.chosen) setBackgroundResource(R.drawable.selected_rounded_stroke)
                else setBackgroundResource(R.drawable.unselected_rounded_stroke)
                search.invoke()
                setOnClickListener {
                    submitList(currentList.toMutableList()
                                       .apply {
                                           mapIndexed { index, model ->
                                               if (model.attributeId == item.attributeId) {
                                                   this[index] = model.copy(chosen = !model.chosen)
                                               }

                                           }
                                       })

                }
            }
        }
    }
}