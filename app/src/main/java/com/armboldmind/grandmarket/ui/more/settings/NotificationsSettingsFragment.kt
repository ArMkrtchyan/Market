package com.armboldmind.grandmarket.ui.more.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentNotificationsSettingsBinding
import com.armboldmind.grandmarket.shared.enums.NotificationSubscriptionsEnum
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.more.notifications.NotificationsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationsSettingsFragment : BaseFragment<FragmentNotificationsSettingsBinding>() {

    private val mNotificationViewModel: NotificationsViewModel by lazy { createViewModel(NotificationsViewModel::class.java, this) }
    private var mLastActionIntent: ActionIntent = ActionIntent.GetUserPushSubscribe
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNotificationsSettingsBinding
        get() = FragmentNotificationsSettingsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentNotificationsSettingsBinding, keysFromDb: Keys) {
        mNotificationViewModel.sendIntent(ActionIntent.GetUserPushSubscribe)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            emailNotificationsSwitch.setOnClickListener {
                lifecycleScope.launch {
                    delay(180)
                    mLastActionIntent = ActionIntent.ChangeUserPushSubscribe(NotificationSubscriptionsEnum.EMAIL_PUSH.type, emailNotificationsSwitch.isChecked)
                    mNotificationViewModel.sendIntent(mLastActionIntent)
                }
            }
            pushNotificationsSwitch.setOnClickListener {
                lifecycleScope.launch {
                    delay(180)
                    mLastActionIntent = ActionIntent.ChangeUserPushSubscribePair(NotificationSubscriptionsEnum.ORDER.type to NotificationSubscriptionsEnum.SUBSCRIPTION.type,
                                                                                 pushNotificationsSwitch.isChecked)
                    mNotificationViewModel.sendIntent(mLastActionIntent)
                }
            }
            marketingNotificationsSwitch.setOnClickListener {
                lifecycleScope.launch {
                    delay(180)
                    mLastActionIntent = ActionIntent.ChangeUserPushSubscribeDoubleTriple(Triple(NotificationSubscriptionsEnum.GLOBAL_PUSH.type,
                                                                                                NotificationSubscriptionsEnum.EMAIL_PUSH.type,
                                                                                                NotificationSubscriptionsEnum.MESSAGE.type),
                                                                                         Triple(NotificationSubscriptionsEnum.REQUEST.type,
                                                                                                NotificationSubscriptionsEnum.PRODUCT_NEW.type,
                                                                                                NotificationSubscriptionsEnum.PRODUCT_DISCOUNT.type),
                                                                                         marketingNotificationsSwitch.isChecked)
                    mNotificationViewModel.sendIntent(mLastActionIntent)
                }
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> lifecycleScope.launch {
                delay(100)
                showError(message = viewState.exception) { mNotificationViewModel.sendIntent(mLastActionIntent) }
            }
            is ViewState.SuccessState -> {
                viewState.message?.let { showToast(it) }
                setLoading(false)
            }
            is ViewState.FetchUserPushSubscribeState -> fetchUserPushSubscribeState(viewState.items)
            else -> setLoading(false)
        }
    }

    private fun fetchUserPushSubscribeState(items: List<Int>) {
        setLoading(false)
        mBinding.emailNotificationsSwitch.isChecked = items.contains(NotificationSubscriptionsEnum.EMAIL_PUSH.ordinal)
        mBinding.pushNotificationsSwitch.isChecked = items.contains(NotificationSubscriptionsEnum.ORDER.type) && items.contains(NotificationSubscriptionsEnum.SUBSCRIPTION.ordinal)
        mBinding.marketingNotificationsSwitch.isChecked =
            items.contains(NotificationSubscriptionsEnum.GLOBAL_PUSH.ordinal) && items.contains(NotificationSubscriptionsEnum.EMAIL_PUSH.ordinal) && items.contains(
                NotificationSubscriptionsEnum.MESSAGE.ordinal) && items.contains(NotificationSubscriptionsEnum.REQUEST.ordinal) && items.contains(NotificationSubscriptionsEnum.PRODUCT_NEW.ordinal) && items.contains(
                NotificationSubscriptionsEnum.PRODUCT_DISCOUNT.ordinal)
    }
}