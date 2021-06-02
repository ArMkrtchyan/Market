package com.armboldmind.grandmarket.shared.mvi.intents

import com.armboldmind.grandmarket.data.models.domain.Address
import com.armboldmind.grandmarket.data.models.domain.Card
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.requestmodels.*
import okhttp3.MultipartBody

sealed class ActionIntent {

    /**
     * User actions intents
     * **/
    class VerifyUsernameActionIntent(val signInData: SendCodeRequestModel,val isUsernameMustExist: Boolean) : ActionIntent()
    object SignInAsGuestActionIntent : ActionIntent()
    object NotifyUserData : ActionIntent()
    class SignInActionIntent(val signInData: SignInRequestModel) : ActionIntent()
    class GetUserInfoActionIntent(val withLoading: Boolean = true) : ActionIntent()
    object SignOutActionIntent : ActionIntent()
    class SendCodeActionIntent(val sendCodeData: SendCodeRequestModel) : ActionIntent()
    class SendCodeForPhoneActionIntent(val sendCodeData: SendCodeRequestModel) : ActionIntent()
    class SendCodeForEmailActionIntent(val sendCodeData: SendCodeRequestModel) : ActionIntent()
    class ChangeUserPhoneNumberActionIntent(val sendCodeData: SendCodeRequestModel) : ActionIntent()
    class ChangeUserEmailActionIntent(val sendCodeData: SendCodeRequestModel) : ActionIntent()
    class SendCodeForForgotPasswordActionIntent(val sendCodeData: SendCodeRequestModel) : ActionIntent()
    class VerifyCodeActionIntent(val sendCodeRequestModel: SendCodeRequestModel) : ActionIntent()
    class ResetPasswordActionIntent(val signUpRequestModel: SignUpRequestModel) : ActionIntent()
    class SignUpActionIntent(val signUpRequestModel: SignUpRequestModel) : ActionIntent()
    class UpdateActionIntent(val updateUserRequestModel: UpdateUserRequestModel) : ActionIntent()
    class ChangePasswordActionIntent(val changePasswordRequestModel: ChangePasswordRequestModel) : ActionIntent()
    class UploadImageActionIntent(val photo: MultipartBody.Part, val isAdd: Boolean) : ActionIntent()
    object DeleteImageActionIntent : ActionIntent()

    /**
     * More actions intents
     * **/
    object FetchGlobalItems : ActionIntent()
    object FetchUserItems : ActionIntent()

    /**
     * Info actions intents
     * **/
    object FetchTermsData : ActionIntent()
    object FetchPrivacyData : ActionIntent()
    class FetchFAQData(val search: SearchRequestModel, val isNeedToSecondLoading: Boolean = false) : ActionIntent()
    class SendContactData(val data: ContactUsRequestModel) : ActionIntent()
    object FetchNewsAndEventsData : ActionIntent()


    /**
     * Notifications actions intents
     * **/
    object GetNotifications : ActionIntent()
    object GetUnseenNotifications : ActionIntent()
    object GetUserPushSubscribe : ActionIntent()
    class ChangeUserPushSubscribe(val notificationType: Int, val enable: Boolean) : ActionIntent()
    class ChangeUserPushSubscribePair(val notificationType: Pair<Int, Int>, val enable: Boolean) : ActionIntent()
    class ChangeUserPushSubscribeTriple(val notificationType: Triple<Int, Int, Int>, val enable: Boolean) : ActionIntent()
    class ChangeUserPushSubscribeDoubleTriple(val notificationTypeTriple1: Triple<Int, Int, Int>, val notificationTypeTriple2: Triple<Int, Int, Int>, val enable: Boolean) : ActionIntent()


    /**
     * Addresses actions intents
     * **/
    object GetAddresses : ActionIntent()
    class SetAddressAsDefault(val address: Address) : ActionIntent()
    class EditAddress(val address: Address) : ActionIntent()
    class DeleteAddress(val address: Address) : ActionIntent()
    class AddAddress(val address: Address) : ActionIntent()
    class UpdateAddress(val address: Address) : ActionIntent()


    /**
     * Cards actions intents
     * **/
    object GetCards : ActionIntent()
    object AddCard : ActionIntent()
    class DeleteCard(val card: Card) : ActionIntent()
    class ShowDeleteDialog(val address: Address) : ActionIntent()
    class ShowDefaultDialog(val address: Address) : ActionIntent()
    class GetNewsDetails(val id: Long) : ActionIntent()

    /**
     * Languages actions intents
     * **/
    object GetLanguages : ActionIntent()
    class ChangeLanguage(val language: Language) : ActionIntent()
    object GetAllKeysAdnSignUpAsGuest : ActionIntent()
    object GetAllKeysFromDB : ActionIntent()
    object GetAllKeysFromNetwork : ActionIntent()

    /**
     * Request actions intents
     * **/
    object GetRequestedProductsActionIntent : ActionIntent()
    class CreateRequestForProduct(
        val contactName: String,
        val contactInformation: String,
        val productName: String,
        val description: String? = null,
        val categoryId: Long,
        val brandId: Long,
        val files: List<MultipartBody.Part?>?,
    ) : ActionIntent()

    class GetRequestById(val id: Long) : ActionIntent()
    object GetCategoriesWithBrands : ActionIntent()

    /**
     * Home page actions intents
     * **/
    object InitHomeData : ActionIntent()

    /**
     * Categories page actions intents
     * **/
    object GetCategoriesData : ActionIntent()

    /**
     * Products page actions intents
     * **/
    class GetProducts(val searchProductsModel: SearchProductsModel) : ActionIntent()
    class GetProductDetails(val productDetailsRequestModel: ProductDetailsRequestModel) : ActionIntent()
    class GetProductsCount(val searchProductsModel: SearchProductsModel) : ActionIntent()
    object GetFavoriteProducts : ActionIntent()
    class GetAttributeIds(val productAttributeIds: List<Long>) : ActionIntent()
    class FavoriteProductActionIntent(val productId: Long) : ActionIntent()
}
