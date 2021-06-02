package com.armboldmind.grandmarket.ui.more.addresses

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Address
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterAddressItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class AdapterAddresses(private val onAddressClick: (address: Address) -> Unit) : BaseAdapter<Address, AdapterAddresses.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterAddressItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterAddressItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.address = getItem(position)
        holder.binding.addressName.maxLines = if (getItem(position).title.isEmpty()) 8 else 4
        holder.binding.root.setOnClickListener { onAddressClick.invoke(getItem(position)) }
    }
}