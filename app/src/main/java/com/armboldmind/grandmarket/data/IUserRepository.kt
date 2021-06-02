package com.armboldmind.grandmarket.data

import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.requestmodels.*
import com.armboldmind.grandmarket.data.models.responsemodels.GuestResponseModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface IUserRepository {
    suspend fun signIn(signInData: SignInRequestModel): Flow<GuestResponseModel>
    suspend fun verifyUsername(sendCodeData: SendCodeRequestModel): Flow<Boolean>
    suspend fun signInAsGuest(): Flow<GuestResponseModel>
    suspend fun signUp(signUpRequestModel: SignUpRequestModel): Flow<GuestResponseModel>
    suspend fun signOut(): Flow<GuestResponseModel>
    suspend fun verifyCode(sendCodeData: SendCodeRequestModel): Flow<Boolean>
    suspend fun sendCode(sendCodeData: SendCodeRequestModel): Flow<Boolean>
    suspend fun sendCodeForEmail(sendCodeData: SendCodeRequestModel): Flow<Boolean>
    suspend fun sendCodeForForgotPassword(sendCodeData: SendCodeRequestModel): Flow<Boolean>
    suspend fun resetPassword(signUpRequestModel: SignUpRequestModel): Flow<Boolean>
    suspend fun getUserInfo(): Flow<User>
    suspend fun update(updateUserRequestModel: UpdateUserRequestModel): Flow<Boolean>
    suspend fun sendCodeForPhone(sendCodeData: SendCodeRequestModel): Flow<Boolean>
    suspend fun updateUserPhoneNumber(sendCodeData: SendCodeRequestModel): Flow<Boolean>
    suspend fun updateUserEmail(sendCodeData: SendCodeRequestModel): Flow<Boolean>
    suspend fun changePassword(changePasswordRequestModel: ChangePasswordRequestModel): Flow<Boolean>
    suspend fun uploadImage(photo: MultipartBody.Part): Flow<String>
    suspend fun deletePhotoForUser(): Flow<Boolean>
}