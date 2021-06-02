package com.armboldmind.grandmarket.ui.more.orders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.databinding.AdapterOrderDetailsHeaderBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class OrdersDetailsHeaderAdapter : RecyclerView.Adapter<OrdersDetailsHeaderAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterOrderDetailsHeaderBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterOrderDetailsHeaderBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = 1
}