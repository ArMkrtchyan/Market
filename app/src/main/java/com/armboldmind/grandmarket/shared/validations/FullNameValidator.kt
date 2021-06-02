package com.armboldmind.grandmarket.shared.validations

import javax.inject.Inject

class FullNameValidator @Inject constructor() : BaseValidator() {
    override fun isValid(value: CharSequence?): Boolean {
        return !value.isNullOrEmpty() && value.length > 2
    }
}