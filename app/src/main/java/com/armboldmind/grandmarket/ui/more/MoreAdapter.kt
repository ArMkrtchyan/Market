package com.armboldmind.grandmarket.ui.more

import android.util.Log
import android.view.ViewGroup
import androidx.navigation.NavDirections
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.More
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterMoreItemBinding
import com.armboldmind.grandmarket.databinding.AdapterMoreLineItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.show

class MoreAdapter(private val onMoreClick: (NavDirections?) -> Unit) : BaseAdapter<More, BaseViewHolder>(getDiffCallback()) {
    companion object {
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_LINE = 2
    }

    inner class MoreViewHolder(private val mBinding: AdapterMoreItemBinding) : BaseViewHolder(mBinding) {
        fun bind(item: More) {
            mBinding.icon.setImageResource(item.icon)
            mBinding.title.text = item.title
            mBinding.root.setOnClickListener { onMoreClick.invoke(item.action) }
            item.badgeCount.observeForever { count ->
                Log.i("CountTag", count.toString())
                count?.let { if (it > 0) mBinding.badge.apply { show();text = it.toString() } }
            }
        }
    }

    inner class LineViewHolder(private val mBinding: AdapterMoreLineItemBinding) : BaseViewHolder(mBinding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) MoreViewHolder(AdapterMoreItemBinding.inflate(parent.context.inflater(), parent, false)) else LineViewHolder(
            AdapterMoreLineItemBinding.inflate(parent.context.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is MoreViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).id != -1) VIEW_TYPE_ITEM else VIEW_TYPE_LINE
    }
}