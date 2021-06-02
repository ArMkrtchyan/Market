package com.armboldmind.grandmarket.ui.more.support

import android.text.Html
import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.domain.FAQ
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterFaqAnswerBinding
import com.armboldmind.grandmarket.databinding.AdapterFaqLineBinding
import com.armboldmind.grandmarket.databinding.AdapterFaqQuestionBinding
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.utils.AnimationUtil

class FAQBodyAdapter : BaseAdapter<FAQ, BaseViewHolder>(getDiffCallback()) {
    companion object {
        const val VIEW_TYPE_QUESTION = 1
        const val VIEW_TYPE_LINE = 2
        const val VIEW_TYPE_ANSWER = 3
    }

    private var closeAll = true
    private val answers = arrayListOf<FAQ>()

    inner class ViewHolderQuestion(val binding: AdapterFaqQuestionBinding) : BaseViewHolder(binding)
    inner class ViewHolderLine(val binding: AdapterFaqLineBinding) : BaseViewHolder(binding)
    inner class ViewHolderAnswer(val binding: AdapterFaqAnswerBinding) : BaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_QUESTION -> ViewHolderQuestion(AdapterFaqQuestionBinding.inflate(parent.inflater(), parent, false))
            VIEW_TYPE_LINE -> ViewHolderLine(AdapterFaqLineBinding.inflate(parent.inflater(), parent, false))
            else -> ViewHolderAnswer(AdapterFaqAnswerBinding.inflate(parent.inflater(), parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ViewHolderQuestion -> {
                item.isOpened.observeForever {
                    if (it) AnimationUtil.rotate(holder.binding.arrow, 90f)
                    else AnimationUtil.rotate(holder.binding.arrow, -90f)
                }
                holder.binding.question.text = item.question
                holder.binding.root.setOnClickListener {
                    var realPosition = 0
                    currentList.mapIndexed { index, faq ->
                        if (item.id == faq.id) realPosition = index
                    }
                    val nextItem = getItem(realPosition + 1)
                    if (nextItem.parentId != item.id) answers.map {
                        if (it.parentId == item.id) {
                            item.isOpened.postValue(true)
                            closeAll = false
                            submitList(currentList.toMutableList()
                                           .apply { add(realPosition + 1, it) })
                            return@setOnClickListener
                        }
                    } else {
                        item.isOpened.postValue(false)
                        closeAll = false
                        submitList(currentList.toMutableList()
                                       .apply { removeAt(realPosition + 1) })
                    }
                }
            }
            is ViewHolderLine -> Unit
            is ViewHolderAnswer -> holder.binding.answer.text =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) Html.fromHtml(item.answer, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(item.answer)
        }
    }

    private fun closeAllItems(list: List<FAQ>?) {
        if (closeAll) {
            list?.map {
                it.isOpened.value = false
            }
        } else closeAll = true
    }

    override fun submitList(list: List<FAQ>?) {
        closeAllItems(list)
        super.submitList(list)

    }

    override fun submitList(list: MutableList<FAQ>?, commitCallback: Runnable?) {
        closeAllItems(list)
        super.submitList(list, commitCallback)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.parentId) {
            -1L -> {
                VIEW_TYPE_QUESTION
            }
            -2L -> {
                VIEW_TYPE_LINE
            }
            else -> {
                VIEW_TYPE_ANSWER
            }
        }
    }

    fun setAnswers(list: List<FAQ>) {
        answers.clear()
        answers.addAll(list)
    }
}