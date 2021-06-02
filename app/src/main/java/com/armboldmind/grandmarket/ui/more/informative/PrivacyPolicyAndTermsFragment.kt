package com.armboldmind.grandmarket.ui.more.informative

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentPrivacyPolicyAndTermsBinding
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.launch

class PrivacyPolicyAndTermsFragment : BaseFragment<FragmentPrivacyPolicyAndTermsBinding>() {
    private val mArgs: PrivacyPolicyAndTermsFragmentArgs by navArgs()
    private val mInfoViewModel by lazy { createViewModel(InfoViewModel::class.java, this) }
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPrivacyPolicyAndTermsBinding
        get() = FragmentPrivacyPolicyAndTermsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentPrivacyPolicyAndTermsBinding, keysFromDb: Keys) {
        binding.apply {
            toolbarTitle.text = if (mArgs.isTerms) keysFromDb.terms_and_conditions else keysFromDb.privacy_police
            mInfoViewModel.sendIntent(if (mArgs.isTerms) ActionIntent.FetchTermsData else ActionIntent.FetchPrivacyData)
            if (mArgs.removePaddingBottom) scrollView.setPadding(0, 0, 0, 0)
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> {
                setLoading(true)
            }
            is ViewState.ErrorState -> {
                lifecycleScope.launch {
                    mBinding.loadingView.setViewState(ViewState.ErrorState(viewState.exception))
                    mBinding.loadingView.setOnButtonClick(mKeys.retry) { mInfoViewModel.sendIntent(if (mArgs.isTerms) ActionIntent.FetchTermsData else ActionIntent.FetchPrivacyData) }
                }
            }
            is ViewState.FetchPrivacyAndTermsState -> {
                setLoading(false)
                showHtml(viewState.termsAndPrivacyResponseModel.text ?: "")
            }
            else -> {
            }
        }
    }

    private fun showHtml(html: String) {
        mBinding.content.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(html)
    }
}