package com.armboldmind.grandmarket.data.database.repositories

import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.data.IMoreRepository
import com.armboldmind.grandmarket.data.database.dao.KeysDao
import com.armboldmind.grandmarket.data.models.domain.More
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.ui.more.MoreFragmentDirections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MoreRepositoryLocal @Inject constructor(private val keysDao: KeysDao) : IMoreRepository {
    override suspend fun createUserItems(): Flow<List<More>> = flowOf(ArrayList<More>().apply {
        getKeys().collect { keys ->
            add(More(1, R.drawable.ic_menu_item_profile, keys.personal_information, MoreFragmentDirections.actionMoreFragmentToPersonalInformationFragment()))
            add(More(2, R.drawable.ic_addreses, keys.addresses, MoreFragmentDirections.actionMoreFragmentToAddressesFragment()))
            add(More(3, R.drawable.ic_cards, keys.cards, MoreFragmentDirections.actionMoreFragmentToCardsFragment()))
            add(More(4, R.drawable.ic_notifications, keys.notifications, MoreFragmentDirections.actionMoreFragmentToNotificationsFragment()))

            //            add(More(5, R.drawable.ic_orders, keys.orders, MoreFragmentDirections.actionMoreFragmentToOrdersFragment()))
            //            add(More(6, R.drawable.ic_requests, keys.requests, MoreFragmentDirections.actionMoreFragmentToRequestsFragment()))
            //            add(More(7, R.drawable.ic_saubscriptons, keys.subscriptions, MoreFragmentDirections.actionMoreFragmentToSubscriptionsFragment()))

            add(More(5, R.drawable.ic_orders, keys.orders, null))
            add(More(6, R.drawable.ic_requests, keys.requests, null))
            add(More(7, R.drawable.ic_saubscriptons, keys.subscriptions, null))

            add(More(-1, R.drawable.ic_grant_market_logo, "", MoreFragmentDirections.actionMoreFragmentToRequestedProductsFragment()))
        }
    })

    override suspend fun getKeys(): Flow<Keys> {
        return flow {
            keysDao.findKeys()
                .mapIndexed { index, keys ->
                    if (index == 0) emit(keys)
                }
        }
    }


    override suspend fun createGlobalItems(): Flow<List<More>> = flowOf(ArrayList<More>().apply {
        getKeys().collect { keys ->
            add(More(9, R.drawable.ic_about_us, keys.about_us, MoreFragmentDirections.actionMoreFragmentToAboutUsFragment()))
            add(More(10,
                     R.drawable.ic_terms,
                     keys.terms_and_conditions,
                     MoreFragmentDirections.actionMoreFragmentToPrivacyPolicyFragment(isTerms = true, removePaddingBottom = false)))
            add(More(11,
                     R.drawable.ic_privacy_p,
                     keys.privacy_police,
                     MoreFragmentDirections.actionMoreFragmentToPrivacyPolicyFragment(isTerms = false, removePaddingBottom = false)))
            add(More(12, R.drawable.ic_faq, keys.faq, MoreFragmentDirections.actionMoreFragmentToFAQFragment()))
            add(More(13, R.drawable.ic_news, keys.news_and_events, MoreFragmentDirections.actionMoreFragmentToNewsAndEventsFragment()))
            add(More(14, R.drawable.ic_contact_us, keys.contact_us, MoreFragmentDirections.actionMoreFragmentToContactUsFragment()))
            add(More(15, R.drawable.ic_settings, keys.settings, MoreFragmentDirections.actionMoreFragmentToSettingsFragment()))
            add(More(16, R.drawable.ic_about_app, keys.about_app, MoreFragmentDirections.actionMoreFragmentToAboutAppFragment()))
        }
    })

}