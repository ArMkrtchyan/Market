package com.armboldmind.grandmarket.ui.basket

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterBasketItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class BasketAdapter(private val onItemClick: (Product) -> Unit) : BaseAdapter<Product, BasketAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterBasketItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterBasketItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply { // oldPrice.paintFlags = oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            getItem(position)?.let { item -> root.setOnClickListener { onItemClick.invoke(item) } }
        }
    }
}