package com.armboldmind.grandmarket.ui.auth.forgotePassword

import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.SendCodeRequestModel
import com.armboldmind.grandmarket.databinding.FragmentForgotPasswordBinding
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setClickableSpan
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setSpannable
import com.armboldmind.grandmarket.shared.globalextensions.toTransitionGroup
import com.armboldmind.grandmarket.shared.managers.FirebaseAuthenticationManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.auth.AuthorizationActivity
import com.armboldmind.grandmarket.ui.auth.formValidators.ForgotPasswordFormValidator
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    private val mUserViewModel: UserViewModel by lazy { createViewModel(UserViewModel::class.java, this) }
    private val mForgotPasswordFormValidator: ForgotPasswordFormValidator by lazy { ForgotPasswordFormValidator() }
    private val mFirebaseManager by lazy { FirebaseAuthenticationManager.getInstance() }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentForgotPasswordBinding
        get() = FragmentForgotPasswordBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentForgotPasswordBinding, keysFromDb: Keys) {
        binding.apply {
            keys = keysFromDb
            initSignInSpannable(keysFromDb)
            next.setText(keysFromDb.next)
            next.setOnClickListener {
                hideSoftInput()
                mForgotPasswordFormValidator.isFormValid(keysFromDb, verificationCode, ::showSnackBar) { value, isEmail ->
                    if (isEmail) mUserViewModel.sendIntent(ActionIntent.VerifyUsernameActionIntent(SendCodeRequestModel(userName = value, email = value),isUsernameMustExist = true))
                    else mUserViewModel.sendIntent(ActionIntent.VerifyUsernameActionIntent(SendCodeRequestModel(userName = value, phoneNumber = value),isUsernameMustExist = true))
                }
            }
            initAnimation()
        }
    }

    private fun initSignInSpannable(keysFromDb: Keys) {
        val spannableString = SpannableString(keysFromDb.don_t_have_an_account_sign_up)
        val markedText = keysFromDb.sign_up
        val signInSpan = this.setClickableSpan(R.color.colorAccent, requireContext()) {
            lifecycleScope.launch {
                hideSoftInput()
                delay(200)
                val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                           mBinding.title.toTransitionGroup(),
                                                           mBinding.description.toTransitionGroup(),
                                                           mBinding.codeLayout.toTransitionGroup(),
                                                           mBinding.next.toTransitionGroup(),
                                                           mBinding.haveAccount.toTransitionGroup(),
                                                           mBinding.logo.toTransitionGroup())
                view?.findNavController()
                        ?.navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignUpFragment(), extrasSignIn)
            }
        }
        spannableString.setSpan(signInSpan, spannableString.indexOf(markedText), spannableString.indexOf(markedText) + markedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.haveAccount.setSpannable(spannableString)
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mBinding.next.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.CodeSentSuccessState -> navigateToForgotVerificationScreen(viewState.sendCodeRequestModel)
            is ViewState.ErrorState -> showError(message = viewState.exception)
            is ViewState.VerifyUserNameState -> lifecycleScope.launch {
                if (viewState.sendCodeRequestModel.email != null) mUserViewModel.sendIntent(ActionIntent.SendCodeForForgotPasswordActionIntent(viewState.sendCodeRequestModel))
                else mFirebaseManager.startAuthentication(activity as AuthorizationActivity, viewState.sendCodeRequestModel, ::handleViewState)
            }
            is ViewState.FirebaseCodeSentSuccessState -> navigateToForgotVerificationScreen(viewState.sendCodeRequestModel)
            is ViewState.VerifyCodeSuccessState -> navigateToRestorePasswordScreen(viewState.sendCodeRequestModel)
            else -> setLoading(false)
        }
    }

    private fun navigateToForgotVerificationScreen(sendCodeRequestModel: SendCodeRequestModel) {
        setLoading(false)
        val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                   mBinding.title.toTransitionGroup(),
                                                   mBinding.description.toTransitionGroup(),
                                                   mBinding.codeLayout.toTransitionGroup(),
                                                   mBinding.next.toTransitionGroup(),
                                                   mBinding.haveAccount.toTransitionGroup(),
                                                   mBinding.logo.toTransitionGroup())
        view?.findNavController()
                ?.navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotVerificationFragment(sendCodeRequestModel), extrasSignIn)
    }

    private fun navigateToRestorePasswordScreen(sendCodeRequestModel: SendCodeRequestModel) {
        setLoading(false)
        val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                   mBinding.title.toTransitionGroup(),
                                                   mBinding.description.toTransitionGroup(),
                                                   mBinding.codeLayout.toTransitionGroup(),
                                                   mBinding.next.toTransitionGroup(),
                                                   mBinding.haveAccount.toTransitionGroup(),
                                                   mBinding.logo.toTransitionGroup())
        view?.findNavController()
                ?.navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToRestorePasswordFragment(sendCodeRequestModel), extrasSignIn)
    }
}