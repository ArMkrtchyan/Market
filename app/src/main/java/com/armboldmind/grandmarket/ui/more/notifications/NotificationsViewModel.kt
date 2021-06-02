package com.armboldmind.grandmarket.ui.more.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.INotificationRepository
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.UserRoleEnum
import com.armboldmind.grandmarket.shared.globalextensions.appComponent
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.managers.PreferencesManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationsViewModel : BaseViewModel() {
    @Inject lateinit var mNotificationsRepository: INotificationRepository

    @Inject lateinit var mPreferencesManager: PreferencesManager

    init {
        appComponent().notificationsComponent.build()
                .inject(this)
    }

    private val unseenNotificationsMutableLiveData by lazy { MutableLiveData(0) }
    val unseenNotificationsLiveData: LiveData<Int>
        get() = unseenNotificationsMutableLiveData


    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i("ExceptionTAg", throwable.localizedMessage ?: "")
        }) {
            when (actionIntent) {
                is ActionIntent.GetNotifications -> getNotifications()
                is ActionIntent.GetUnseenNotifications -> getUnseenNotificationsCount()
                is ActionIntent.GetUserPushSubscribe -> getUserPushSubscribe()
                is ActionIntent.ChangeUserPushSubscribe -> enablePushNotification(actionIntent.notificationType, actionIntent.enable)
                is ActionIntent.ChangeUserPushSubscribePair -> enablePushNotification(actionIntent.notificationType, actionIntent.enable)
                is ActionIntent.ChangeUserPushSubscribeDoubleTriple -> enablePushNotification(actionIntent.notificationTypeTriple1,
                                                                                              actionIntent.notificationTypeTriple2,
                                                                                              actionIntent.enable)
                else -> Unit
            }
        }
    }

    private suspend fun getNotifications() {
        mNotificationsRepository.getNotifications()
                .collect { response ->
                    setState.invoke(ViewState.FetchNotifications(response))
                }

    }

    private suspend fun getUnseenNotificationsCount() {
        val user: UserRoleEnum = preferencesManager().findByKey(BundleKeysEnum.USER_ROLE.key)
        if (user == UserRoleEnum.USER) mNotificationsRepository.getUnseenCount()
                .collect {
                    unseenNotificationsMutableLiveData.postValue(it)
                }
    }

    private suspend fun getUserPushSubscribe() {
        val user: UserRoleEnum = preferencesManager().findByKey(BundleKeysEnum.USER_ROLE.key)
        if (user == UserRoleEnum.USER) mNotificationsRepository.getUserPushSubscribe()
                .onStart {
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
                }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect {
                    setState.invoke(ViewState.FetchUserPushSubscribeState(it))
                }
    }

    private suspend fun enablePushNotification(notificationType: Int, enable: Boolean) {
        val user: UserRoleEnum = preferencesManager().findByKey(BundleKeysEnum.USER_ROLE.key)
        if (user == UserRoleEnum.USER) mNotificationsRepository.enablePushNotification(notificationType, enable)
                .onStart {
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
                }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect {
                    setState.invoke(ViewState.SuccessState())
                }
    }

    private suspend fun enablePushNotification(notificationTypes: Pair<Int, Int>, enable: Boolean) {
        val user: UserRoleEnum = preferencesManager().findByKey(BundleKeysEnum.USER_ROLE.key)
        if (user == UserRoleEnum.USER) {
            val responseFirst = mNotificationsRepository.enablePushNotification(notificationTypes.first, enable)
            val responseSecond = mNotificationsRepository.enablePushNotification(notificationTypes.second, enable)
            responseFirst.zip(responseSecond) { _, _ ->
                setState.invoke(ViewState.SuccessState())
            }
                    .onStart {
                        withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
                    }
                    .catch { throwable ->
                        withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                    }
                    .collect { }
        }
    }

    private suspend fun enablePushNotification(notificationTypeTriple1: Triple<Int, Int, Int>, notificationTypeTriple2: Triple<Int, Int, Int>, enable: Boolean) {
        val user: UserRoleEnum = preferencesManager().findByKey(BundleKeysEnum.USER_ROLE.key)
        if (user == UserRoleEnum.USER) {
            val responseFirst = mNotificationsRepository.enablePushNotification(notificationTypeTriple1.first, enable)
            val responseSecond = mNotificationsRepository.enablePushNotification(notificationTypeTriple1.second, enable)
            val responseThird = mNotificationsRepository.enablePushNotification(notificationTypeTriple1.third, enable)
            val responseFourth = mNotificationsRepository.enablePushNotification(notificationTypeTriple2.first, enable)
            val responseFiveth = mNotificationsRepository.enablePushNotification(notificationTypeTriple2.second, enable)
            val responseSixth = mNotificationsRepository.enablePushNotification(notificationTypeTriple2.third, enable)
            responseFirst.zip(responseSecond) { _, _ ->
            }
                    .zip(responseThird) { _, _ ->
                        setState.invoke(ViewState.SuccessState())
                    }
                    .zip(responseFourth) { _, _ ->
                        setState.invoke(ViewState.SuccessState())
                    }
                    .zip(responseFiveth) { _, _ ->
                        setState.invoke(ViewState.SuccessState())
                    }
                    .zip(responseSixth) { _, _ ->
                        setState.invoke(ViewState.SuccessState())
                    }
                    .onStart {
                        withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
                    }
                    .catch { throwable ->
                        withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                    }
                    .collect { }
        }
    }
}