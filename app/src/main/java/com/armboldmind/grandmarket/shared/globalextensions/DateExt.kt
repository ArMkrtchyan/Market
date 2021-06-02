package com.armboldmind.grandmarket.shared.globalextensions

import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import java.text.SimpleDateFormat
import java.util.*

// TODO: 12/29/2020 change all hardcoded locales
fun Date.toServerDate(pattern: String = DatePatternsEnum.UTC.pattern): String {
    val sdf = SimpleDateFormat(pattern, Locale("ru"))
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return try {
        sdf.format(this)
    } catch (e: Throwable) {
        e.printStackTrace()
        ""
    }
}

fun Date.toServerDateWithoutTimeZone(pattern: String = DatePatternsEnum.UTC.pattern): String {
    val sdf = SimpleDateFormat(pattern, Locale("ru"))
    return try {
        sdf.format(this)
    } catch (e: Throwable) {
        e.printStackTrace()
        ""
    }
}

fun String.convertServerDateTo(pattern: () -> String): String? {
    val simpleDateFormat = SimpleDateFormat(DatePatternsEnum.UTC.pattern, Locale("ru"))
    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")

    val convertedDateFormat = SimpleDateFormat(pattern(), Locale("ru"))
    convertedDateFormat.timeZone = TimeZone.getDefault()

    return simpleDateFormat.parse(this)
        ?.let {
            convertedDateFormat.format(it)
        }
}

fun String?.convertFrom(pattern: () -> String): Date {
    val sdf = SimpleDateFormat(pattern(), Locale("ru"))
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return if (this != null) {
        sdf.parse(this)
            ?.let {
                it
            } ?: throw Exception("Date $this is not valid")
    } else throw Exception("Date $this is not valid")
}

fun String?.convertFromWithoutTimeZone(pattern: () -> String): Date {
    val sdf = SimpleDateFormat(pattern(), Locale("ru"))
    return if (this != null) {
        sdf.parse(this)
            ?.let {
                it
            } ?: throw Exception("Date $this is not valid")
    } else throw Exception("Date $this is not valid")
}

fun Date.convertTo(pattern: () -> String): String {
    val sdf = SimpleDateFormat(pattern(), Locale("ru"))
    return try {
        sdf.format(this)
    } catch (e: Throwable) {
        e.printStackTrace()
        ""
    }
}