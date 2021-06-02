package com.armboldmind.grandmarket.ui.more.notifications

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Notification
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterNotificationsItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.getDrawableCompat
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class NotificationsAdapter(private val onItemClick: (action: Int, actionId: Long) -> Unit) : BasePagingAdapter<Notification, NotificationsAdapter.ViewHolder>(
    getDiffCallback()) {
    inner class ViewHolder(private val binding: AdapterNotificationsItemBinding) : BaseViewHolder(binding) {
        fun bind(item: Notification) {
            binding.notification = item
            binding.icon.setImageResource(item.icon)
            binding.icon.background = mContext.getDrawableCompat(item.background)
            binding.root.setOnClickListener { onItemClick.invoke(item.action, item.actionId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterNotificationsItemBinding.inflate(parent.context.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}