package com.armboldmind.grandmarket.ui.auth.formValidators

import android.widget.EditText
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.validations.IValidator
import com.armboldmind.grandmarket.shared.validations.PasswordValidator
import com.armboldmind.grandmarket.shared.validations.PhoneNumberOrEmailValidator

class SignInFormValidator {

    private val mPasswordValidator: IValidator by lazy { PasswordValidator() }
    private val mPhoneNumberOrEmailValidator: IValidator by lazy { PhoneNumberOrEmailValidator() }

    fun isFormValid(keys: Keys, password: EditText, phoneOrEmail: EditText, showError: (message: String) -> Unit, onValidResult: (username: String, password: String) -> Unit) {
        val isPasswordValid = mPasswordValidator.isValid(password.text)
        val isPhoneOrEmailValid = mPhoneNumberOrEmailValidator.isValid(phoneOrEmail.text)
        if (!isPasswordValid) mPasswordValidator.showError(password)
        if (!isPhoneOrEmailValid) mPhoneNumberOrEmailValidator.showError(phoneOrEmail)
        if (isPasswordValid && isPhoneOrEmailValid) onValidResult.invoke(phoneOrEmail.text.toString(), password.text.toString())
        else showError.invoke(keys.incorrect_fields)

    }
}