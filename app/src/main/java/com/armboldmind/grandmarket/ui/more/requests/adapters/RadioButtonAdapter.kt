package com.armboldmind.grandmarket.ui.more.requests.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.RadioButtonModel
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterRadoiButtonItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class RadioButtonAdapter(private val onItemClick: (RadioButtonModel) -> Unit) : BaseAdapter<RadioButtonModel, RadioButtonAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterRadoiButtonItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterRadoiButtonItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.apply {
                adapterItem.isChecked = item.isSelected
                adapterItem.text = item.title
                root.setOnClickListener {
                    val list = arrayListOf<RadioButtonModel>()
                    currentList.toMutableList()
                        .apply {
                            this.map { model -> list.add(model.copy(isSelected = model.id == item.id)) }
                        }
                    submitList(list)
                    onItemClick.invoke(item)
                }
            }
        }
    }
}