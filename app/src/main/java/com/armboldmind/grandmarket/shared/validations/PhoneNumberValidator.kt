package com.armboldmind.grandmarket.shared.validations

import androidx.core.text.isDigitsOnly

class PhoneNumberValidator : BaseValidator() {
    override fun isValid(value: CharSequence?): Boolean {
        return if (value == null) false else value.isNotEmpty() && when {
            value.startsWith("0") && value.isDigitsOnly() && value.length == 9 && value.toString() != "000000000" -> {
                true
            }
            value.startsWith("00374") && value.isDigitsOnly() && value.length == 13 && value.toString() != "0037400000000" -> {
                true
            }
            value.startsWith("374") && value.isDigitsOnly() && value.length == 11 && value.toString() != "37400000000" -> {
                true
            }
            value.startsWith("+374") && value.removePrefix("+")
                .isDigitsOnly() && value.length == 12 && value.toString() != "+37400000000" -> {
                true
            }
            value.length == 8 && value.isDigitsOnly() && value.toString() != "000000000" -> {
                true
            }
            else -> {
                false
            }

        }
    }

    fun isValidShortPhoneNumber(value: CharSequence?): Boolean {
        return if (value == null) false else value.isNotEmpty() && when {
            value.startsWith("0") && value.isDigitsOnly() && value.length == 9 && value.toString() != "000000000" -> {
                true
            }
            value.length == 8 && value.isDigitsOnly() && value.toString() != "00000000" -> {
                true
            }
            else -> {
                false
            }

        }
    }

    override fun getFullPhoneNumber(value: CharSequence): String {
        var phone = ""
        return when {
            value.startsWith("0") && value.isDigitsOnly() -> {
                phone = "+374${value.removePrefix("0")}"
                phone
            }
            value.startsWith("00374") && value.isDigitsOnly() -> {
                phone = "+${value.removePrefix("00")}"
                phone
            }
            value.startsWith("374") && value.isDigitsOnly() -> {
                phone = "+$value"
                phone
            }
            value.startsWith("+374") && value.removePrefix("+")
                .isDigitsOnly() -> {
                phone = value.toString()
                phone
            }

            else -> {
                phone = "+374$value"
                phone
            }
        }
    }

}