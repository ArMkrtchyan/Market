package com.armboldmind.grandmarket.ui.more.requests.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseBottomSheetDialogFragment
import com.armboldmind.grandmarket.data.models.domain.RadioButtonModel
import com.armboldmind.grandmarket.databinding.BottomSheetListBinding
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.ui.more.requests.adapters.RadioButtonAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class CategoriesBrandsBottomSheetDialog(private val mTitle: String, private val mItems: List<RadioButtonModel>, private val onItemClick: (RadioButtonModel) -> Unit) :
    BaseBottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return BottomSheetListBinding.inflate(inflater, container, false)
            .apply {
                keysLiveData().observe(viewLifecycleOwner) { mKeys ->
                    lifecycleOwner = viewLifecycleOwner
                    keys = mKeys
                    title.text = mTitle
                    items.adapter = RadioButtonAdapter { item ->
                        setOnSaveClickListener(this, item)
                    }.apply { submitList(mItems) }
                    setOnSaveClickListener(this, mItems[0])
                }
            }.root
    }

    private fun setOnSaveClickListener(binding: BottomSheetListBinding, radioButtonModel: RadioButtonModel) {
        binding.save.setOnClickListener {
            onItemClick.invoke(radioButtonModel)
            dismiss()
        }
    }
}