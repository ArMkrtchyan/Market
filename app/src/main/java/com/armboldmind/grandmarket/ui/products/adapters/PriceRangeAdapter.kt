package com.armboldmind.grandmarket.ui.products.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.GrandMarketApp
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.requestmodels.SearchProductsModel
import com.armboldmind.grandmarket.databinding.AdapterFilterPriceRangeBinding
import com.armboldmind.grandmarket.shared.globalextensions.format
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.onStopTrackingTouch

class PriceRangeAdapter(
    private val filters: Filters,
    private val searchProductModel: SearchProductsModel,
    private val search: () -> Unit,
) : RecyclerView.Adapter<PriceRangeAdapter.ViewHolder>(), Reset {
    inner class ViewHolder(val binding: AdapterFilterPriceRangeBinding) : BaseViewHolder(binding)

    private lateinit var binding: AdapterFilterPriceRangeBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterFilterPriceRangeBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            binding = this
            keys = GrandMarketApp.getInstance().keysLiveData.value
            slide.apply {
                valueFrom = filters.priceFrom.toFloat()
                valueTo = filters.priceTo.toFloat()
                values = listOf((searchProductModel.priceFrom ?: filters.priceFrom).toFloat(), (searchProductModel.priceTo ?: filters.priceTo).toFloat())
                setLabelFormatter { value: Float -> value.format() }
                onStopTrackingTouch { from, to ->
                    searchProductModel.priceFrom = from
                    searchProductModel.priceTo = to
                    search.invoke()
                }
            }
            startPrice.text = filters.priceFrom.format()
            endPrice.text = filters.priceTo.format()
        }
    }

    override fun getItemCount() = 1
    override fun reset() {
        binding.slide.apply {
            searchProductModel.priceFrom = null
            searchProductModel.priceTo = null
            values = listOf((searchProductModel.priceFrom ?: filters.priceFrom).toFloat(), (searchProductModel.priceTo ?: filters.priceTo).toFloat())
        }
    }
}