package com.armboldmind.grandmarket.ui.more.addresses

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.databinding.AdapterAutocomplateAddressBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.yandex.mapkit.search.SuggestItem

class AdapterAutoComplete(private val onItemClick: (SuggestItem) -> Unit) : BaseAdapter<SuggestItem, AdapterAutoComplete.ViewHolder>(object : DiffUtil.ItemCallback<SuggestItem>() {
    override fun areItemsTheSame(oldItem: SuggestItem, newItem: SuggestItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: SuggestItem, newItem: SuggestItem): Boolean {
        return oldItem.title.text == newItem.title.text
    }

}) {
    inner class ViewHolder(val binding: AdapterAutocomplateAddressBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterAutocomplateAddressBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            address.text = getItem(position).title.text
            place.text = getItem(position).subtitle?.text ?: ""
            root.setOnClickListener { onItemClick.invoke(getItem(position)) }
        }
    }
}