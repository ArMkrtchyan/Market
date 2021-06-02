package com.armboldmind.grandmarket.di.modules.validators

import com.armboldmind.grandmarket.di.qualifiers.Email
import com.armboldmind.grandmarket.di.qualifiers.Password
import com.armboldmind.grandmarket.shared.validations.EmailValidator
import com.armboldmind.grandmarket.shared.validations.IValidator
import com.armboldmind.grandmarket.shared.validations.PasswordValidator
import dagger.Binds
import dagger.Module

@Module
abstract class ValidatorModule {

    @Binds
    @Password
    abstract fun bindPasswordValidator(passwordValidator: PasswordValidator): IValidator

    @Binds
    @Email
    abstract fun bindEmailValidator(emailValidator: EmailValidator): IValidator
}