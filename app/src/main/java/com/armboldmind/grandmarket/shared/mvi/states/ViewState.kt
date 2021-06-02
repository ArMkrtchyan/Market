package com.armboldmind.grandmarket.shared.mvi.states

import androidx.paging.PagingData
import com.armboldmind.grandmarket.data.models.domain.*
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.SendCodeRequestModel
import com.armboldmind.grandmarket.data.models.responsemodels.TermsAndPrivacyResponseModel

sealed class ViewState {
    object LoadingState : ViewState()
    object SecondaryLoadingState : ViewState()
    object LoadingViewState : ViewState()
    class ErrorState(val exception: Throwable) : ViewState()
    class SuccessState(val message: String? = null) : ViewState()
    object EmptyState : ViewState()

    class VerifyUserNameState(val sendCodeRequestModel: SendCodeRequestModel) : ViewState()
    class CodeSentSuccessState(val sendCodeRequestModel: SendCodeRequestModel) : ViewState()
    class VerifyCodeSuccessState(val sendCodeRequestModel: SendCodeRequestModel) : ViewState()
    class FirebaseCodeSentSuccessState(val sendCodeRequestModel: SendCodeRequestModel) : ViewState()
    class FirebaseVerifyCodeSuccessState(val sendCodeRequestModel: SendCodeRequestModel) : ViewState()
    class FetchUserItemsState(val items: List<More>) : ViewState()
    class FetchGlobalItemsState(val items: List<More>) : ViewState()
    class FetchUserPushSubscribeState(val items: List<Int>) : ViewState()
    class FetchLanguages(val items: List<Language>) : ViewState()
    class ChangeLanguageState(val language: Language) : ViewState()
    class FetchNotifications(val list: PagingData<Notification>) : ViewState()

    object MoreSignedOutState : ViewState()
    class FetchNewsData(val news: PagingData<NewsAndEvents>) : ViewState()
    class FetchFaqData(val faqQuestionList: List<FAQ>, val faqAnswerList: List<FAQ>) : ViewState()
    class FetchPrivacyAndTermsState(val termsAndPrivacyResponseModel: TermsAndPrivacyResponseModel) : ViewState()

    object OpenGallery : ViewState()
    object OpenCamera : ViewState()
    object DeletePhoto : ViewState()

    class GetAllAddresses(val addresses: List<Address>) : ViewState()
    class EditAddress(val address: Address) : ViewState()
    class ShowDeleteDialog(val address: Address) : ViewState()
    class ShowDefaultDialog(val address: Address) : ViewState()


    class GetAllCards(val cards: List<Card>) : ViewState()
    class FetchNewsDetails(val news: NewsAndEventsDetails) : ViewState()

    object AddNewCard : ViewState()
    object DeleteCard : ViewState()
    class FetchKeys(val keys: Keys) : ViewState()

    class FetchRequests(val list: PagingData<RequestProduct>) : ViewState()
    class FetchRequestById(val list: RequestProduct) : ViewState()

    class FetchHomePageData(
        val banners: List<Banner>,
        val categories: List<Category>,
        val newArrivals: List<Product>,
        val bestSellers: List<Product>,
        val discounts: List<Product>,
    ) : ViewState()

    class FetchCategoriesWithBrands(
        val brands: List<Brand>,
        val categories: List<Category>,
    ) : ViewState()

    class FetchCategoriesData(
        val categories: List<Category>,
    ) : ViewState()

    object RequestForProductCreated : ViewState()
    object FavoriteProduct : ViewState()
    class FetchProducts(val list: PagingData<Product>) : ViewState()
    class FetchProductDetails(val productDetails: ProductDetails, val similarProducts: List<Product>) : ViewState()
    class FetchProductsCount(val count: Long) : ViewState()
    class UpdateAttributesByIds(val attributeIds: List<Long>) : ViewState()
}
