package com.armboldmind.grandmarket.ui.init

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseActivity
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.databinding.ActivityChooseLanguageBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.LanguagesEnum
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.shared.utils.AnimationUtil
import com.armboldmind.grandmarket.ui.MainActivity
import kotlinx.coroutines.launch

class ChooseLanguageActivity : BaseActivity<ActivityChooseLanguageBinding>() {

    companion object {
        private const val TITLE_ARMENIAN = "Ընտրեք ձեր ցուցադրման լեզուն"
        private const val TITLE_RUSSIAN = "Выберите язык отображения"
        private const val TITLE_ENGLISH = "Select your display language"
        private const val BUTTON_TEXT_ARMENIAN = "Հաջորդ"
        private const val BUTTON_TEXT_RUSSIAN = "Далее"
        private const val BUTTON_TEXT_ENGLISH = "Next"
    }

    private val mLanguagesViewModel by lazy { createViewModel(LanguageViewModel::class.java, this) }
    private var mCurrentActionIntent: ActionIntent = ActionIntent.GetLanguages
    private var mSelectedLanguage: Language? = null
    private val mAdapter by lazy {
        AdapterChooseLanguage {
            mSelectedLanguage = it
            if (!mBinding.next.isVisible) {
                mBinding.nextGroup.show()
                AnimationUtil.alphaFrom0To1(mBinding.next)
            }
        }
    }
    override val inflate: (LayoutInflater) -> ActivityChooseLanguageBinding
        get() = ActivityChooseLanguageBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {
            lifecycleOwner = this@ChooseLanguageActivity
            mLanguagesViewModel.sendIntent(mCurrentActionIntent)
            selectedLanguage = LanguagesEnum.NONE.code
            languages.adapter = mAdapter
            next.setOnClickListener {
                mSelectedLanguage?.let {
                    preferencesManager().saveByKey(BundleKeysEnum.APP_LANGUAGE.key, it)
                }
                mCurrentActionIntent = ActionIntent.GetAllKeysAdnSignUpAsGuest
                mLanguagesViewModel.sendIntent(mCurrentActionIntent)
            }
        }
    }

    private fun navigateToSignInScreen() {
        setLoading(false)
        startActivity(android.content.Intent(this@ChooseLanguageActivity, MainActivity::class.java))
        overridePendingTransition(R.anim.fade_in_500, 0)
        finish()
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.SuccessState -> navigateToSignInScreen()
            is ViewState.FetchLanguages -> fetchLanguages(viewState.items)
            is ViewState.ErrorState -> {
                lifecycleScope.launch {
                    mBinding.loadingView.setViewState(ViewState.ErrorState(viewState.exception))
                    mBinding.loadingView.setOnButtonClick(getString(R.string.retry)) { mLanguagesViewModel.sendIntent(mCurrentActionIntent) }
                }
            }
            else -> setLoading(false)
        }
    }

    private fun fetchLanguages(items: List<Language>) {
        setLoading(false)
        mAdapter.submitList(items)
        mBinding.main.show()
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        if (mCurrentActionIntent is ActionIntent.GetLanguages) mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
        else mBinding.next.setIsLoading(isLoading)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.white)
    }
}