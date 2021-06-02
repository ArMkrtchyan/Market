package com.armboldmind.grandmarket.ui.auth.formValidators

import android.widget.EditText
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.validations.PhoneNumberOrEmailValidator

class ForgotPasswordFormValidator {

    private val mPhoneNumberOrEmailValidator: PhoneNumberOrEmailValidator by lazy { PhoneNumberOrEmailValidator() }

    fun isFormValid(keys: Keys, phoneOrEmail: EditText, showError: (message: String) -> Unit, onValidResult: (String, Boolean) -> Unit) {
        if (phoneOrEmail.text.isNullOrEmpty()) showError.invoke(keys.email_or_phone_required)
        val isPhoneOrEmailValid = mPhoneNumberOrEmailValidator.isValid(phoneOrEmail.text)
        if (!isPhoneOrEmailValid) {
            mPhoneNumberOrEmailValidator.showError(phoneOrEmail)
            showError.invoke(if (mPhoneNumberOrEmailValidator.isEmail(phoneOrEmail.text)) keys.incorrect_email else keys.incorrect_phone)
        } else onValidResult.invoke(mPhoneNumberOrEmailValidator.returnValue(phoneOrEmail.text), mPhoneNumberOrEmailValidator.isEmail(phoneOrEmail.text))
    }
}