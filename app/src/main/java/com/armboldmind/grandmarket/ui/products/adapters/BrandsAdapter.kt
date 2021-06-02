package com.armboldmind.grandmarket.ui.products.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Filters
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.data.models.requestmodels.SearchProductsModel
import com.armboldmind.grandmarket.databinding.AdapterSizesBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class BrandsAdapter(
    private val searchProductModel: SearchProductsModel,
    private val search: () -> Unit,
) : BaseAdapter<Filters.BrandModel, BrandsAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterSizesBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterSizesBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.size.apply {
                text = item.brandName
                if (item.chosen) searchProductModel.brandIds?.let { brandIds ->
                    brandIds.add(item.brandId)
                } ?: run {
                    arrayListOf<Long>().apply {
                        add(item.brandId)
                    }
                } else searchProductModel.brandIds?.let { brandIds ->
                    brandIds.remove(item.brandId)
                }
                search.invoke()
                if (item.chosen) setBackgroundResource(R.drawable.selected_rounded_stroke)
                else setBackgroundResource(R.drawable.unselected_rounded_stroke)
                setOnClickListener {
                    submitList(currentList.toMutableList()
                                       .apply {
                                           mapIndexed { index, model ->
                                               if (model.brandId == item.brandId) {
                                                   this[index] = model.copy(chosen = !model.chosen)
                                               }
                                           }
                                       })


                }
            }
        }
    }
}