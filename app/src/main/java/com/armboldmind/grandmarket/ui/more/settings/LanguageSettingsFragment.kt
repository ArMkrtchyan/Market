package com.armboldmind.grandmarket.ui.more.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentLanguageSettingsBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.MainActivity
import com.armboldmind.grandmarket.ui.init.LanguageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LanguageSettingsFragment : BaseFragment<FragmentLanguageSettingsBinding>() {
    private val mLanguagesViewModel by lazy { createViewModel(LanguageViewModel::class.java, this) }
    private val mAdapter by lazy {
        AdapterSettingsLanguage {
            lifecycleScope.launch {
                delay(200)
                mCurrentActionIntent = ActionIntent.ChangeLanguage(it)
                mLanguagesViewModel.sendIntent(ActionIntent.ChangeLanguage(it))
            }
        }
    }
    private var mCurrentActionIntent: ActionIntent = ActionIntent.GetLanguages

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLanguageSettingsBinding
        get() = FragmentLanguageSettingsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentLanguageSettingsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            if (mAdapter.currentList.isEmpty()) mLanguagesViewModel.sendIntent(mCurrentActionIntent)
            languages.adapter = mAdapter
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> lifecycleScope.launch {
                delay(100)
                showError(message = viewState.exception) { mLanguagesViewModel.sendIntent(mCurrentActionIntent) }
            }
            is ViewState.FetchLanguages -> fetchLanguages(viewState.items)
            is ViewState.ChangeLanguageState -> {
                (activity as MainActivity).updateResources(viewState.language.uniqueSeoCode)
                setLoading(false)
            }
            else -> setLoading(false)
        }
    }

    private fun fetchLanguages(items: List<Language>) {
        setLoading(false)
        val currentLanguage: Language = preferencesManager().findByKey(BundleKeysEnum.APP_LANGUAGE.key)
        mAdapter.submitList(items.apply { map { it.isSelected = currentLanguage.id == it.id } })
        mBinding.main.show()
    }
}