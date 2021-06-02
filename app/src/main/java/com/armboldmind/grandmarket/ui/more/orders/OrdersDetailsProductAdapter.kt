package com.armboldmind.grandmarket.ui.more.orders

import android.graphics.Paint
import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Product
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterOrderDetailsProductItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class OrdersDetailsProductAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onRateClick: (Product) -> Unit,
) : BaseAdapter<Product, OrdersDetailsProductAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterOrderDetailsProductItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterOrderDetailsProductItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            getItem(position)?.let { item -> root.setOnClickListener { onItemClick.invoke(item) } }
            rate.paintFlags = rate.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            getItem(position)?.let { item -> rate.setOnClickListener { onRateClick.invoke(item) } }
        }
    }
}