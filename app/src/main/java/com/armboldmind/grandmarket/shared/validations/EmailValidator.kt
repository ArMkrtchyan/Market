package com.armboldmind.grandmarket.shared.validations

import com.armboldmind.grandmarket.shared.enums.PatternsEnum
import javax.inject.Inject

class EmailValidator @Inject constructor() : BaseValidator() {
    override fun isValid(value: CharSequence?): Boolean {
        return value?.isNotEmpty() ?: false && PatternsEnum.EMAIL_PATTERN.pattern.matcher(value ?: "")
            .matches()
    }
}