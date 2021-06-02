package com.armboldmind.grandmarket.ui.home.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.databinding.AdapterViewAllBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class ViewAllAdapter(private val onItemClick: () -> Unit) : RecyclerView.Adapter<ViewAllAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterViewAllBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterViewAllBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.setOnClickListener { onItemClick.invoke() }
    }

    override fun getItemCount() = 1

}