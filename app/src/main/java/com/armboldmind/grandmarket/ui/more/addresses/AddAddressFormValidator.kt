package com.armboldmind.grandmarket.ui.more.addresses

import android.widget.EditText
import com.armboldmind.grandmarket.shared.validations.FullNameValidator
import com.armboldmind.grandmarket.shared.validations.IValidator

class AddAddressFormValidator {
    private val mValidator: IValidator by lazy { FullNameValidator() }
    fun validate(address: EditText, onValidResult: (address: String) -> Unit) {
        val isAddressValid = mValidator.isValid(address.text)
        if (!isAddressValid) mValidator.showError(address)
        if (isAddressValid) onValidResult.invoke(address.text.toString())
    }
}