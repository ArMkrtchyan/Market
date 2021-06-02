package com.armboldmind.grandmarket.ui.home.adapters

import android.graphics.Paint
import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterHomeBestsellersBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.load
import com.armboldmind.grandmarket.ui.products.ProductsViewModel

class BestSellersAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onFavoriteClick: (Product) -> Unit,
) : BaseAdapter<Product, BestSellersAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterHomeBestsellersBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterHomeBestsellersBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            getItem(position)?.let { item ->
                product = item
                oldPrice.paintFlags = oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                image.load(item.productCoverPhoto, shimmer)
                root.setOnClickListener { onItemClick.invoke(item) }
                favorite.setOnClickListener { onFavoriteClick.invoke(item) }
                if (!ProductsViewModel.favoritesMap.containsKey(item.productId)) {
                    ProductsViewModel.favoritesMap[item.productId] = item.favorite
                }
                ProductsViewModel.productLiveData.observeForever {
                    isFavorite = it[item.productId] ?: false
                }
            }
        }
    }
}