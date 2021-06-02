package com.armboldmind.grandmarket.ui.more.settings

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterSettingsLanguageItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.getDrawableCompat
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class AdapterSettingsLanguage(private val onLanguageClick: (Language) -> Unit) : BaseAdapter<Language, AdapterSettingsLanguage.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterSettingsLanguageItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterSettingsLanguageItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            getItem(position)?.let { item ->
                name.text = item.name
                name.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, if (item.isSelected) root.context.getDrawableCompat(R.drawable.ic_check) else null, null)
                line.isVisible = position != currentList.size - 1
                root.setOnClickListener {
                    if (!item.isSelected) {
                        val newList = arrayListOf<Language>()
                        currentList.map {
                            if (item.id == it.id) {
                                newList.add(it.copy(isSelected = true))
                            } else newList.add(it.copy(isSelected = false))
                        }
                        submitList(newList)
                        onLanguageClick.invoke(item)
                    }
                }
            }
        }
    }
}