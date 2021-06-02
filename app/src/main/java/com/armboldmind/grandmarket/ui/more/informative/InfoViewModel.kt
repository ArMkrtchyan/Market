package com.armboldmind.grandmarket.ui.more.informative

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.IInfoRepository
import com.armboldmind.grandmarket.data.mappers.FAQMapper
import com.armboldmind.grandmarket.data.models.domain.FAQ
import com.armboldmind.grandmarket.data.models.requestmodels.ContactUsRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.SearchRequestModel
import com.armboldmind.grandmarket.shared.enums.TermsAndPrivacyEnum
import com.armboldmind.grandmarket.shared.globalextensions.appComponent
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InfoViewModel : BaseViewModel() {
    @Inject
    lateinit var mInfoRepository: IInfoRepository

    init {
        appComponent().infoComponent.build()
            .inject(this)
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.d("ExceptionTag", throwable.localizedMessage ?: "")
        }) {
            when (actionIntent) {
                is ActionIntent.FetchTermsData -> getTermsAndPrivacy(TermsAndPrivacyEnum.TERMS_AND_CONDITION.value)
                is ActionIntent.FetchPrivacyData -> getTermsAndPrivacy(TermsAndPrivacyEnum.PRIVACY_AND_POLICY.value)
                is ActionIntent.FetchFAQData -> getFAQ(actionIntent.search, actionIntent.isNeedToSecondLoading)
                is ActionIntent.SendContactData -> contactUs(actionIntent.data)
                is ActionIntent.FetchNewsAndEventsData -> getNewsAndEvents()
                is ActionIntent.GetNewsDetails -> getNewsById(actionIntent.id)
                else -> Unit
            }
        }
    }

    private suspend fun getTermsAndPrivacy(type: Int) {
        mInfoRepository.getTermsAndPrivacy(type)
            .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect { setState.invoke(ViewState.FetchPrivacyAndTermsState(it)) }
    }

    private suspend fun getFAQ(search: SearchRequestModel, needToSecondLoading: Boolean) {
        mInfoRepository.getFAQ(search)
            .onStart {
                withContext(Dispatchers.Main) {
                    setState.invoke(if (needToSecondLoading) ViewState.SecondaryLoadingState else ViewState.LoadingState)
                }
            }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                val faqMapper = FAQMapper()
                val faqQuestionList = arrayListOf<FAQ>()
                val faqAnswerList = arrayListOf<FAQ>()
                it.map { faqResponseModel ->
                    val faq = faqMapper.map(faqResponseModel)
                    faqQuestionList.add(faq)
                    faqQuestionList.add(faqMapper.mapToLine(faq))
                    faqAnswerList.add(faqMapper.mapToAnswer(faq))
                }
                if (it.isEmpty()) setState.invoke(ViewState.EmptyState) else setState.invoke(ViewState.FetchFaqData(faqQuestionList, faqAnswerList))
            }
    }

    private suspend fun contactUs(data: ContactUsRequestModel) {
        mInfoRepository.contactUs(data)
            .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect { setState.invoke(ViewState.SuccessState()) }
    }

    private suspend fun getNewsAndEvents() {
        mInfoRepository.getNewsAndEvents()
            .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                setState.invoke(ViewState.FetchNewsData(it))
            }
    }

    private suspend fun getNewsById(newsId: Long) {
        mInfoRepository.getNewsById(newsId)
            .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                setState.invoke(ViewState.FetchNewsDetails(it))
            }
    }
}