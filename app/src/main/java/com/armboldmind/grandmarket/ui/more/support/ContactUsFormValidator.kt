package com.armboldmind.grandmarket.ui.more.support

import android.widget.EditText
import com.armboldmind.grandmarket.shared.validations.FullNameValidator
import com.armboldmind.grandmarket.shared.validations.IValidator
import com.armboldmind.grandmarket.shared.validations.PhoneNumberOrEmailValidator
import com.armboldmind.grandmarket.shared.validations.PhoneNumberValidator

class ContactUsFormValidator {
    private val mFullNameValidator: IValidator by lazy { FullNameValidator() }
    private val mPhoneNumberOrEmailValidator by lazy { PhoneNumberOrEmailValidator() }
    private val mPhoneNumberValidator: IValidator by lazy { PhoneNumberValidator() }
    fun isFormValid(
        fullName: EditText,
        phoneOrEmail: EditText,
        message: EditText,
        selectedOptionEdit: EditText,
        selectedOption: Int,
        onValidResult: (fullName: String, phone: String?, email: String?, message: String) -> Unit,
    ) {
        val isFullNameValid = mFullNameValidator.isValid(fullName.text)
        val isPhoneOrEmailValid = mPhoneNumberOrEmailValidator.isValid(phoneOrEmail.text)
        val isSelectedOptionValid = selectedOption != -1
        if (!isFullNameValid) mFullNameValidator.showError(fullName)
        if (message.text.isNullOrEmpty()) mFullNameValidator.showError(message)
        if (!isPhoneOrEmailValid) mPhoneNumberOrEmailValidator.showError(phoneOrEmail)
        if (!isSelectedOptionValid) mPhoneNumberOrEmailValidator.showError(selectedOptionEdit)
        if (isFullNameValid && isPhoneOrEmailValid && message.text.isNotEmpty() && isSelectedOptionValid) {
            val phone: String? = if (mPhoneNumberOrEmailValidator.isEmail(phoneOrEmail.text)) null else mPhoneNumberValidator.getFullPhoneNumber(phoneOrEmail.text)
            val email: String? = if (mPhoneNumberOrEmailValidator.isEmail(phoneOrEmail.text)) phoneOrEmail.text.toString() else null
            onValidResult.invoke(fullName.text.toString(), phone, email, message.text.toString())
        }
    }
}