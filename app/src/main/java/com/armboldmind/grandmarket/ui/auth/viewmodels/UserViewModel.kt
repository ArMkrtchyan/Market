package com.armboldmind.grandmarket.ui.auth.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.IUserRepository
import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.requestmodels.*
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.ImageSizesEnum
import com.armboldmind.grandmarket.shared.enums.UserRoleEnum
import com.armboldmind.grandmarket.shared.globalextensions.appComponent
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.shared.managers.PreferencesManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class UserViewModel : BaseViewModel() {

    @Inject
    lateinit var mUserRepositoryRemote: IUserRepository

    @Inject
    lateinit var mPreferencesManager: PreferencesManager

    private val userMutableLiveData by lazy { MutableLiveData<User>(mPreferencesManager.findByKey(BundleKeysEnum.USER.key)) }
    val userLiveData: LiveData<User>
        get() = userMutableLiveData

    init {
        appComponent().authorizationComponent.build()
                .inject(this)
        userMutableLiveData.value = mPreferencesManager.findByKey(BundleKeysEnum.USER.key)
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            setState.invoke(ViewState.ErrorState(BaseDataSource.SuccessException(throwable.localizedMessage)))
            Log.d("ExceptionTag", throwable.localizedMessage ?: "")
        }) {
            when (actionIntent) {
                is ActionIntent.SignInAsGuestActionIntent -> signInAsGuest()
                is ActionIntent.VerifyUsernameActionIntent -> verifyUsername(actionIntent.signInData,actionIntent.isUsernameMustExist)
                is ActionIntent.NotifyUserData -> userMutableLiveData.postValue(mPreferencesManager.findByKey(BundleKeysEnum.USER.key))
                is ActionIntent.SignInActionIntent -> signIn(actionIntent.signInData)
                is ActionIntent.GetUserInfoActionIntent -> getUserInfo(actionIntent.withLoading)
                is ActionIntent.SignOutActionIntent -> signOut()
                is ActionIntent.SendCodeActionIntent -> sendCode(actionIntent.sendCodeData)
                is ActionIntent.SendCodeForPhoneActionIntent -> sendCodeForPhone(actionIntent.sendCodeData)
                is ActionIntent.SendCodeForEmailActionIntent -> sendCodeForEmail(actionIntent.sendCodeData)
                is ActionIntent.ChangeUserPhoneNumberActionIntent -> updateUserPhoneNumber(actionIntent.sendCodeData)
                is ActionIntent.ChangeUserEmailActionIntent -> updateUserEmail(actionIntent.sendCodeData)
                is ActionIntent.SendCodeForForgotPasswordActionIntent -> sendCodeForForgotPassword(actionIntent.sendCodeData)
                is ActionIntent.VerifyCodeActionIntent -> verifyCode(actionIntent.sendCodeRequestModel)
                is ActionIntent.ResetPasswordActionIntent -> resetPassword(actionIntent.signUpRequestModel)
                is ActionIntent.SignUpActionIntent -> signUp(actionIntent.signUpRequestModel)
                is ActionIntent.UpdateActionIntent -> update(actionIntent.updateUserRequestModel)
                is ActionIntent.ChangePasswordActionIntent -> changePassword(actionIntent.changePasswordRequestModel)
                is ActionIntent.UploadImageActionIntent -> uploadImage(actionIntent.photo, actionIntent.isAdd)
                is ActionIntent.DeleteImageActionIntent -> deletePhotoForUser()
                else -> Unit
            }
        }
    }

    private suspend fun signInAsGuest() {
        mUserRepositoryRemote.signInAsGuest()
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect {
                    mPreferencesManager.saveByKey(BundleKeysEnum.ACCESS_TOKEN.key, it.token)
                    mPreferencesManager.saveByKey(BundleKeysEnum.USER_ROLE.key, UserRoleEnum.GUEST)
                    mPreferencesManager.remove(BundleKeysEnum.USER.key)
                    withContext(Dispatchers.Main) {
                        setState.invoke(ViewState.SuccessState())
                        setState.invoke(ViewState.MoreSignedOutState)
                    }
                }
    }

    private suspend fun signIn(signInData: SignInRequestModel) {
        mUserRepositoryRemote.signIn(signInData)
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect {
                    mPreferencesManager.saveByKey(BundleKeysEnum.ACCESS_TOKEN.key, it.token)
                    mPreferencesManager.saveByKey(BundleKeysEnum.USER_ROLE.key, UserRoleEnum.USER)
                    getUserInfo()
                }
    }

    private suspend fun verifyUsername(sendCodeData: SendCodeRequestModel, isUsernameMustExist: Boolean) {
        mUserRepositoryRemote.verifyUsername(sendCodeData)
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect {
                    if (it) withContext(Dispatchers.Main) {
                        if (!isUsernameMustExist) setState.invoke(ViewState.ErrorState(BaseDataSource.SuccessException(keysLiveData().value?.account_with_phone_exist ?: "")))
                        else setState.invoke(ViewState.VerifyUserNameState(sendCodeData))
                    }
                    else withContext(Dispatchers.Main) {
                        if (isUsernameMustExist) setState.invoke(ViewState.ErrorState(BaseDataSource.SuccessException(
                            if (sendCodeData.email == null) keysLiveData().value?.account_with_phone_not_exist ?: ""
                            else keysLiveData().value?.account_with_email_not_exist ?: "",
                        )))
                        else setState.invoke(ViewState.VerifyUserNameState(sendCodeData))
                    }
                }
    }

    private suspend fun getUserInfo(needLoading: Boolean = true) {
        val user: UserRoleEnum = mPreferencesManager.findByKey(BundleKeysEnum.USER_ROLE.key)
        if (user == UserRoleEnum.USER) mUserRepositoryRemote.getUserInfo()
                .onStart {
                    if (needLoading) withContext(Dispatchers.Main) {
                        setState.invoke(ViewState.LoadingState)
                    }
                }
                .catch { throwable -> withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) } }
                .collect {
                    mPreferencesManager.saveByKey(BundleKeysEnum.USER.key, it)
                    userMutableLiveData.postValue(mPreferencesManager.findByKey(BundleKeysEnum.USER.key))
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.SuccessState()) }
                }
    }

    private suspend fun signOut() {
        mUserRepositoryRemote.signOut()
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect {
                    mPreferencesManager.saveByKey(BundleKeysEnum.ACCESS_TOKEN.key, it.token)
                    mPreferencesManager.saveByKey(BundleKeysEnum.USER_ROLE.key, UserRoleEnum.GUEST)
                    mPreferencesManager.remove(BundleKeysEnum.USER.key)
                    withContext(Dispatchers.Main) {
                        setState.invoke(ViewState.MoreSignedOutState)
                    }
                }

    }

    private suspend fun sendCode(sendCodeData: SendCodeRequestModel) {
        mUserRepositoryRemote.sendCode(sendCodeData)
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect { withContext(Dispatchers.Main) { setState.invoke(ViewState.CodeSentSuccessState(sendCodeData)) } }

    }

    private suspend fun sendCodeForEmail(sendCodeData: SendCodeRequestModel) {
        mUserRepositoryRemote.sendCodeForEmail(sendCodeData)
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect { withContext(Dispatchers.Main) { setState.invoke(ViewState.CodeSentSuccessState(sendCodeData)) } }

    }

    private suspend fun sendCodeForPhone(sendCodeData: SendCodeRequestModel) {
        mUserRepositoryRemote.sendCodeForPhone(sendCodeData)
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect { withContext(Dispatchers.Main) { setState.invoke(ViewState.CodeSentSuccessState(sendCodeData)) } }

    }

    private suspend fun sendCodeForForgotPassword(sendCodeData: SendCodeRequestModel) {
        mUserRepositoryRemote.sendCodeForForgotPassword(sendCodeData)
                .onStart {
                    withContext(Dispatchers.Main) {
                        setState.invoke(ViewState.LoadingState)
                    }
                }
                .catch { throwable -> withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) } }
                .collect {
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.CodeSentSuccessState(sendCodeData)) }
                }
    }

    private suspend fun verifyCode(sendCodeRequestModel: SendCodeRequestModel) {
        mUserRepositoryRemote.verifyCode(sendCodeRequestModel)
                .onStart {
                    withContext(Dispatchers.Main) {
                        setState.invoke(ViewState.LoadingState)
                    }
                }
                .catch { throwable -> withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) } }
                .collect {
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.VerifyCodeSuccessState(sendCodeRequestModel)) }
                }
    }

    private suspend fun resetPassword(signUpRequestModel: SignUpRequestModel) {
        mUserRepositoryRemote.resetPassword(signUpRequestModel)
                .onStart {
                    withContext(Dispatchers.Main) {
                        setState.invoke(ViewState.LoadingState)
                    }
                }
                .catch { throwable -> withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) } }
                .collect {
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.SuccessState()) }
                }
    }


    private suspend fun signUp(signUpRequestModel: SignUpRequestModel) {
        mUserRepositoryRemote.signUp(signUpRequestModel)
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect {
                    mPreferencesManager.saveByKey(BundleKeysEnum.USER_ROLE.key, UserRoleEnum.USER)
                    mPreferencesManager.saveByKey(BundleKeysEnum.ACCESS_TOKEN.key, it.token)
                    getUserInfo()
                }

    }

    private suspend fun update(updateUserRequestModel: UpdateUserRequestModel) {
        mUserRepositoryRemote.update(updateUserRequestModel)
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect {
                    getUserInfo(false)
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.SuccessState(keysLiveData().value?.info_changed_successfully)) }
                }

    }

    private suspend fun updateUserEmail(sendCodeData: SendCodeRequestModel) {
        mUserRepositoryRemote.updateUserEmail(sendCodeData)
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect { withContext(Dispatchers.Main) { setState.invoke(ViewState.SuccessState()) } }

    }

    private suspend fun updateUserPhoneNumber(sendCodeData: SendCodeRequestModel) {
        mUserRepositoryRemote.updateUserPhoneNumber(sendCodeData)
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect { withContext(Dispatchers.Main) { setState.invoke(ViewState.SuccessState()) } }

    }

    private suspend fun changePassword(changePasswordRequestModel: ChangePasswordRequestModel) {
        mUserRepositoryRemote.changePassword(changePasswordRequestModel)
                .onStart {
                    withContext(Dispatchers.Main) {
                        setState.invoke(ViewState.LoadingState)
                    }
                }
                .catch { throwable -> withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) } }
                .collect {
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.SuccessState(keysLiveData().value?.password_changed_successfully)) }
                }
    }

    private suspend fun uploadImage(photo: MultipartBody.Part, isAdd: Boolean) {
        mUserRepositoryRemote.uploadImage(photo)
                .onStart { }
                .catch { throwable -> withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) } }
                .collect {
                    val user: User = mPreferencesManager.findByKey(BundleKeysEnum.USER.key)
                    user.imageUrl = it + ImageSizesEnum.MEDIUM.size
                    mPreferencesManager.saveByKey(BundleKeysEnum.USER.key, user)
                    userMutableLiveData.postValue(user)
                    withContext(Dispatchers.Main) {
                        setState.invoke(ViewState.SuccessState(if (isAdd) keysLiveData().value?.photo_added_successfully else keysLiveData().value?.photo_changed_successfully))
                    }
                }
    }

    private suspend fun deletePhotoForUser() {
        mUserRepositoryRemote.deletePhotoForUser()
                .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
                .catch { throwable ->
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
                }
                .collect {
                    val user: User = mPreferencesManager.findByKey(BundleKeysEnum.USER.key)
                    user.imageUrl = ""
                    mPreferencesManager.saveByKey(BundleKeysEnum.USER.key, user)
                    userMutableLiveData.postValue(user)
                    withContext(Dispatchers.Main) { setState.invoke(ViewState.SuccessState(keysLiveData().value?.photo_deleted_successfully)) }
                }
    }
}