package com.armboldmind.grandmarket.shared.formatters

import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import java.util.*

interface IFormatter {
    fun toServerIsoString(date: Date): String
    fun formatToPattern(date: Date, patternsEnum: DatePatternsEnum): String
    fun formatPatternToDate(date: String, patternsEnum: DatePatternsEnum): Date?
}