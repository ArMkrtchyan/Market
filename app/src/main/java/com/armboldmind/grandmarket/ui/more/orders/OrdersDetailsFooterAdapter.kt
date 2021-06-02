package com.armboldmind.grandmarket.ui.more.orders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.databinding.AdapterOrderDetailsFooterBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class OrdersDetailsFooterAdapter(private val onButtonClick: () -> Unit) : RecyclerView.Adapter<OrdersDetailsFooterAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterOrderDetailsFooterBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterOrderDetailsFooterBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            footerButton.setOnClickListener { onButtonClick.invoke() }
        }
    }

    override fun getItemCount() = 1
}