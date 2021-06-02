package com.armboldmind.grandmarket.shared.validations

import android.widget.EditText
import com.armboldmind.grandmarket.data.models.entity.Keys

class CodeValidator : BaseValidator() {
    override fun isValid(value: CharSequence?): Boolean {
        return !value?.trim()
                .isNullOrEmpty()
    }

    fun validate(keys: Keys, editText: EditText, showSnackBarError: (message: String) -> Unit, onValid: (code: String) -> Unit) {
        if (isValid(editText.text)) onValid.invoke(editText.text.toString())
        else {
            showError(editText)
            showSnackBarError.invoke(keys.verification_code_required)
        }
    }
}