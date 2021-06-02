package com.armboldmind.grandmarket.ui.more.requests

import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.validations.FullNameValidator
import com.armboldmind.grandmarket.shared.validations.IValidator
import com.armboldmind.grandmarket.shared.validations.PhoneNumberOrEmailValidator

class SendRequestFormValidator {
    private val mFullNameValidator: IValidator by lazy { FullNameValidator() }
    private val mPhoneNumberOrEmailValidator: PhoneNumberOrEmailValidator by lazy { PhoneNumberOrEmailValidator() }
    fun isFormValid(
        keys: Keys,
        fullName: AppCompatEditText,
        phoneOrEmail: EditText,
        mSelectedCategory: Long,
        category: AppCompatEditText,
        mSelectedBrand: Long,
        brand: AppCompatEditText,
        productName: AppCompatEditText, description: EditText, showError: (message: String) -> Unit,
        onValidResult: (fullName: String, contact: String, selectedCategory: Long, selectedBrand: Long, productName: String, description: String?) -> Unit,
    ) {
        val isPhoneOrEmailValid = mPhoneNumberOrEmailValidator.isValid(phoneOrEmail.text)
        val isFullNameValid = mFullNameValidator.isValid(fullName.text.toString()
                                                             .replace(" ", ""))
        if (!isFullNameValid) mFullNameValidator.showError(fullName)
        if (!isPhoneOrEmailValid) mPhoneNumberOrEmailValidator.showError(phoneOrEmail)
        if (mSelectedBrand == -1L) mPhoneNumberOrEmailValidator.showError(brand)
        if (mSelectedCategory == -1L) mPhoneNumberOrEmailValidator.showError(category)
        if (productName.text.isNullOrEmpty() || productName.text.toString().length <= 1) mPhoneNumberOrEmailValidator.showError(productName)
        if (description.text.isNullOrEmpty() || description.text.toString().length <= 1) mPhoneNumberOrEmailValidator.showError(description)
        if (isPhoneOrEmailValid && isFullNameValid && mSelectedCategory != -1L && mSelectedBrand != -1L && !productName.text.isNullOrEmpty() && productName.text.toString().length > 1 && !description.text.isNullOrEmpty() && description.text.toString().length > 1) onValidResult.invoke(
            fullName.text.toString(),
            mPhoneNumberOrEmailValidator.returnValue(phoneOrEmail.text),
            mSelectedCategory,
            mSelectedBrand,
            productName.text.toString(),
            description.text.toString())
        else showError.invoke(keys.incorrect_fields)
    }
}