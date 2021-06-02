package com.armboldmind.grandmarket.shared.enums

enum class NotificationSubscriptionsEnum(val type: Int) {
    GLOBAL_PUSH(1),
    EMAIL_PUSH(2),
    ORDER(3),
    SUBSCRIPTION(4),
    MESSAGE(5),
    REQUEST(6),
    PRODUCT_NEW(7),
    PRODUCT_DISCOUNT(8);
}