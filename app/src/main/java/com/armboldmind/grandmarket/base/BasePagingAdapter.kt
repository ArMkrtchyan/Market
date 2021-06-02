package com.armboldmind.grandmarket.base

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.armboldmind.grandmarket.databinding.AdapterLoadingBinding
import com.armboldmind.grandmarket.shared.globalextensions.gone
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.invisible
import com.armboldmind.grandmarket.shared.globalextensions.show

abstract class BasePagingAdapter<T : Any, VH : BaseViewHolder>(callback: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, VH>(callback) {

    class LoadStateAdapter(private val retry: () -> Unit) : androidx.paging.LoadStateAdapter<LoadStateAdapter.LoadStateViewHolder>() {
        inner class LoadStateViewHolder(private val mBinding: AdapterLoadingBinding) : BaseViewHolder(mBinding) {
            fun bind(loadState: LoadState) {
                mBinding.apply {
                    if (loadState is LoadState.Error) {
                        progress.gone()
                        error.show()
                        btn.setOnClickListener {
                            progress.show()
                            error.invisible()
                            retry.invoke()
                        }
                    }
                }
            }
        }

        override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
            holder.bind(loadState)
        }

        override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
            return LoadStateViewHolder(AdapterLoadingBinding.inflate(parent.context.inflater(), parent, false))
        }
    }

}
