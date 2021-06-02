package com.armboldmind.grandmarket.ui.more.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentSettingsBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.UserRoleEnum
import com.armboldmind.grandmarket.shared.globalextensions.gone
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.MainActivity
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import com.armboldmind.grandmarket.ui.init.LanguageViewModel


class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    private val mLanguagesViewModel by lazy { createViewModel(LanguageViewModel::class.java, this) }
    private val mUserViewModel: UserViewModel by lazy { (activity as MainActivity).createViewModel(UserViewModel::class.java, this) }
    private var currentLanguage: Int = preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).id
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentSettingsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            val userRole: UserRoleEnum? = preferencesManager().findByKey(BundleKeysEnum.USER_ROLE.key)
            if (userRole != UserRoleEnum.USER) {
                notifications.gone()
                notifications.setOnClickListener(null)
            } else {
                notifications.show()
                notifications.setOnClickListener {
                    view?.findNavController()
                        ?.navigate(SettingsFragmentDirections.actionSettingsFragmentToNotificationsSettingsFragment())
                }
            }
            languages.setOnClickListener {
                view?.findNavController()
                    ?.navigate(SettingsFragmentDirections.actionSettingsFragmentToLanguageSettingsFragment())
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {}
    override fun handleViewState(viewState: ViewState) {}
    override fun onResume() {
        super.onResume()
        if (currentLanguage != preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).id) {
            currentLanguage = preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).id
            keysLiveData().observe(viewLifecycleOwner) { initView(mBinding, it) }
        }
    }
}