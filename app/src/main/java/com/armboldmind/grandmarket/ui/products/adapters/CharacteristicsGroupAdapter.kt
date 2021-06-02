package com.armboldmind.grandmarket.ui.products.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.databinding.AdapterFiltersCharacteristicsBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class CharacteristicsGroupAdapter(
    private val characteristicGroupFilter: Filters.CharacteristicGroupFilter,
    private val isFirstPosition: Boolean,
    private val characteristicIds: ArrayList<Long>,
    private val search: () -> Unit,
) : RecyclerView.Adapter<CharacteristicsGroupAdapter.ViewHolder>(), Reset {
    private lateinit var binding: AdapterFiltersCharacteristicsBinding

    inner class ViewHolder(val binding: AdapterFiltersCharacteristicsBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterFiltersCharacteristicsBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            binding = this
            title.text = characteristicGroupFilter.characteristicGroupName

            items.adapter = CharacteristicsAdapter(characteristicIds, search).apply { submitList(characteristicGroupFilter.characteristicFilter) }
            line.isVisible = !isFirstPosition
            Log.i("PositionTag", "CharacteristicsGroup -> $isFirstPosition")
        }
    }

    override fun getItemCount() = 1
    override fun reset() {

        binding.apply { items.adapter = CharacteristicsAdapter(characteristicIds.apply { clear() }, search).apply { submitList(characteristicGroupFilter.characteristicFilter) } }
    }
}