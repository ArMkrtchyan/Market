package com.armboldmind.grandmarket.shared.validations

import android.widget.EditText

interface IValidator {
    fun isValid(value: CharSequence?): Boolean
    fun getFullPhoneNumber(value: CharSequence): String
    fun showError(editText: EditText)
    fun isPasswordMatches(password: CharSequence, confirmPassword: CharSequence): Boolean
}