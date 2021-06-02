package com.armboldmind.grandmarket.ui.products.adapters

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.databinding.AdapterFilterColorsBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class ColorsGroupAdapter(
    private val filterPreviews: Filters.FilterPreview,
    private val isFirstPosition: Boolean,
    private val attributeIds: ArrayList<Long>,
    private val search: () -> Unit,
) : RecyclerView.Adapter<ColorsGroupAdapter.ViewHolder>(), Reset {
    inner class ViewHolder(val binding: AdapterFilterColorsBinding) : BaseViewHolder(binding)

    private lateinit var binding: AdapterFilterColorsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterFilterColorsBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            binding = this
            title.text = filterPreviews.attributeGroupName
            line.isVisible = !isFirstPosition
            colors.adapter = ColorsAdapter(attributeIds, search).apply { submitList(filterPreviews.attributeModelList) }
        }
    }

    override fun getItemCount() = 1
    override fun reset() {
        binding.apply { colors.adapter = ColorsAdapter(attributeIds.apply { clear() }, search).apply { submitList(filterPreviews.attributeModelList) } }
    }
}