package com.armboldmind.grandmarket.shared.enums

import com.armboldmind.grandmarket.GrandMarketApp
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.shared.customview.StateLayout

enum class EmptyStatesEnum(val emptyModel: StateLayout.EmptyModel) {
    FAVORITES(StateLayout.EmptyModel(R.drawable.ic_favorites_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_favorites)),
    ADDRESSES(StateLayout.EmptyModel(R.drawable.ic_addresses_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_addresses)),
    CARDS(StateLayout.EmptyModel(R.drawable.ic_cards_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_cards)),
    ORDERS(StateLayout.EmptyModel(R.drawable.ic_orders_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_orders)),
    BASKET(StateLayout.EmptyModel(R.drawable.ic_favorites_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_basket)),
    REQUESTS(StateLayout.EmptyModel(R.drawable.ic_requests_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_requests)),
    SUBSCRIPTIONS(StateLayout.EmptyModel(R.drawable.ic_subscribtions_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_subscriptions)),
    NOTIFICATIONS(StateLayout.EmptyModel(R.drawable.ic_notifications_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_notifications)),
    PRODUCTS(StateLayout.EmptyModel(R.drawable.ic_notifications_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_notifications)),
    SEARCH(StateLayout.EmptyModel(R.drawable.ic_notifications_empty, GrandMarketApp.getInstance().keysLiveData.value?.no_addresses));
}