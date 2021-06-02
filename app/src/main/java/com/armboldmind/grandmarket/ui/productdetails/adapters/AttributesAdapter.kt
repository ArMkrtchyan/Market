package com.armboldmind.grandmarket.ui.productdetails.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.ViewGroup
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.ProductDetails
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterAttributesDetailsBinding
import com.armboldmind.grandmarket.databinding.AdapterAttributesDetailsColorBinding
import com.armboldmind.grandmarket.databinding.AdapterColorsBinding
import com.armboldmind.grandmarket.databinding.AdapterSizesBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class AttributesAdapter(
    private val onAttributeClick: (id: Long, List<Long>) -> Unit,
) : BaseAdapter<ProductDetails.CombinationAttributeGroup, BaseViewHolder>(getDiffCallback()) {
    companion object {
        private const val VIEW_STATE_COLOR = 1
        private const val VIEW_STATE_ATTR = 2
    }

    inner class ColorViewHolder(val binding: AdapterAttributesDetailsColorBinding) : BaseViewHolder(binding)
    inner class AttributesViewHolder(val binding: AdapterAttributesDetailsBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == VIEW_STATE_COLOR) ColorViewHolder(AdapterAttributesDetailsColorBinding.inflate(parent.inflater(), parent, false))
        else AttributesViewHolder(AdapterAttributesDetailsBinding.inflate(parent.inflater(), parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).colorPicker) VIEW_STATE_COLOR
        else VIEW_STATE_ATTR
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let { item ->
            when (holder) {
                is ColorViewHolder -> {
                    holder.binding.apply {
                        title.text = item.attributeGroupName
                        colors.adapter = ColorsAdapter().apply { submitList(item.combinationAttributes) }
                    }
                }
                is AttributesViewHolder -> {
                    holder.binding.apply {
                        title.text = item.attributeGroupName
                        items.adapter = CharacteristicsAdapter().apply { submitList(item.combinationAttributes) }
                    }
                }
                else -> Unit
            }
        }
    }

    inner class ColorsAdapter : BaseAdapter<ProductDetails.CombinationAttribute, ColorsAdapter.ViewHolder>(getDiffCallback()) {
        inner class ViewHolder(val binding: AdapterColorsBinding) : BaseViewHolder(binding)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(AdapterColorsBinding.inflate(parent.inflater(), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            getItem(position)?.let { item ->
                holder.binding.color.apply {
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.color))
                    if (item.chosen) setImageResource(R.drawable.selected_color_foreground)
                    else setImageResource(R.drawable.unselected_color_foreground)
                    setOnClickListener {
                        val list: List<Long> = currentList.flatMap { currentList.map { it.productAttributeId } }
                        onAttributeClick.invoke(item.productAttributeId, list)
                    }
                }
            }
        }
    }

    inner class CharacteristicsAdapter : BaseAdapter<ProductDetails.CombinationAttribute, CharacteristicsAdapter.ViewHolder>(getDiffCallback()) {
        inner class ViewHolder(val binding: AdapterSizesBinding) : BaseViewHolder(binding)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(AdapterSizesBinding.inflate(parent.inflater(), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            getItem(position)?.let { item ->
                holder.binding.size.apply {
                    text = item.attributeName
                    if (item.chosen) setBackgroundResource(R.drawable.selected_rounded_stroke)
                    else setBackgroundResource(R.drawable.unselected_rounded_stroke)
                    setOnClickListener {
                        val list: List<Long> = currentList.flatMap { currentList.map { it.productAttributeId } }
                        onAttributeClick.invoke(item.productAttributeId, list)
                    }
                }
            }
        }
    }
}