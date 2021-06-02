package com.armboldmind.grandmarket.ui.auth.forgotePassword

import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.SendCodeRequestModel
import com.armboldmind.grandmarket.databinding.FragmentForgotVerificationBinding
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setClickableSpan
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setSpannable
import com.armboldmind.grandmarket.shared.globalextensions.gone
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.globalextensions.startTimer
import com.armboldmind.grandmarket.shared.globalextensions.toTransitionGroup
import com.armboldmind.grandmarket.shared.managers.FirebaseAuthenticationManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.shared.validations.CodeValidator
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForgotVerificationFragment : BaseFragment<FragmentForgotVerificationBinding>() {

    private val mUserViewModel: UserViewModel by lazy { createViewModel(UserViewModel::class.java, this) }
    private val mArgs: ForgotVerificationFragmentArgs by navArgs()
    private val mFirebaseManager by lazy { FirebaseAuthenticationManager.getInstance() }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentForgotVerificationBinding
        get() = FragmentForgotVerificationBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentForgotVerificationBinding, keysFromDb: Keys) {
        binding.apply {
            keys = keysFromDb
            initSignInSpannable(keysFromDb)
            initDescription(keysFromDb)
            startTimer(keysFromDb)
            resend.setOnClickListener {
                resend.isEnabled = false
                verificationCode.text?.clear()
                if (mArgs.sendCodeRequestModel.phoneNumber.isNullOrEmpty()) mUserViewModel.sendIntent(ActionIntent.SendCodeForForgotPasswordActionIntent(mArgs.sendCodeRequestModel.apply {
                    code = null
                }))
                else {
                    mFirebaseManager.resendCode(::handleViewState)
                    setLoading(true)
                }
            }
            verify.setText(keysFromDb.verify)
            verify.setOnClickListener {
                hideSoftInput()
                CodeValidator().validate(keysFromDb, verificationCode, { showSnackBar(it) }) { code ->
                    if (mArgs.sendCodeRequestModel.phoneNumber.isNullOrEmpty()) mUserViewModel.sendIntent(ActionIntent.VerifyCodeActionIntent(mArgs.sendCodeRequestModel.apply {
                        this.code = code
                    }))
                    else {
                        mFirebaseManager.verifyCode(mArgs.sendCodeRequestModel.apply { this.code = code }, ::handleViewState)
                        setLoading(true)
                    }
                }
            }
            initAnimation()
        }
    }

    private fun initDescription(keysFromDb: Keys) {
        mArgs.sendCodeRequestModel.phoneNumber?.let {
            mBinding.description.text = keysFromDb.sent_code_to_phone.replace("#PHONE#", it)
        }
        mArgs.sendCodeRequestModel.email?.let {
            mBinding.description.text = keysFromDb.sent_code_to_email.replace("#EMAIL#", it)
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
                                                           mBinding.verify.toTransitionGroup(),
                                                           mBinding.haveAccount.toTransitionGroup(),
                                                           mBinding.logo.toTransitionGroup())
                view?.findNavController()
                        ?.navigate(ForgotVerificationFragmentDirections.actionForgotVerificationFragmentToSignUpFragment(), extrasSignIn)
            }
        }
        spannableString.setSpan(signInSpan, spannableString.indexOf(markedText), spannableString.indexOf(markedText) + markedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.haveAccount.setSpannable(spannableString)
    }

    private fun startTimer(keysFromDb: Keys) {
        mBinding.timer.startTimer(lifecycleScope, keysFromDb) {
            mBinding.resend.show()
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mArgs.sendCodeRequestModel.code?.let { mBinding.verify.setIsLoading(isLoading) }
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> showError(message = viewState.exception)
            is ViewState.VerifyCodeSuccessState -> navigateToRestorePasswordScreen(viewState.sendCodeRequestModel)
            is ViewState.FirebaseVerifyCodeSuccessState -> navigateToRestorePasswordScreen(viewState.sendCodeRequestModel)
            is ViewState.CodeSentSuccessState -> lifecycleScope.launch {
                startTimer(mKeys)
                mBinding.resend.gone()
                mBinding.resend.isEnabled = true
                setLoading(false)
            }
            is ViewState.FirebaseCodeSentSuccessState -> lifecycleScope.launch {
                startTimer(mKeys)
                mBinding.resend.gone()
                mBinding.resend.isEnabled = true
                setLoading(false)
            }
            else -> setLoading(false)
        }
    }

    private fun navigateToRestorePasswordScreen(sendCodeRequestModel: SendCodeRequestModel) {
        setLoading(false)
        val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                   mBinding.title.toTransitionGroup(),
                                                   mBinding.description.toTransitionGroup(),
                                                   mBinding.codeLayout.toTransitionGroup(),
                                                   mBinding.verify.toTransitionGroup(),
                                                   mBinding.haveAccount.toTransitionGroup(),
                                                   mBinding.logo.toTransitionGroup())
        view?.findNavController()
                ?.navigate(ForgotVerificationFragmentDirections.actionForgotVerificationFragmentToRestorePasswordFragment(sendCodeRequestModel), extrasSignIn)
    }
}