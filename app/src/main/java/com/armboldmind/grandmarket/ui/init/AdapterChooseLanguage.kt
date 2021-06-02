package com.armboldmind.grandmarket.ui.init

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterChooseLanguageItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class AdapterChooseLanguage(private val onItemClick: (language: Language) -> Unit) : BaseAdapter<Language, AdapterChooseLanguage.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterChooseLanguageItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterChooseLanguageItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { language ->
            holder.binding.language = language
            holder.binding.item.setOnClickListener {
                val newList = arrayListOf<Language>()
                currentList.map {
                    if (language.id == it.id) {
                        newList.add(it.copy(isSelected = true))
                    } else newList.add(it.copy(isSelected = false))
                }
                submitList(newList)
                onItemClick.invoke(language)
            }
        }
    }
}