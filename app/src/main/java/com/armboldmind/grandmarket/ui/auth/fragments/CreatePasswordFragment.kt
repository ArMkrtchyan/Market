package com.armboldmind.grandmarket.ui.auth.fragments

import android.app.Activity
import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.signUpRequestModel
import com.armboldmind.grandmarket.databinding.FragmentCreatePasswordBinding
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setClickableSpan
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setSpannable
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.auth.AuthorizationActivity
import com.armboldmind.grandmarket.ui.auth.formValidators.CreatePasswordFormValidator
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel

class CreatePasswordFragment : BaseFragment<FragmentCreatePasswordBinding>() {
    private val mCreatePasswordFormValidator by lazy { CreatePasswordFormValidator() }
    private val mUserViewModel: UserViewModel by lazy { createViewModel(UserViewModel::class.java, this) }
    private val mArgs: CreatePasswordFragmentArgs by navArgs()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreatePasswordBinding
        get() = FragmentCreatePasswordBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentCreatePasswordBinding, keysFromDb: Keys) {
        binding.apply {
            keys = keysFromDb
            initSignUpSpannable(keysFromDb)
            initAnimation()
            signUp.setText(keysFromDb.sign_up)
            signUp.setOnClickListener {
                hideSoftInput()
                mCreatePasswordFormValidator.isFormValid(keysFromDb, password, repeatPassword, ::showSnackBar) { pass, _ ->
                    if (checkbox.isChecked) mUserViewModel.sendIntent(ActionIntent.SignUpActionIntent(signUpRequestModel = signUpRequestModel {
                        fullName = mArgs.sendCodeRequestModel.fullName
                        userName = mArgs.sendCodeRequestModel.userName
                        dateOfBirth = mArgs.sendCodeRequestModel.dateOfBirth
                        email = mArgs.sendCodeRequestModel.email
                        phoneNumber = mArgs.sendCodeRequestModel.phoneNumber
                        uid = mArgs.sendCodeRequestModel.uid
                        password = pass
                    })) else showSnackBar(keysFromDb.please_read_terms_and_conditions)
                }
            }
        }
    }

    private fun initSignUpSpannable(keysFromDb: Keys) {
        val spannableString = SpannableString(keysFromDb.confirm_terms_and_policy)
        val markedTextTerms = keysFromDb.terms_and_conditions
        val markedTextPrivacy = keysFromDb.privacy_police
        val termsSpan = this.setClickableSpan(R.color.black, requireContext()) {
            hideSoftInput()
            view?.findNavController()
                ?.navigate(CreatePasswordFragmentDirections.actionCreatePasswordFragmentToPrivacyPolicyFragment2(isTerms = true, removePaddingBottom = true))
        }
        val privacySpan = this.setClickableSpan(R.color.black, requireContext()) {
            hideSoftInput()
            view?.findNavController()
                ?.navigate(CreatePasswordFragmentDirections.actionCreatePasswordFragmentToPrivacyPolicyFragment2(isTerms = false, removePaddingBottom = true))
        }
        spannableString.setSpan(termsSpan,
                                spannableString.indexOf(markedTextTerms),
                                spannableString.indexOf(markedTextTerms) + markedTextTerms.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(privacySpan,
                                spannableString.indexOf(markedTextPrivacy),
                                spannableString.indexOf(markedTextPrivacy) + markedTextPrivacy.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.termsAndPrivacy.setSpannable(spannableString)
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mBinding.signUp.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) = when (viewState) {
        is ViewState.LoadingState -> setLoading(true)
        is ViewState.SuccessState -> leaveAuthScreens()
        is ViewState.ErrorState -> showError(message = viewState.exception)
        else -> Unit
    }

    private fun leaveAuthScreens() {
        (activity as AuthorizationActivity).apply {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

}