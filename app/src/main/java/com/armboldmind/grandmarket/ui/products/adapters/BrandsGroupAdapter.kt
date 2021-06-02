package com.armboldmind.grandmarket.ui.products.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.GrandMarketApp
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.requestmodels.SearchProductsModel
import com.armboldmind.grandmarket.databinding.AdapterFiltersCharacteristicsBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class BrandsGroupAdapter(
    private val brandModels: List<Filters.BrandModel>, private val isFirstPosition: Boolean, private val searchProductModel: SearchProductsModel,
    private val search: () -> Unit,
) : RecyclerView.Adapter<BrandsGroupAdapter.ViewHolder>(), Reset {

    private lateinit var binding: AdapterFiltersCharacteristicsBinding

    inner class ViewHolder(val binding: AdapterFiltersCharacteristicsBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterFiltersCharacteristicsBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            binding = this
            title.text = GrandMarketApp.getInstance().keysLiveData.value?.brands ?: "brands"
            items.adapter = BrandsAdapter(searchProductModel, search).apply { submitList(brandModels) }
            line.isVisible = !isFirstPosition
            Log.i("PositionTag", "BrandsGroup -> $isFirstPosition")
        }
    }

    override fun getItemCount() = 1
    override fun reset() {
        binding.apply {
            items.adapter = BrandsAdapter(searchProductModel, search).apply { submitList(brandModels) }
        }
    }
}