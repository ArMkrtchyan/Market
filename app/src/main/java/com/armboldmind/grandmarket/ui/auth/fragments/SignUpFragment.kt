package com.armboldmind.grandmarket.ui.auth.fragments

import android.text.SpannableString
import android.text.Spanned
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.SendCodeRequestModel
import com.armboldmind.grandmarket.databinding.FragmentSignUpBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import com.armboldmind.grandmarket.shared.formatters.Formatter
import com.armboldmind.grandmarket.shared.formatters.IFormatter
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setClickableSpan
import com.armboldmind.grandmarket.shared.globalextensions.Spannable.setSpannable
import com.armboldmind.grandmarket.shared.globalextensions.getDrawableCompat
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.globalextensions.toTransitionGroup
import com.armboldmind.grandmarket.shared.managers.FirebaseAuthenticationManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.shared.utils.AnimationUtil
import com.armboldmind.grandmarket.ui.auth.AuthorizationActivity
import com.armboldmind.grandmarket.ui.auth.dialogs.DatePickerDialog
import com.armboldmind.grandmarket.ui.auth.formValidators.SignUpFormValidator
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import com.armboldmind.grandmarket.ui.more.addresses.dialogs.EditAddressDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    private var mIsEmailChosen = false
    private val mFormatter: IFormatter by lazy { Formatter() }
    private val mUserViewModel: UserViewModel by lazy { createViewModel(UserViewModel::class.java, this) }
    private val mSignUpFormValidator by lazy { SignUpFormValidator() }
    private val mFirebaseManager by lazy { FirebaseAuthenticationManager.getInstance() }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignUpBinding
        get() = FragmentSignUpBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentSignUpBinding, keysFromDb: Keys) {
        binding.apply {
            isEmailChoosed = mIsEmailChosen
            keys = keysFromDb
            phoneLayout.prefixTextView.apply {
                updateLayoutParams {
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                gravity = Gravity.CENTER
                isClickable = true
                isFocusable = true
            }
            initSignInSpannable(keysFromDb)
            signIn.setText(keysFromDb.receive_code)
            signIn.setOnClickListener {
                hideSoftInput()
                if (mIsEmailChosen) {
                    mSignUpFormValidator.isEmailFormValid(keysFromDb, fullName, dateOfBirth, email, { showSnackBar(it) }) { fullName, dateOfBirth, email ->
                        mUserViewModel.sendIntent(ActionIntent.VerifyUsernameActionIntent(SendCodeRequestModel(email = email,
                                                                                                               userName = email,
                                                                                                               fullName = fullName,
                                                                                                               dateOfBirth = dateOfBirth),isUsernameMustExist = false))
                    }
                } else {
                    mSignUpFormValidator.isPhoneFormValid(keysFromDb, fullName, dateOfBirth, phone, { showSnackBar(it) }) { fullName, dateOfBirth, phone ->
                        mUserViewModel.sendIntent(ActionIntent.VerifyUsernameActionIntent(SendCodeRequestModel(phoneNumber = phone,
                                                                                                               userName = phone,
                                                                                                               fullName = fullName,
                                                                                                               dateOfBirth = dateOfBirth),isUsernameMustExist = false))
                    }
                }
            }
            changePhoneOrEmail.setOnClickListener {
                mIsEmailChosen = !mIsEmailChosen;isEmailChoosed = mIsEmailChosen
                if (mIsEmailChosen) {
                    emailLayout.transitionName = "PasswordLayoutTransitionName"
                    phoneLayout.transitionName = ""
                } else {
                    emailLayout.transitionName = ""
                    phoneLayout.transitionName = "PasswordLayoutTransitionName"
                }
                if (email.isFocused || phone.isFocused) if (mIsEmailChosen) email.requestFocus() else phone.requestFocus()
                fullName.background = requireContext().getDrawableCompat(R.drawable.background_rounded_4_bordered)
                dateOfBirth.background = requireContext().getDrawableCompat(R.drawable.background_rounded_4_bordered)
            }
            dateOfBirth.setOnClickListener {
                hideSoftInput()
                val datePickerDialog = DatePickerDialog(dateOfBirth.text.toString()) {
                    dateOfBirth.setText((mFormatter.formatToPattern(it, DatePatternsEnum.DAY_MONTH_YEAR)))
                }
                datePickerDialog.show(childFragmentManager, EditAddressDialog::class.java.name)
            }
            AnimationUtil.alphaFrom0To1(dateOfBirthLayout)
            initAnimation()
        }
    }


    private fun initSignInSpannable(keysFromDb: Keys) {
        val spannableString = SpannableString(keysFromDb.already_have_an_account_sign_in)
        val markedText = keysFromDb.sign_in
        val signInSpan = this.setClickableSpan(R.color.colorAccent, requireContext()) {
            lifecycleScope.launch {
                hideSoftInput()
                delay(200)
                val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                           mBinding.title.toTransitionGroup(),
                                                           mBinding.description.toTransitionGroup(),
                                                           mBinding.fullNameLayout.toTransitionGroup(),
                                                           mBinding.signIn.toTransitionGroup(),
                                                           mBinding.haveAccount.toTransitionGroup(),
                                                           mBinding.logo.toTransitionGroup())
                view?.findNavController()
                        ?.navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(), extrasSignIn)
            }
        }
        spannableString.setSpan(signInSpan, spannableString.indexOf(markedText), spannableString.indexOf(markedText) + markedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.haveAccount.setSpannable(spannableString)
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mBinding.signIn.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.CodeSentSuccessState -> navigateToVerificationScreen(viewState.sendCodeRequestModel)
            is ViewState.ErrorState -> showError(message = viewState.exception)
            is ViewState.VerifyUserNameState -> lifecycleScope.launch {
                if (mIsEmailChosen) {
                    mUserViewModel.sendIntent(ActionIntent.SendCodeActionIntent(viewState.sendCodeRequestModel))
                } else {
                    startFirebaseAuthentication(viewState.sendCodeRequestModel)
                }
            }
            is ViewState.FirebaseCodeSentSuccessState -> navigateToVerificationScreen(viewState.sendCodeRequestModel)
            is ViewState.VerifyCodeSuccessState -> navigateToCreatePasswordScreen(viewState.sendCodeRequestModel)
            else -> setLoading(false)
        }
    }

    private fun startFirebaseAuthentication(sendCodeRequestModel: SendCodeRequestModel) {
        setLoading(false)
        mFirebaseManager.startAuthentication(activity as AuthorizationActivity, sendCodeRequestModel, ::handleViewState)
    }

    private fun navigateToVerificationScreen(sendCodeRequestModel: SendCodeRequestModel) {
        setLoading(false)
        val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                   mBinding.title.toTransitionGroup(),
                                                   mBinding.description.toTransitionGroup(),
                                                   mBinding.fullNameLayout.toTransitionGroup(),
                                                   mBinding.signIn.toTransitionGroup(),
                                                   mBinding.haveAccount.toTransitionGroup(),
                                                   mBinding.logo.toTransitionGroup())
        view?.findNavController()
                ?.navigate(SignUpFragmentDirections.actionSignUpFragmentToVerificationFragment(sendCodeRequestModel), extrasSignIn)
    }

    private fun navigateToCreatePasswordScreen(sendCodeRequestModel: SendCodeRequestModel) {
        setLoading(false)
        val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                   mBinding.title.toTransitionGroup(),
                                                   mBinding.description.toTransitionGroup(),
                                                   mBinding.fullNameLayout.toTransitionGroup(),
                                                   mBinding.signIn.toTransitionGroup(),
                                                   mBinding.haveAccount.toTransitionGroup(),
                                                   mBinding.logo.toTransitionGroup())
        view?.findNavController()
                ?.navigate(SignUpFragmentDirections.actionSignUpFragmentToCreatePasswordFragment(sendCodeRequestModel), extrasSignIn)
    }

    override fun onResume() {
        super.onResume()
        (activity as AuthorizationActivity).updateResources(preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).uniqueSeoCode)
    }
}