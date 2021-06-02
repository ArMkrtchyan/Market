package com.armboldmind.grandmarket.shared.enums

enum class DatePatternsEnum(val pattern: String) {
    UTC("yyyy-MM-dd'T'HH:mm:ss"),
    DAY_MONTH_YEAR("dd MMMM yyyy"),
    DAY_MONTH_YEAR_NUMBER("dd/MM/yyyy"),
    DAY_MON_YEAR("dd MMM yyyy"),
    DAY_MONTH_YEAR_TIME_PATTERN("d MMM yyyy HH:mm"),
    DAY_MONTH_YEAR_TIME_PATTERN2("dd.MM.yyyy HH:mm"),
    DAY_MONTH_TIME_PATTERN("d MMM HH:mm"),
    HOUR_MINUTE_TIME_PATTERN("HH:mm"),
    MOUNT_YEAR_PATTERN("MMM, yyyy"),
    WEEK_DAY_MOUNT_PATTERN("EEEE d MMM"),
    MOUNT_DAY_YEAR_PATTERN("MMMM d yyyy"),
    SERVER_ISO_PATTERN("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
}
