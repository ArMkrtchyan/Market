package com.armboldmind.grandmarket.ui.more.requests.adapters

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.RequestProduct
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterRequestsItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData

class RequestsAdapter(private val onItemClick: (request: RequestProduct) -> Unit) : BasePagingAdapter<RequestProduct, RequestsAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterRequestsItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterRequestsItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            getItem(position)?.let { item ->
                keys = keysLiveData().value
                request = item
                root.setOnClickListener {
                    onItemClick.invoke(item)
                }
            }
        }
    }
}