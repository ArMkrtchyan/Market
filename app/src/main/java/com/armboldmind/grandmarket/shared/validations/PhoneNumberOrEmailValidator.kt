package com.armboldmind.grandmarket.shared.validations

class PhoneNumberOrEmailValidator : BaseValidator() {

    private val mEmailValidator by lazy { EmailValidator() }
    private val mPhoneNumberValidator by lazy { PhoneNumberValidator() }

    override fun isValid(value: CharSequence?): Boolean {
        return when {
            isEmail(value) -> mEmailValidator.isValid(value)
            else -> mPhoneNumberValidator.isValid(value)
        }
    }

    fun isEmail(value: CharSequence?): Boolean {
        return value?.contains("@") ?: false
    }

    fun returnValue(value: CharSequence): String {
        return when {
            isEmail(value) -> value.toString()
            else -> mPhoneNumberValidator.getFullPhoneNumber(value)
        }
    }
}