package com.armboldmind.grandmarket.ui.more.personalInformation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.SendCodeRequestModel
import com.armboldmind.grandmarket.databinding.FragmentVerifyLoginInfoBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.globalextensions.gone
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.globalextensions.startTimer
import com.armboldmind.grandmarket.shared.managers.FirebaseAuthenticationManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.shared.validations.CodeValidator
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VerifyLoginInfoFragment : BaseFragment<FragmentVerifyLoginInfoBinding>() {
    private val mUserViewModel: UserViewModel by lazy { createViewModel(UserViewModel::class.java, this) }
    private val mArgs: VerifyLoginInfoFragmentArgs by navArgs()
    private val mFirebaseManager by lazy { FirebaseAuthenticationManager.getInstance() }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentVerifyLoginInfoBinding
        get() = FragmentVerifyLoginInfoBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentVerifyLoginInfoBinding, keysFromDb: Keys) {
        binding.apply {
            keys = keysFromDb
            initDescription(keysFromDb)
            startTimer(keysFromDb)
            val requestModel = SendCodeRequestModel(email = mArgs.email, phoneNumber = mArgs.phone)
            resend.setOnClickListener {
                verificationCode.text?.clear()
                mArgs.phone?.let {
                    mFirebaseManager.resendCode(::handleViewState)
                    setLoading(true)
                }
                mArgs.email?.let { mUserViewModel.sendIntent(ActionIntent.SendCodeForEmailActionIntent(requestModel)) }

            }
            verify.setText(keysFromDb.verify)
            verify.setOnClickListener {
                hideSoftInput()
                CodeValidator().validate(keysFromDb, verificationCode, { showSnackBar(it) }) { code ->
                    mArgs.email?.let { mUserViewModel.sendIntent(ActionIntent.ChangeUserEmailActionIntent(requestModel.apply { this.code = code })) }
                    mArgs.phone?.let {
                        mFirebaseManager.verifyCode(requestModel.apply { this.code = code }, ::handleViewState)
                        setLoading(true)
                    }

                }
            }
        }
    }

    private fun initDescription(keysFromDb: Keys) {
        mArgs.phone?.let {
            mBinding.description.text = keysFromDb.sent_code_to_phone.replace("#PHONE#", it)
        }
        mArgs.email?.let {
            mBinding.description.text = keysFromDb.sent_code_to_email.replace("#EMAIL#", it)
        }
    }

    private fun startTimer(keysFromDb: Keys) {
        mBinding.timer.startTimer(lifecycleScope, keysFromDb) {
            mBinding.resend.show()
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mBinding.verify.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.SuccessState -> updateUserData()
            is ViewState.FirebaseVerifyCodeSuccessState -> lifecycleScope.launch { mUserViewModel.sendIntent(ActionIntent.ChangeUserPhoneNumberActionIntent(viewState.sendCodeRequestModel)) }
            is ViewState.FirebaseCodeSentSuccessState -> lifecycleScope.launch {
                startTimer(mKeys)
                mBinding.resend.gone()
                setLoading(false)
            }
            is ViewState.CodeSentSuccessState -> lifecycleScope.launch {
                startTimer(mKeys)
                mBinding.resend.gone()
                setLoading(false)
            }
            is ViewState.ErrorState -> showError(message = viewState.exception)
            else -> setLoading(false)
        }
    }

    private fun updateUserData() = lifecycleScope.launch {
        val user: User = preferencesManager().findByKey(BundleKeysEnum.USER.key)
        mArgs.email?.let { user.email = it }
        mArgs.phone?.let { user.phoneNumber = it }
        preferencesManager().saveByKey(BundleKeysEnum.USER.key, user)
        delay(200)
        requireActivity().onBackPressed()
        requireActivity().onBackPressed()
    }
}