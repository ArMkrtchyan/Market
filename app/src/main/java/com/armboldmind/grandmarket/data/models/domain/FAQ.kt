package com.armboldmind.grandmarket.data.models.domain

import androidx.lifecycle.MutableLiveData
import com.armboldmind.gsport24.root.utils.DifItem

data class FAQ(val id: Long = 0, val parentId: Long = -1, val question: String = "", val answer: String = "", var isOpened: MutableLiveData<Boolean> = MutableLiveData(false)) :
    DifItem<FAQ> {
    override fun areItemsTheSame(second: FAQ): Boolean {
        return id == second.id
    }

    override fun areContentsTheSame(second: FAQ): Boolean {
        return id == second.id && parentId == second.parentId && question == second.question && answer == second.answer && isOpened == second.isOpened
    }

}