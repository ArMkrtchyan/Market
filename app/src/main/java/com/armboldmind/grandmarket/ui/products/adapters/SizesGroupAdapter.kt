package com.armboldmind.grandmarket.ui.products.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.databinding.AdapterFiltersCharacteristicsBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class SizesGroupAdapter(
    private val filterPreviews: Filters.FilterPreview,
    private val isFirstPosition: Boolean,
    private val attributeIds: ArrayList<Long>,
    private val search: () -> Unit,
) : RecyclerView.Adapter<SizesGroupAdapter.ViewHolder>(), Reset {
    inner class ViewHolder(val binding: AdapterFiltersCharacteristicsBinding) : BaseViewHolder(binding)

    private lateinit var binding: AdapterFiltersCharacteristicsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterFiltersCharacteristicsBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            binding = this
            title.text = filterPreviews.attributeGroupName
            items.adapter = SizesAdapter(attributeIds, search).apply { submitList(filterPreviews.attributeModelList) }
            line.isVisible = !isFirstPosition
            Log.i("PositionTag", "SizesGroup -> $isFirstPosition")
        }
    }

    override fun getItemCount() = 1
    override fun reset() {
        binding.apply { items.adapter = SizesAdapter(attributeIds.apply { clear() }, search).apply { submitList(filterPreviews.attributeModelList) } }
    }
}