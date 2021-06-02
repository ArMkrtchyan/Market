package com.armboldmind.grandmarket.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseActivity
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.requestmodels.searchProductsModel
import com.armboldmind.grandmarket.databinding.ActivityMainBinding
import com.armboldmind.grandmarket.databinding.ViewNotificationBadgeBinding
import com.armboldmind.grandmarket.shared.enums.*
import com.armboldmind.grandmarket.shared.globalextensions.*
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.utils.AnimationUtil
import com.armboldmind.grandmarket.ui.init.LanguageViewModel
import com.armboldmind.grandmarket.ui.more.notifications.NotificationsViewModel
import com.armboldmind.grandmarket.ui.productdetails.ProductDetailsActivity
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.yandex.mapkit.MapKitFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object {
       // private const val MAP_KIT_API_KEY = "2a4ef143-1c29-453b-95c7-934f0258ccf7"
        private const val MAP_KIT_API_KEY = "fdvdfvfd"
        var isAnimated = false
    }

    private val defaultActionType = -1
    private val defaultActionId = -1L

    private val mLanguagesViewModel by lazy { createViewModel(LanguageViewModel::class.java, this) }
    private val mNotificationViewModel: NotificationsViewModel by lazy { createViewModel(NotificationsViewModel::class.java, this) }
    private val mBackStack: ArrayList<Int> = arrayListOf<Int>().apply { add(MenuEnum.HOME.position) }
    private lateinit var mMenuTabs: List<NavHostFragment>

    private val mOrderBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: android.content.Intent?) {
            if (ActionsEnum.RECEIVE_GLOBAL_NOTIFICATION_ACTION.action == intent?.action) {
                when (mNavController.currentDestination?.id) {
                    R.id.notificationsFragment -> mNotificationViewModel.sendIntent(ActionIntent.GetNotifications)
                    else -> {
                        mNotificationViewModel.sendIntent(ActionIntent.GetUnseenNotifications)
                    }
                }
            }
        }
    }

    val mNavController: NavController
        get() = mMenuTabs[mBackStack.last()].navController

    override val inflate: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        if (preferencesManager().findByKey(BundleKeysEnum.DARK_MODE.key)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.AppThemeLight)
        }
        updateResources(preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).uniqueSeoCode)
        initMapKitSettings()
        mLanguagesViewModel.sendIntent(ActionIntent.GetAllKeysFromNetwork)
        super.onCreate(savedInstanceState)
        onNetworkStatusChange(this) {

        }
        initMainPager()
        KeyboardVisibilityEvent.setEventListener(this, this) { isOpen ->
            if (isOpen) hideBottomNavigation() else showBottomNavigation()
        }
        overridePendingTransition(R.anim.fade_in_500, R.anim.fade_out_500)
        handleNotification(intent?.getIntExtra(BundleKeysEnum.ACTION_TYPE.key, defaultActionType) ?: defaultActionType)
    }

    override fun onResume() {
        super.onResume()
        mNotificationViewModel.sendIntent(ActionIntent.GetUnseenNotifications)
        try {
            registerReceiver(mOrderBroadcastReceiver, IntentFilter(ActionsEnum.RECEIVE_GLOBAL_NOTIFICATION_ACTION.action))
            setStatusBarColor(R.color.white)
        } catch (e: Exception) {
        }
        updateResources(preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).uniqueSeoCode)
    }

    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(mOrderBroadcastReceiver)
        } catch (e: Exception) {
        }
    }

    private fun initBottomNavigation() {
        mBinding.apply {
            bottomNavigationView.background = null
            bottomNavigationView.menu.getItem(2).isEnabled = false
            bottomNavigationView.setOnNavigationItemSelectedListener {
                fab.setImageResource(R.drawable.ic_basket)
                badgeViewBinding.profileImage.background = this@MainActivity.getDrawableCompat(R.drawable.background_rounded_profile_unselected)
                when (it.itemId) {
                    R.id.home -> {
                        mBackStack.remove(MenuEnum.HOME.position)
                        mBackStack.add(MenuEnum.HOME.position)
                        mainPager.setCurrentItem(MenuEnum.HOME.position, false)
                    }
                    R.id.categories -> {
                        mBackStack.remove(MenuEnum.CATEGORIES.position)
                        mBackStack.add(MenuEnum.CATEGORIES.position)
                        mainPager.setCurrentItem(MenuEnum.CATEGORIES.position, false)
                    }
                    R.id.favorites -> {
                        mBackStack.remove(MenuEnum.FAVORITES.position)
                        mBackStack.add(MenuEnum.FAVORITES.position)
                        mainPager.setCurrentItem(MenuEnum.FAVORITES.position, false)
                    }
                    R.id.more -> {
                        mBackStack.remove(MenuEnum.MORE.position)
                        mBackStack.add(MenuEnum.MORE.position)
                        mainPager.setCurrentItem(MenuEnum.MORE.position, false)
                        badgeViewBinding.profileImage.background = this@MainActivity.getDrawableCompat(R.drawable.background_rounded_profile_selected)
                    }
                }
                true
            }
            fab.setOnClickListener {
                mBackStack.remove(MenuEnum.BASKET.position)
                mBackStack.add(MenuEnum.BASKET.position)
                mainPager.setCurrentItem(MenuEnum.BASKET.position, false)
                bottomNavigationView.selectedItemId = R.id.placeholder
                fab.setImageResource(R.drawable.ic_basket_selected)
            }
        }
        createBottomNavigationBadge()
    }

    private lateinit var badgeViewBinding: ViewNotificationBadgeBinding

    private fun createBottomNavigationBadge() {
        mBinding.apply {
            val menuItem = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
            val itemView = menuItem.getChildAt(4) as BottomNavigationItemView
            badgeViewBinding = ViewNotificationBadgeBinding.inflate(LayoutInflater.from(this@MainActivity), menuItem, false)
            itemView.addView(badgeViewBinding.root)
            checkBottomNavigationProfileIcon()
            mNotificationViewModel.unseenNotificationsLiveData.observe(this@MainActivity) {
                if (it > 0) {
                    badgeViewBinding.badge.text = it.toString()
                    badgeViewBinding.badge.show()
                } else badgeViewBinding.badge.gone()
            }
        }
    }

    fun checkBottomNavigationProfileIcon() {
        mBinding.apply {
            if (preferencesManager().findByKey<UserRoleEnum>(BundleKeysEnum.USER_ROLE.key) == UserRoleEnum.USER) {
                val user: User? = preferencesManager().findByKey<User?>(BundleKeysEnum.USER.key)
                badgeViewBinding.profileImage.show()
                badgeViewBinding.user = user
            } else {
                badgeViewBinding.profileImage.gone()
            }
        }
    }

    private fun initMainPager() {
        initBottomNavigation()
        mMenuTabs = initMenuTabs()
        mBinding.mainPager.apply {
            isUserInputEnabled = false
            offscreenPageLimit = 4
            adapter = PagerAdapter(mMenuTabs, supportFragmentManager, lifecycle)
        }
    }

    private fun initMenuTabs(): List<NavHostFragment> {
        return arrayListOf(NavHostFragment.create(R.navigation.navigation_home),
                           NavHostFragment.create(R.navigation.navigation_categories),
                           NavHostFragment.create(R.navigation.navigation_basket),
                           NavHostFragment.create(R.navigation.navigation_favorites),
                           NavHostFragment.create(R.navigation.navigation_more))
    }

    private inner class PagerAdapter(
        private val list: List<NavHostFragment>,
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return list.size
        }

        override fun createFragment(position: Int): Fragment {
            return list[position]
        }
    }

    override fun hideBottomNavigation() {
        mBinding.bottomAppBar.gone()
        mBinding.fab.hide()
    }

    override fun showBottomNavigation() {
        if (!mBinding.bottomAppBar.isVisible) {
            mBinding.bottomAppBar.show()
            mBinding.fab.show()
            AnimationUtil.alphaFrom0To1(mBinding.bottomNavigationView)
            AnimationUtil.alphaFrom0To1(mBinding.fab)
        }
    }

    override fun onBackPressed() {
        if (isAnimated) {
            return
        }
        if (mNavController.currentDestination?.id == R.id.requestProductInfoFragment) {
            mNavController.popBackStack()
            navigateToLast()
        } else if (!mNavController.navigateUp()) {
            if (mBackStack.size > 1) navigateToLast()
            else super.onBackPressed()
        }
    }

    private fun navigateToLast() {
        mBackStack.removeAt(mBackStack.lastIndex)
        fab.setImageResource(R.drawable.ic_basket)
        when (mBackStack.last()) {
            MenuEnum.HOME.position -> bottomNavigationView.selectedItemId = R.id.home
            MenuEnum.CATEGORIES.position -> bottomNavigationView.selectedItemId = R.id.categories
            MenuEnum.BASKET.position -> {
                bottomNavigationView.selectedItemId = R.id.placeholder
                fab.setImageResource(R.drawable.ic_basket_selected)
            }
            MenuEnum.FAVORITES.position -> bottomNavigationView.selectedItemId = R.id.favorites
            MenuEnum.MORE.position -> bottomNavigationView.selectedItemId = R.id.more
        }
        mBinding.mainPager.setCurrentItem(mBackStack.last(), false)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (isAnimated) {
            return false
        }
        return mNavController.navigateUp() || super.onSupportNavigateUp()
    }

    fun navigateToTab(tab: MenuEnum) {
        mBinding.apply {
            mBackStack.remove(tab.position)
            mBackStack.add(tab.position)
            mainPager.setCurrentItem(tab.position, false)
            bottomNavigationView.selectedItemId = when (tab) {
                MenuEnum.HOME -> R.id.home
                MenuEnum.CATEGORIES -> R.id.categories
                MenuEnum.BASKET -> R.id.placeholder
                MenuEnum.FAVORITES -> R.id.favorites
                MenuEnum.MORE -> R.id.more
            }
        }
    }

    private fun initMapKitSettings() {
        MapKitFactory.setApiKey(MAP_KIT_API_KEY)
        val tag = Locale.getDefault().language + "_" + Locale.getDefault().country
        MapKitFactory.setLocale(tag)
    }

    private fun handleNotification(actionType: Int) {
        val actionId = intent?.getLongExtra(BundleKeysEnum.ACTION_ID.key, defaultActionId) ?: defaultActionId
        when (actionType) {
            -1 -> Unit
            NotificationActionEnum.CATEGORY.action -> {
                if (actionId != -1L) navigateToProductsList(actionId)
                else navigateToNotificationsList()
            }
            NotificationActionEnum.PRODUCT.action -> {
                if (actionId != -1L) navigateToProductDetails(actionId)
                else navigateToNotificationsList()
            }
            NotificationActionEnum.NEWS.action -> {
                if (actionId != -1L) navigateToNewsList(actionId)
                else navigateToNotificationsList()
            }
            NotificationActionEnum.BLOG.action -> {
                if (actionId != -1L) navigateToNewsList(actionId)
                else navigateToNotificationsList()
            }
            NotificationActionEnum.EVENT.action -> {
                if (actionId != -1L) navigateToNewsList(actionId)
                else navigateToNotificationsList()
            }
            NotificationActionEnum.ORDERS.action -> {
                if (actionId != -1L) navigateToOrderDetails(actionId)
                else navigateToNotificationsList()
            }
            NotificationActionEnum.BASKET.action -> {
                navigateToBasket()
            }
            NotificationActionEnum.SUBSCRIPTIONS.action -> {
                if (actionId != -1L) navigateToSubscription(actionId)
                else navigateToNotificationsList()
            }
            NotificationActionEnum.DISCOUNT.action -> {
            }
            else -> {
                navigateToNotificationsList()
            }
        }
    }

    private fun navigateToNotificationsList() = lifecycleScope.launchWhenCreated {
        delay(1000)
        if (preferencesManager().findByKey<UserRoleEnum>(BundleKeysEnum.USER_ROLE.key) == UserRoleEnum.USER) {
            moreNavController().navigate(R.id.notificationsFragment)
            navigateToTab(MenuEnum.MORE)
        }
    }

    private fun navigateToNewsList(actionId: Long) = lifecycleScope.launchWhenCreated {
        delay(1000)
        if (preferencesManager().findByKey<UserRoleEnum>(BundleKeysEnum.USER_ROLE.key) == UserRoleEnum.USER) {
            moreNavController().navigate(R.id.newsAndEventsFragment)
            moreNavController().navigate(R.id.newsDetailsFragment, bundleOf("id" to actionId))
            navigateToTab(MenuEnum.MORE)
        }
    }

    private fun navigateToProductsList(actionId: Long) = lifecycleScope.launchWhenCreated {
        delay(500)
        if (preferencesManager().findByKey<UserRoleEnum>(BundleKeysEnum.USER_ROLE.key) == UserRoleEnum.USER) {
            categoriesNavController().navigate(R.id.productsFragment, bundleOf("searchProductModel" to searchProductsModel {
                categoryIdList = arrayListOf<Long>().apply { add(actionId) }
            }))
            navigateToTab(MenuEnum.CATEGORIES)
        }
    }

    private fun navigateToProductDetails(actionId: Long) = lifecycleScope.launchWhenCreated {
        if (preferencesManager().findByKey<UserRoleEnum>(BundleKeysEnum.USER_ROLE.key) == UserRoleEnum.USER) {
            ProductDetailsActivity.start(this@MainActivity, bundleOf(BundleKeysEnum.PRODUCT.key to actionId))
        }
    }

    private fun navigateToOrderDetails(actionId: Long) = lifecycleScope.launchWhenCreated {
        delay(1000)
        if (preferencesManager().findByKey<UserRoleEnum>(BundleKeysEnum.USER_ROLE.key) == UserRoleEnum.USER) {
            moreNavController().navigate(R.id.ordersFragment)
            moreNavController().navigate(R.id.orderDetailsFragment, bundleOf("id" to actionId))
            navigateToTab(MenuEnum.MORE)
        }
    }

    private fun navigateToBasket() {
        navigateToTab(MenuEnum.BASKET)
    }

    private fun navigateToSubscription(actionId: Long) = lifecycleScope.launchWhenCreated {
        delay(1000)
        if (preferencesManager().findByKey<UserRoleEnum>(BundleKeysEnum.USER_ROLE.key) == UserRoleEnum.USER) {
            moreNavController().navigate(R.id.subscriptionsFragment)
            moreNavController().navigate(R.id.subscriptionDetailsFragment, bundleOf("id" to actionId))
            navigateToTab(MenuEnum.MORE)
        }
    }

    override fun setLoading(isLoading: Boolean) {}
    fun moreNavController() = mMenuTabs[4].navController
    fun categoriesNavController() = mMenuTabs[1].navController

}