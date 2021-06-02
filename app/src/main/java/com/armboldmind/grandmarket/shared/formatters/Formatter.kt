package com.armboldmind.grandmarket.shared.formatters

import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import java.text.SimpleDateFormat
import java.util.*

class Formatter : IFormatter {
    override fun toServerIsoString(date: Date): String {
        val sdf = SimpleDateFormat(DatePatternsEnum.SERVER_ISO_PATTERN.pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    override fun formatToPattern(date: Date, patternsEnum: DatePatternsEnum): String {
        return SimpleDateFormat(patternsEnum.pattern, Locale.getDefault()).apply { }
            .format(date)
    }

    override fun formatPatternToDate(date: String, patternsEnum: DatePatternsEnum): Date? {
        return try {
            SimpleDateFormat(patternsEnum.pattern, Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }
                .parse(date)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}