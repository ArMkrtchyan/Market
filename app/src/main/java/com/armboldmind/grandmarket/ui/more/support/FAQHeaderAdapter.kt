package com.armboldmind.grandmarket.ui.more.support

import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.databinding.AdapterFaqHeaderBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class FAQHeaderAdapter(private val loadingLiveData: LiveData<Boolean>, private val onTextChange: (String) -> Unit) : RecyclerView.Adapter<FAQHeaderAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterFaqHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterFaqHeaderBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.search.doAfterTextChanged {
            onTextChange.invoke(it.toString())
        }
    }

    override fun getItemCount() = 1
}