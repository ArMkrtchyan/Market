package com.armboldmind.grandmarket.ui.more.personalInformation

import android.widget.EditText
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import com.armboldmind.grandmarket.shared.formatters.Formatter
import com.armboldmind.grandmarket.shared.formatters.IFormatter
import com.armboldmind.grandmarket.shared.validations.FullNameValidator
import com.armboldmind.grandmarket.shared.validations.IValidator
import com.armboldmind.grandmarket.shared.validations.PasswordValidator

class PersonalInformationFormValidator {

    private val mPasswordValidator: IValidator by lazy { PasswordValidator() }
    private val mFullNameValidator: IValidator by lazy { FullNameValidator() }
    private val mDateFormatter: IFormatter by lazy { Formatter() }

    fun validateInfo(
        keys: Keys,
        fullName: EditText,
        dayOfBirth: EditText,
        showSnackBar: (message: String) -> Unit,
        onValidResult: (fullName: String, dateOfBirth: Long) -> Unit,
    ) {
        val isFullNameValid = mFullNameValidator.isValid(fullName.text)
        val date = mDateFormatter.formatPatternToDate(dayOfBirth.text.toString(), DatePatternsEnum.DAY_MONTH_YEAR)
        if (!isFullNameValid) mFullNameValidator.showError(fullName)
        if (date == null) mFullNameValidator.showError(dayOfBirth)
        if (isFullNameValid && date != null) onValidResult.invoke(fullName.text.toString(), date.time) else showSnackBar.invoke(keys.incorrect_fields)
    }

    fun validatePassword(
        keys: Keys,
        currentPassword: EditText,
        newPassword: EditText,
        repeatNewPassword: EditText,
        showSnackBar: (message: String) -> Unit,
        onValidResult: (currentPassword: String, password: String) -> Unit,
    ) {
        val isCurrentPasswordValid = mPasswordValidator.isValid(currentPassword.text)
        val isNewPasswordValid = mPasswordValidator.isValid(newPassword.text)
        val isRepeatPasswordValid = mPasswordValidator.isValid(repeatNewPassword.text)
        val isPasswordsMatch = mPasswordValidator.isPasswordMatches(newPassword.text, repeatNewPassword.text)
        if (!isCurrentPasswordValid) mPasswordValidator.showError(currentPassword)
        if (!isRepeatPasswordValid) mPasswordValidator.showError(repeatNewPassword)
        if (!isNewPasswordValid) mPasswordValidator.showError(newPassword)
        if (!isPasswordsMatch) {
            mPasswordValidator.showError(newPassword);mPasswordValidator.showError(repeatNewPassword)
            showSnackBar.invoke(keys.passwords_dont_match)
        }
        if (isNewPasswordValid && isPasswordsMatch) onValidResult.invoke(currentPassword.text.toString(), newPassword.text.toString())
        else showSnackBar.invoke(keys.incorrect_fields)
    }
}