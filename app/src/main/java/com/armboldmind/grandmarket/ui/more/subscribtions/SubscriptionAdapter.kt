package com.armboldmind.grandmarket.ui.more.subscribtions

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Subscription
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterSubscriptionsItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class SubscriptionAdapter(private val onItemClick: (Subscription) -> Unit) : BasePagingAdapter<Subscription, SubscriptionAdapter.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterSubscriptionsItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterSubscriptionsItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item -> holder.binding.root.setOnClickListener { onItemClick.invoke(item) } }
    }
}