package com.armboldmind.grandmarket.ui.more.addresses.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.armboldmind.grandmarket.base.BaseBottomSheetDialogFragment
import com.armboldmind.grandmarket.data.models.domain.Address
import com.armboldmind.grandmarket.databinding.BottomSheetAddressEditDialogBinding
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.ui.more.addresses.AddressViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class EditAddressDialog(private val address: Address, private val mAddressViewModel: AddressViewModel) : BaseBottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return BottomSheetAddressEditDialogBinding.inflate(inflater, container, false)
            .apply {
                keysLiveData().observe(viewLifecycleOwner) {
                    lifecycleOwner = viewLifecycleOwner
                    keys = it
                    setAsDefault.isVisible = !address.isDefaultForDelivery
                    setAsDefault.setOnClickListener {
                        mAddressViewModel.sendIntent(ActionIntent.ShowDefaultDialog(address))
                        dismiss()
                    }
                    edit.setOnClickListener {
                        mAddressViewModel.sendIntent(ActionIntent.EditAddress(address))
                        dismiss()
                    }
                    delete.setOnClickListener {
                        mAddressViewModel.sendIntent(ActionIntent.ShowDeleteDialog(address))
                        dismiss()
                    }
                }
            }.root
    }
}