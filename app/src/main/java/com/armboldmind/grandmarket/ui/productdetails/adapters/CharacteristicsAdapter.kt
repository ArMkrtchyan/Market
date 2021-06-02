package com.armboldmind.grandmarket.ui.productdetails.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.ProductDetails
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterAttributesDetailsBinding
import com.armboldmind.grandmarket.databinding.AdapterSizesBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class CharacteristicsAdapter : BaseAdapter<ProductDetails.CharacteristicGroupModel, CharacteristicsAdapter.AttributesViewHolder>(getDiffCallback()) {
    inner class AttributesViewHolder(val binding: AdapterAttributesDetailsBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AttributesViewHolder(AdapterAttributesDetailsBinding.inflate(parent.inflater(), parent, false))


    override fun onBindViewHolder(holder: AttributesViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.apply {
                title.text = item.characteristicGroupName
                items.adapter = CharacteristicsAdapter().apply { submitList(item.characteristicModels) }
            }
        }
    }

    inner class CharacteristicsAdapter : BaseAdapter<ProductDetails.CharacteristicModel, CharacteristicsAdapter.ViewHolder>(getDiffCallback()) {
        inner class ViewHolder(val binding: AdapterSizesBinding) : BaseViewHolder(binding)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(AdapterSizesBinding.inflate(parent.inflater(), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            getItem(position)?.let { item ->
                holder.binding.size.apply {
                    text = item.characteristicName
                    setBackgroundResource(R.drawable.selected_rounded_stroke)
                }
            }
        }
    }
}