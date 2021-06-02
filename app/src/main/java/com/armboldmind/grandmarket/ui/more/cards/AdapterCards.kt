package com.armboldmind.grandmarket.ui.more.cards

import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.Card
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterCardItemBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater

class AdapterCards(private val onCardDelete: (card: Card) -> Unit) : BaseAdapter<Card, AdapterCards.ViewHolder>(getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterCardItemBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterCardItemBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card = getItem(position)
        holder.binding.deleteCard.setOnClickListener { onCardDelete.invoke(getItem(position)) }
    }
}