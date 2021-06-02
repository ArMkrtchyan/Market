package com.armboldmind.grandmarket.ui.auth.formValidators

import android.widget.EditText
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import com.armboldmind.grandmarket.shared.formatters.Formatter
import com.armboldmind.grandmarket.shared.formatters.IFormatter
import com.armboldmind.grandmarket.shared.validations.EmailValidator
import com.armboldmind.grandmarket.shared.validations.FullNameValidator
import com.armboldmind.grandmarket.shared.validations.IValidator
import com.armboldmind.grandmarket.shared.validations.PhoneNumberValidator

class SignUpFormValidator {

    private val mFullNameValidator: IValidator by lazy { FullNameValidator() }
    private val mEmailValidator: IValidator by lazy { EmailValidator() }
    private val mPhoneValidator by lazy { PhoneNumberValidator() }
    private val mDateFormatter: IFormatter by lazy { Formatter() }

    fun isPhoneFormValid(
        keys: Keys,
        fullName: EditText,
        dayOfBirth: EditText,
        phone: EditText,
        showError: (message: String) -> Unit,
        onValidResult: (fullName: String, dateOfBirth: Long, phone: String) -> Unit,
    ) {
        fullName.setText(fullName.text.toString()
                             .trimStart()
                             .trimEnd())
        val isFullNameValid = mFullNameValidator.isValid(fullName.text.toString()
                                                             .replace(" ", ""))
        val isPhoneValid = mPhoneValidator.isValidShortPhoneNumber(phone.text)
        val date = mDateFormatter.formatPatternToDate(dayOfBirth.text.toString(), DatePatternsEnum.DAY_MONTH_YEAR)
        if (!isFullNameValid) mFullNameValidator.showError(fullName)
        if (!isPhoneValid) mPhoneValidator.showError(phone)
        if (date == null) mPhoneValidator.showError(dayOfBirth)
        if (isFullNameValid && isPhoneValid && date != null) onValidResult.invoke(fullName.text.toString(), date.time, mPhoneValidator.getFullPhoneNumber(phone.text))
        else showError.invoke(keys.incorrect_fields)
    }

    fun isEmailFormValid(
        keys: Keys,
        fullName: EditText,
        dayOfBirth: EditText,
        email: EditText,
        showError: (message: String) -> Unit,
        onValidResult: (fullName: String, dateOfBirth: Long, email: String) -> Unit,
    ) {
        fullName.setText(fullName.text.toString()
                             .trimStart()
                             .trimEnd())
        val isFullNameValid = mFullNameValidator.isValid(fullName.text)
        val isEmailValid = mEmailValidator.isValid(email.text)
        val date = mDateFormatter.formatPatternToDate(dayOfBirth.text.toString(), DatePatternsEnum.DAY_MONTH_YEAR)
        if (!isFullNameValid) mFullNameValidator.showError(fullName)
        if (!isEmailValid) mEmailValidator.showError(email)
        if (date == null) mPhoneValidator.showError(dayOfBirth)
        if (isFullNameValid && isEmailValid && date != null) onValidResult.invoke(fullName.text.toString(), date.time, email.text.toString())
        else showError.invoke(keys.incorrect_fields)
    }
}