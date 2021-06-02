package com.armboldmind.grandmarket.ui.more.informative

import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.armboldmind.grandmarket.base.BasePagingAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.NewsAndEvents
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterNewsAndEventsBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.load
import com.armboldmind.grandmarket.shared.globalextensions.toTransitionGroup

class NewsAndEventsAdapter(private val onItemClick: (NewsAndEvents, FragmentNavigator.Extras) -> Unit) : BasePagingAdapter<NewsAndEvents, NewsAndEventsAdapter.ViewHolder>(
    getDiffCallback()) {
    inner class ViewHolder(val binding: AdapterNewsAndEventsBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterNewsAndEventsBinding.inflate(parent.inflater(), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.apply {
                newsAndEvents = item
                image.load(item.newsCoverPhoto, shimmer)
                root.setOnClickListener {
                    val extras = FragmentNavigatorExtras(date.toTransitionGroup(), title.toTransitionGroup(), image.toTransitionGroup())
                    onItemClick.invoke(item, extras)
                }
            }
        }
    }
}

