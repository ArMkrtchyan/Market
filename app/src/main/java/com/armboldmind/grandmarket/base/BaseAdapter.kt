package com.armboldmind.grandmarket.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<T, VH : BaseViewHolder>(callback: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(callback)