package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import com.armboldmind.grandmarket.data.IUserRepository
import com.armboldmind.grandmarket.data.mappers.UserMapper
import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.requestmodels.*
import com.armboldmind.grandmarket.data.models.responsemodels.GuestResponseModel
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.IUserService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepositoryRemote @Inject constructor(private val mUserService: IUserService, private val mUserMapper: UserMapper, mApplication: Context) : BaseDataSource(mApplication),
    IUserRepository {

    override suspend fun signIn(signInData: SignInRequestModel): Flow<GuestResponseModel> {
        return getResult { mUserService.signIn(signInData) }
    }

    override suspend fun verifyUsername(sendCodeData: SendCodeRequestModel): Flow<Boolean> {
        return getResult { mUserService.verifyUsername(sendCodeData) }
    }

    override suspend fun signInAsGuest(): Flow<GuestResponseModel> {
        return getResult { mUserService.signInAsGuest() }
    }

    override suspend fun getUserInfo(): Flow<User> {
        return getResultWithMapper(mUserMapper) { mUserService.getUserDetails() }
    }

    override suspend fun signUp(signUpRequestModel: SignUpRequestModel): Flow<GuestResponseModel> {
        return getResult { mUserService.signUp(signUpRequestModel) }
    }

    override suspend fun signOut(): Flow<GuestResponseModel> {
        return getResult { mUserService.signOut() }
    }

    override suspend fun update(updateUserRequestModel: UpdateUserRequestModel): Flow<Boolean> {
        return getResult { mUserService.updateUserInfo(updateUserRequestModel) }
    }

    override suspend fun updateUserEmail(sendCodeData: SendCodeRequestModel): Flow<Boolean> {
        return getResult { mUserService.updateUserEmail(sendCodeData) }
    }

    override suspend fun updateUserPhoneNumber(sendCodeData: SendCodeRequestModel): Flow<Boolean> {
        return getResult { mUserService.updateUserPhoneNumber(sendCodeData) }
    }

    override suspend fun verifyCode(sendCodeData: SendCodeRequestModel): Flow<Boolean> {
        return getResult { mUserService.verifyCode(sendCodeData) }
    }

    override suspend fun sendCode(sendCodeData: SendCodeRequestModel): Flow<Boolean> {
        return getResult { mUserService.sendCode(sendCodeData) }
    }

    override suspend fun sendCodeForEmail(sendCodeData: SendCodeRequestModel): Flow<Boolean> {
        return getResult { mUserService.sendCodeForEmail(sendCodeData) }
    }

    override suspend fun sendCodeForPhone(sendCodeData: SendCodeRequestModel): Flow<Boolean> {
        return getResult { mUserService.sendCodeForPhone(sendCodeData) }
    }

    override suspend fun sendCodeForForgotPassword(sendCodeData: SendCodeRequestModel): Flow<Boolean> {
        return getResult { mUserService.sendCodeForForgotPassword(sendCodeData) }
    }

    override suspend fun resetPassword(signUpRequestModel: SignUpRequestModel): Flow<Boolean> {
        return getResult { mUserService.resetPassword(signUpRequestModel) }
    }

    override suspend fun changePassword(changePasswordRequestModel: ChangePasswordRequestModel): Flow<Boolean> {
        return getResult { mUserService.changePassword(changePasswordRequestModel) }
    }

    override suspend fun uploadImage(photo: MultipartBody.Part): Flow<String> {
        return getResult { mUserService.uploadImage(photo) }
    }

    override suspend fun deletePhotoForUser(): Flow<Boolean> {
        return getResult { mUserService.deletePhotoForUser() }
    }
}