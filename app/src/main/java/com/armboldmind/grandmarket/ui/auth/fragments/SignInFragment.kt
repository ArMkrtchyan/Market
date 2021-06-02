package com.armboldmind.grandmarket.ui.auth.fragments

import android.annotation.SuppressLint
import android.app.Activity
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
import com.armboldmind.grandmarket.data.models.requestmodels.SignInRequestModel
import com.armboldmind.grandmarket.databinding.FragmentSignInBinding
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setClickableSpan
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setSpannable
import com.armboldmind.grandmarket.shared.globalextensions.toTransitionGroup
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.shared.utils.AnimationUtil
import com.armboldmind.grandmarket.ui.auth.AuthorizationActivity
import com.armboldmind.grandmarket.ui.auth.formValidators.SignInFormValidator
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    private val mSignInFormValidator by lazy { SignInFormValidator() }
    private val mUserViewModel: UserViewModel by lazy { createViewModel(UserViewModel::class.java, this) }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignInBinding
        get() = FragmentSignInBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    @SuppressLint("SetTextI18n")
    override fun initView(binding: FragmentSignInBinding, keysFromDb: Keys) {
        binding.apply {
            keys = keysFromDb
            forgotPassword.setOnClickListener {
                lifecycleScope.launch {
                    hideSoftInput()
                    delay(200)
                    val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                               mBinding.title.toTransitionGroup(),
                                                               mBinding.description.toTransitionGroup(),
                                                               mBinding.phoneOrEmailLayout.toTransitionGroup(),
                                                               mBinding.signIn.toTransitionGroup(),
                                                               mBinding.haveAccount.toTransitionGroup(),
                                                               mBinding.logo.toTransitionGroup())
                    view?.findNavController()
                        ?.navigate(SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment(), extrasSignIn)
                }
            }
            signIn.setText(keysFromDb.sign_in)
            signIn.setOnClickListener {
                hideSoftInput()
                mSignInFormValidator.isFormValid(keysFromDb, password, phoneOrEmail, { showSnackBar(it) }) { username, password ->
                    mUserViewModel.sendIntent(ActionIntent.SignInActionIntent(SignInRequestModel(username, password)))
                }
            }
            AnimationUtil.alphaFrom0To1(forgotPassword)
            AnimationUtil.alphaFrom0To1(passwordLayout)
            initSignUpSpannable(keysFromDb)
            initAnimation()
        }
    }

    private fun initSignUpSpannable(keysFromDb: Keys) {
        val spannableString = SpannableString(keysFromDb.don_t_have_an_account_sign_up)
        val markedText = keysFromDb.sign_up
        val signUpSpan = this.setClickableSpan(R.color.colorAccent, requireContext()) {
            lifecycleScope.launch {
                hideSoftInput()
                delay(200)
                val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                           mBinding.title.toTransitionGroup(),
                                                           mBinding.description.toTransitionGroup(),
                                                           mBinding.phoneOrEmailLayout.toTransitionGroup(),
                                                           mBinding.passwordLayout.toTransitionGroup(),
                                                           mBinding.signIn.toTransitionGroup(),
                                                           mBinding.haveAccount.toTransitionGroup(),
                                                           mBinding.logo.toTransitionGroup())
                view?.findNavController()
                    ?.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment(), extrasSignIn)
            }
        }
        spannableString.setSpan(signUpSpan, spannableString.indexOf(markedText), spannableString.indexOf(markedText) + markedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.haveAccount.setSpannable(spannableString)
    }

    private fun leaveAuthScreens() {
        (activity as AuthorizationActivity).apply {
            setResult(Activity.RESULT_OK)
            finish()
            overridePendingTransition(R.anim.fade_in_500, R.anim.fade_out_500)
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mBinding.signIn.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.SuccessState -> leaveAuthScreens()
            is ViewState.ErrorState -> showError(message = viewState.exception)
            else -> {
            }
        }
    }

}