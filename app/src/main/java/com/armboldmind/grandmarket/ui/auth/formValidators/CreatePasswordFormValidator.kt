package com.armboldmind.grandmarket.ui.auth.formValidators

import android.widget.EditText
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.validations.IValidator
import com.armboldmind.grandmarket.shared.validations.PasswordValidator

class CreatePasswordFormValidator {

    private val mPasswordValidator: IValidator by lazy { PasswordValidator() }

    fun isFormValid(
        keys: Keys,
        password: EditText,
        repeatPassword: EditText,
        showError: (message: String) -> Unit,
        onValidResult: (password: String, repeatPassword: String) -> Unit,
    ) {
        if (password.text.isNullOrEmpty() || repeatPassword.text.isNullOrEmpty()) {
            showError.invoke(keys.incorrect_fields)
            return
        }
        val isPasswordValid = mPasswordValidator.isValid(password.text)
        val isPasswordsMatch = mPasswordValidator.isPasswordMatches(password.text, repeatPassword.text)
        if (!isPasswordValid) {
            mPasswordValidator.showError(password)
            showError.invoke(keys.incorrect_password)
            return
        }
        if (!isPasswordsMatch) {
            mPasswordValidator.showError(password);mPasswordValidator.showError(repeatPassword)
            showError.invoke(keys.passwords_dont_match)
        }
        if (isPasswordValid && isPasswordsMatch) onValidResult.invoke(password.text.toString(), repeatPassword.text.toString())
    }
}