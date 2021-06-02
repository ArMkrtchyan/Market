package com.armboldmind.grandmarket

import com.armboldmind.grandmarket.shared.validations.EmailValidator
import com.armboldmind.grandmarket.shared.validations.IValidator
import com.armboldmind.grandmarket.shared.validations.PasswordValidator
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal class ValidatorTest {

    private var passwordValidator: IValidator = PasswordValidator()
    private var emailValidator: IValidator = EmailValidator()

    @TestFactory
    @DisplayName("passwordValidatorTest")
    fun passwordValidatorTest() = listOf("Test123!" to true,
                                         "#tesT12!" to true,
                                         "12Es@t123" to true,
                                         "test123!" to true,
                                         "tes t123!" to true,
                                         "t " to false,
                                         "   " to false,
                                         null to false,
                                         "" to false,
                                         "123456789123456789123" to false).map { (password, expected) ->
        dynamicTest("given \"$password\", when validating the password, then it should be reported as ${if (expected) "valid" else "invalid"}") {
            assertThat(passwordValidator.isValid(password), equalTo(expected))
        }
    }

    @TestFactory
    @DisplayName("passwordMatchesValidatorTest")
    fun passwordsMatchesTest() = listOf(Triple("Password1", "Password1", true),
                                        Triple("test123456", "test123456", true),
                                        Triple("test1234", "test123456", false),
                                        Triple("test4", "te23456", false)).map { (password, confirmPassword, expected) ->
        dynamicTest("given \"$password\" and \"$confirmPassword\" , when validating the passwords match, then it should be reported as ${if (expected) "valid" else "invalid"}") {
            assertThat(passwordValidator.isPasswordMatches(password, confirmPassword), equalTo(expected))
        }
    }


    @TestFactory
    @DisplayName("emailValidatorTest")
    fun emailValidatorTest() = listOf("arshak9292@gmail.com" to true,
                                      "arshak9292@gmail.com" to true,
                                      "arshak9292@gma@il.com" to false,
                                      "arshak9292@gmailcom" to false,
                                      "arshak9292gmail.com" to false,
                                      "arshak9292.gmail.com" to false,
                                      "arshak9292@@gmail.com" to false,
                                      "ars.hak@9292gmai@l.com" to false,
                                      "arsha.k9.292@gmail.com" to true,
                                      "arsha@k9292gmail@.com@" to false,
                                      "   " to false,
                                      null to false,
                                      "" to false).map { (email, expected) ->
        dynamicTest("given \"$email\", when validating the email, then it should be reported as ${if (expected) "valid" else "invalid"}") {
            assertThat(emailValidator.isValid(email), equalTo(expected))
        }
    }

}