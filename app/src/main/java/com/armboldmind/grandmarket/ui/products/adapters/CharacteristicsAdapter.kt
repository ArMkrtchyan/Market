package com.armboldmind.grandmarket.ui.products.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterSizesBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class CharacteristicsAdapter(
    private val characteristicIds: ArrayList<Long>,
    private val search: () -> Unit,
) : BaseAdapter<Filters.CharacteristicFilter, CharacteristicsAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterSizesBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterSizesBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.size.apply {
                if (item.chosen) characteristicIds.add(item.characteristicId)
                else characteristicIds.remove(item.characteristicId)
                text = item.characteristicName
                search.invoke()
                if (item.chosen) setBackgroundResource(R.drawable.selected_rounded_stroke)
                else setBackgroundResource(R.drawable.unselected_rounded_stroke)
                setOnClickListener {
                    submitList(currentList.toMutableList()
                                       .apply {
                                           mapIndexed { index, model ->
                                               if (model.characteristicId == item.characteristicId) {
                                                   this[index] = model.copy(chosen = !model.chosen)
                                               }

                                           }
                                       })

                }
            }
        }
    }
}