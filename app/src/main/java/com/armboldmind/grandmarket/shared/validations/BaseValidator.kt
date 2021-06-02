package com.armboldmind.grandmarket.shared.validations

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.shared.globalextensions.getDrawableCompat

abstract class BaseValidator : IValidator {
    override fun isPasswordMatches(password: CharSequence, confirmPassword: CharSequence): Boolean {
        return false
    }

    override fun showError(editText: EditText) {
        editText.background = editText.context.getDrawableCompat(R.drawable.background_rounded_4_bordered_error)
        editText.addTextChangedListener {
            editText.background = editText.context.getDrawableCompat(R.drawable.background_rounded_4_bordered)
        }
    }

    override fun getFullPhoneNumber(value: CharSequence): String {
        return ""
    }
}