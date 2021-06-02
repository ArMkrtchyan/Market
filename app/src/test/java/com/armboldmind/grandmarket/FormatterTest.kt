package com.armboldmind.grandmarket

import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import com.armboldmind.grandmarket.shared.formatters.Formatter
import com.armboldmind.grandmarket.shared.formatters.IFormatter
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.*

class FormatterTest {
    private val formatter: IFormatter = Formatter()

    @TestFactory
    @DisplayName("Date patterns formatter test")
    fun `Date patterns formatter test`() = listOf(Triple(Date(1616594371617), DatePatternsEnum.DAY_MONTH_YEAR, "24 марта 2021"),
                                                  Triple(Date(1616594371617), DatePatternsEnum.DAY_MONTH_YEAR_NUMBER, "24/03/2021"),
                                                  Triple(Date(1616594371617), DatePatternsEnum.DAY_MON_YEAR, "24 мар 2021"),
                                                  Triple(Date(1616594371617), DatePatternsEnum.DAY_MONTH_YEAR_TIME_PATTERN, "24 мар 2021 13:59"),
                                                  Triple(Date(1616594371617), DatePatternsEnum.DAY_MONTH_TIME_PATTERN, "24 мар 13:59"),
                                                  Triple(Date(1616594371617), DatePatternsEnum.HOUR_MINUTE_TIME_PATTERN, "13:59"),
                                                  Triple(Date(1616594371617), DatePatternsEnum.MOUNT_YEAR_PATTERN, "марта 2021"),
                                                  Triple(Date(1616594371617), DatePatternsEnum.WEEK_DAY_MOUNT_PATTERN, "среда 24 мар")).map { (date, pattern, expected) ->
        DynamicTest.dynamicTest("given \"$date\", when formatting to  \"$pattern\", then it should be formatted as \"$expected\"") {
            MatcherAssert.assertThat(formatter.formatToPattern(date, pattern), Matchers.equalTo(expected))
        }
    }

    @TestFactory
    @DisplayName("Date to server iso string test")
    fun `Date to server iso string test`() = listOf(Triple(Date(1616594371617), "2021-03-24T13:59:31.617Z", true),
                                                    Triple(Date(1616594372617), "2021-03-24T13:59:32.617Z", true),
                                                    Triple(Date(1616594371617), "2021-03-24T13:59:36.617Z", false),
                                                    Triple(Date(1616594371617), "2021-03-24T13:59:38.617Z", false)).map { (date, pattern, expected) ->
        DynamicTest.dynamicTest("given \"$date\", when formatting to  server iso date, then it should  ${if (expected) "to be match" else "not to be match"} with \"$pattern\"") {
            MatcherAssert.assertThat(formatter.toServerIsoString(date) == pattern, Matchers.equalTo(expected))
        }
    }
}