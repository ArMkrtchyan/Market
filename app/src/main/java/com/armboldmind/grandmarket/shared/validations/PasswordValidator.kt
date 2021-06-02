package com.armboldmind.grandmarket.shared.validations

import com.armboldmind.grandmarket.shared.enums.PatternsEnum
import javax.inject.Inject

class PasswordValidator @Inject constructor() : BaseValidator() {
    override fun isValid(value: CharSequence?): Boolean {
        return value?.isNotEmpty() ?: false && PatternsEnum.PASSWORD_PATTERN.pattern.matcher(value ?: "")
            .matches()
    }

    override fun isPasswordMatches(password: CharSequence, confirmPassword: CharSequence): Boolean {
        return password.toString() == confirmPassword.toString()
    }
}