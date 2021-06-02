package com.armboldmind.grandmarket.data.network.services

import com.armboldmind.grandmarket.data.models.requestmodels.*
import com.armboldmind.grandmarket.data.models.responsemodels.GuestResponseModel
import com.armboldmind.grandmarket.data.models.responsemodels.UserResponseModel
import com.armboldmind.grandmarket.data.network.ResponseModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface IUserService {

    @GET("user/getUserDetails")
    suspend fun getUserDetails(): Response<ResponseModel<UserResponseModel>>

    @POST("auth/signUpAsGuest")
    suspend fun signInAsGuest(): Response<ResponseModel<GuestResponseModel>>

    @POST("auth/signIn")
    suspend fun signIn(@Body signInData: SignInRequestModel): Response<ResponseModel<GuestResponseModel>>

    @POST("auth/verifyUsername")
    suspend fun verifyUsername(@Body sendCodeData: SendCodeRequestModel): Response<ResponseModel<Boolean>>

    @PUT("auth/logout")
    suspend fun signOut(): Response<ResponseModel<GuestResponseModel>>

    @PUT("auth/sendCodeForSignUp")
    suspend fun sendCode(@Body sendCodeData: SendCodeRequestModel): Response<ResponseModel<Boolean>>

    @PUT("user/sendCodeForVerifyEmail")
    suspend fun sendCodeForEmail(@Body sendCodeData: SendCodeRequestModel): Response<ResponseModel<Boolean>>

    @PUT("user/sendCodeForVerifyPhone")
    suspend fun sendCodeForPhone(@Body sendCodeData: SendCodeRequestModel): Response<ResponseModel<Boolean>>

    @PUT("auth/sendCodeForForgotPassword")
    suspend fun sendCodeForForgotPassword(@Body sendCodeData: SendCodeRequestModel): Response<ResponseModel<Boolean>>

    @PUT("auth/resetPassword")
    suspend fun resetPassword(@Body signUpData: SignUpRequestModel): Response<ResponseModel<Boolean>>

    @PUT("auth/verifyCode")
    suspend fun verifyCode(@Body sendCodeData: SendCodeRequestModel): Response<ResponseModel<Boolean>>

    @POST("auth/signUp")
    suspend fun signUp(@Body signUpData: SignUpRequestModel): Response<ResponseModel<GuestResponseModel>>

    @PUT("user")
    suspend fun updateUserInfo(@Body updateUserRequestModel: UpdateUserRequestModel): Response<ResponseModel<Boolean>>

    @PUT("user/changePhoneNumber")
    suspend fun updateUserPhoneNumber(@Body updateUserRequestModel: SendCodeRequestModel): Response<ResponseModel<Boolean>>

    @PUT("user/changeEmail")
    suspend fun updateUserEmail(@Body updateUserRequestModel: SendCodeRequestModel): Response<ResponseModel<Boolean>>

    @PUT("user/changePassword")
    suspend fun changePassword(@Body changePasswordRequestModel: ChangePasswordRequestModel): Response<ResponseModel<Boolean>>

    @Multipart
    @POST("user/uploadImage")
    suspend fun uploadImage(@Part photo: MultipartBody.Part): Response<ResponseModel<String>>

    @DELETE("user/deletePhotoForUser")
    suspend fun deletePhotoForUser(): Response<ResponseModel<Boolean>>

}