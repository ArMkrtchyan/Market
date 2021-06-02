package com.armboldmind.grandmarket.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder(view: ViewBinding) : RecyclerView.ViewHolder(view.root) {
    protected val mContext = view.root.context
}
