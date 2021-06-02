package com.armboldmind.grandmarket.shared.enums

import androidx.annotation.DrawableRes
import com.armboldmind.grandmarket.R

enum class NotificationActionEnum(val action: Int, @DrawableRes val icon: Int, @DrawableRes val background: Int) {
    GLOBAL(1, R.drawable.ic_grant_market, R.drawable.background_notification_nothing),
    CATEGORY(2, R.drawable.ic_grant_market, R.drawable.background_notification_discount),
    PRODUCT(3, R.drawable.ic_product_for_notification, R.drawable.background_notification_product),
    NEWS(4, R.drawable.ic_news_for_notifications, R.drawable.background_notification_news),
    BLOG(5, R.drawable.ic_grant_market, R.drawable.background_notification_discount),
    EVENT(6, R.drawable.ic_news_for_notifications, R.drawable.background_notification_discount),
    BRAND(7, R.drawable.ic_grant_market, R.drawable.background_notification_discount),
    LINK(8, R.drawable.ic_grant_market, R.drawable.background_notification_discount),

    ORDERS(9, R.drawable.ic_product_for_notifications, R.drawable.background_notification_orders),
    BASKET(10, R.drawable.ic_basket_for_notifications, R.drawable.background_notification_basket),
    SUBSCRIPTIONS(11, R.drawable.ic_subscribtion_for_notification, R.drawable.background_notification_subscribtions),
    DISCOUNT(12, R.drawable.ic_discount_for_notifications, R.drawable.background_notification_discount),
}