package com.armboldmind.grandmarket.ui.more.support

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseBottomSheetDialogFragment
import com.armboldmind.grandmarket.databinding.BottomSheetTopicBinding
import com.armboldmind.grandmarket.shared.enums.MessageSubjectEnum
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class TopicBottomSheetDialog(private val mType: Int, private val block: (name: String, type: Int) -> Unit) : BaseBottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme).apply {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return BottomSheetTopicBinding.inflate(inflater, container, false)
            .apply {
                keysLiveData().observe(viewLifecycleOwner) { mKeys ->
                    lifecycleOwner = viewLifecycleOwner
                    keys = mKeys
                    type = mType
                    save.setOnClickListener {
                        when {
                            optionsTechSupport.isChecked -> block.invoke(mKeys.technical_support, MessageSubjectEnum.TECHNICAL_SUPPORT.type)
                            optionsProducts.isChecked -> block.invoke(mKeys.products, MessageSubjectEnum.PRODUCTS.type)
                            optionsCollaboration.isChecked -> block.invoke(mKeys.collaboration, MessageSubjectEnum.COLLABORATION.type)
                            optionsAdvertisingProposal.isChecked -> block.invoke(mKeys.advertising_proposal, MessageSubjectEnum.ADVERTISING_PROPOSAL.type)
                            optionsReview.isChecked -> block.invoke(mKeys.review, MessageSubjectEnum.REVIEW.type)
                            optionsCommentsAndSuggestions.isChecked -> block.invoke(mKeys.comments_and_suggestions, MessageSubjectEnum.COMMENTS_AND_SUGGESTIONS.type)
                            optionsOther.isChecked -> block.invoke(mKeys.other, MessageSubjectEnum.OTHER.type)
                        }
                        dismiss()
                    }
                }
            }.root
    }

}