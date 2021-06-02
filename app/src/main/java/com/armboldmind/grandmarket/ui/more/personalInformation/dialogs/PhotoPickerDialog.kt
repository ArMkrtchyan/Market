package com.armboldmind.grandmarket.ui.more.personalInformation.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.armboldmind.grandmarket.base.BaseBottomSheetDialogFragment
import com.armboldmind.grandmarket.databinding.BottomSheetPhotoPickerDialogBinding
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.google.android.material.bottomsheet.BottomSheetDialog

class PhotoPickerDialog(private val userImage: String, private val setViewState: (ViewState) -> Unit) : BaseBottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return BottomSheetPhotoPickerDialogBinding.inflate(inflater, container, false)
            .apply {
                keysLiveData().observe(viewLifecycleOwner) {
                    lifecycleOwner = viewLifecycleOwner
                    keys = it
                    fromGallery.setOnClickListener {
                        setViewState.invoke(ViewState.OpenGallery)
                        dismiss()
                    }
                    fromCamera.setOnClickListener {
                        setViewState.invoke(ViewState.OpenCamera)
                        dismiss()
                    }
                    deletePhoto.isVisible = userImage.isNotEmpty()
                    deletePhoto.setOnClickListener {
                        setViewState.invoke(ViewState.DeletePhoto)
                        dismiss()
                    }
                }
            }.root
    }

}