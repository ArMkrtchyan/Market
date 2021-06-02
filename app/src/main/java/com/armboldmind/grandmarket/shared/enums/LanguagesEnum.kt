package com.armboldmind.grandmarket.shared.enums

enum class LanguagesEnum(val code: String, val languageName: String, val id: Int) {
    ARMENIAN("hy", "Հայերեն", 2),
    RUSSIAN("ru", "Русский", 3),
    ENGLISH("en", "English", 1),
    NONE("", "", -1),
}