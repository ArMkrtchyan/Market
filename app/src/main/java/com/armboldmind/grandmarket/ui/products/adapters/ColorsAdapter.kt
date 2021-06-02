package com.armboldmind.grandmarket.ui.products.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.ViewGroup
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterColorsBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class ColorsAdapter(
    private val attributeIds: ArrayList<Long>,
    private val search: () -> Unit,
) : BaseAdapter<Filters.AttributeModel, ColorsAdapter.ViewHolder>(getDiffCallback()) {

    inner class ViewHolder(val binding: AdapterColorsBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterColorsBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.color.apply {
                if (item.chosen) attributeIds.add(item.attributeId)
                else attributeIds.remove(item.attributeId)
                backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.color))
                if (item.chosen) setImageResource(R.drawable.selected_color_foreground)
                else setImageResource(R.drawable.unselected_color_foreground)
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