package com.armboldmind.grandmarket.shared.enums

import java.util.regex.Pattern

enum class PatternsEnum(val pattern: Pattern) {
    NAME_PATTERN(Pattern.compile("^[\\p{L}.-]{3,30}$")),
    PHONE_NUMBER_PATTERN(Pattern.compile("^[0-9+]{0,1}+[0-9]{6}\$")),
    PASSWORD_PATTERN(Pattern.compile(".{6,20}")),
    MESSAGE_PATTERN(Pattern.compile(".{5,}")),
    FEEDBACK_PATTERN(Pattern.compile(".{5,400}")),
    COMMENT_PATTERN(Pattern.compile(".{3,50}")),
    SUBJECT_PATTERN(Pattern.compile(".{5,}")),
    EMAIL_PATTERN(Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")),
    VEHICLE_NUMBER_PATTERN(Pattern.compile("^[a-zA-Z0-9]{3,10}")),
    COMPANY_NAME_PATTERN(Pattern.compile("^[\\p{L}0-9.-.,]{3,30}$")),
    TAX_NUMBER_PATTERN(Pattern.compile("^[0-9]{6,10}"))
}
