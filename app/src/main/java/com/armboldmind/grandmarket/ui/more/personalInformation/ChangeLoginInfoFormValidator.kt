package com.armboldmind.grandmarket.ui.more.personalInformation

import android.widget.EditText
import com.armboldmind.grandmarket.shared.validations.EmailValidator
import com.armboldmind.grandmarket.shared.validations.IValidator
import com.armboldmind.grandmarket.shared.validations.PhoneNumberValidator

class ChangeLoginInfoFormValidator {

    private val mEmailValidator: IValidator by lazy { EmailValidator() }
    private val mPhoneValidator by lazy { PhoneNumberValidator() }

    fun isPhoneFormValid(phone: EditText, onValidResult: (phone: String) -> Unit) {
        val isPhoneValid = mPhoneValidator.isValidShortPhoneNumber(phone.text)
        if (!isPhoneValid) mPhoneValidator.showError(phone)
        else onValidResult.invoke(mPhoneValidator.getFullPhoneNumber(phone.text))
    }

    fun isEmailFormValid(email: EditText, onValidResult: (email: String) -> Unit) {
        val isEmailValid = mEmailValidator.isValid(email.text)
        if (!isEmailValid) mEmailValidator.showError(email)
        else onValidResult.invoke(email.text.toString())
    }
}