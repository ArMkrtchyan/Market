package com.armboldmind.grandmarket.ui.more.personalInformation

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.SendCodeRequestModel
import com.armboldmind.grandmarket.databinding.FragmentChangeLoginInfoBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.managers.FirebaseAuthenticationManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.MainActivity
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class ChangeLoginInfoFragment : BaseFragment<FragmentChangeLoginInfoBinding>() {
    private val mUserViewModel: UserViewModel by lazy { createViewModel(UserViewModel::class.java, this) }
    private val mArgs: ChangeLoginInfoFragmentArgs by navArgs()
    private val mChangeLoginInfoFormValidator by lazy { ChangeLoginInfoFormValidator() }
    private val mFirebaseManager by lazy { FirebaseAuthenticationManager.getInstance() }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChangeLoginInfoBinding
        get() = FragmentChangeLoginInfoBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentChangeLoginInfoBinding, keysFromDb: Keys) {
        binding.apply {
            isEmailChoosed = mArgs.isEmailChoosed
            keys = keysFromDb
            val mUser = preferencesManager().findByKey<User>(BundleKeysEnum.USER.key)
            user = mUser
            phoneLayout.prefixTextView.apply {
                updateLayoutParams {
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                gravity = Gravity.CENTER
                isClickable = true
                isFocusable = true
            }
            signIn.setText(keysFromDb.receive_code)
            signIn.setOnClickListener {
                if (mArgs.isEmailChoosed) mChangeLoginInfoFormValidator.isEmailFormValid(email) {
                    mUserViewModel.sendIntent(ActionIntent.VerifyUsernameActionIntent(signInData = SendCodeRequestModel(email = it, userName = it), isUsernameMustExist = false))
                } else mChangeLoginInfoFormValidator.isPhoneFormValid(phone) {
                    mUserViewModel.sendIntent(ActionIntent.VerifyUsernameActionIntent(signInData = SendCodeRequestModel(phoneNumber = it, userName = it),
                                                                                      isUsernameMustExist = false))
                }
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mBinding.signIn.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.CodeSentSuccessState -> navigateToVerificationScreen(viewState.sendCodeRequestModel)
            is ViewState.FirebaseCodeSentSuccessState -> navigateToVerificationScreen(viewState.sendCodeRequestModel)
            is ViewState.VerifyUserNameState -> lifecycleScope.launch {
                if (mArgs.isEmailChoosed) mUserViewModel.sendIntent(ActionIntent.SendCodeForEmailActionIntent(viewState.sendCodeRequestModel))
                else mFirebaseManager.startAuthentication(activity as MainActivity, viewState.sendCodeRequestModel, ::handleViewState)
            }
            is ViewState.ErrorState -> showError(message = viewState.exception)
            else -> setLoading(false)
        }
    }

    private fun navigateToVerificationScreen(sendCodeRequestModel: SendCodeRequestModel) {
        setLoading(false)
        view?.findNavController()
                ?.navigate(ChangeLoginInfoFragmentDirections.actionChangeLoginInfoFragmentToVerifyLoginInfoFragment(sendCodeRequestModel.phoneNumber, sendCodeRequestModel.email))
    }
}