package com.armboldmind.grandmarket.ui.more.orders

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Order
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterOrdersItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class OrdersAdapter(private val onItemClick: (Order) -> Unit) : BasePagingAdapter<Order, OrdersAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterOrdersItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterOrdersItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item -> holder.binding.root.setOnClickListener { onItemClick.invoke(item) } }
    }
}