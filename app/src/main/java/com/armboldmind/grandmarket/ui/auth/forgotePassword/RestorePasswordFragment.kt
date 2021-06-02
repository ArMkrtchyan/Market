package com.armboldmind.grandmarket.ui.auth.forgotePassword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.signUpRequestModel
import com.armboldmind.grandmarket.databinding.FragmentRestorePasswordBinding
import com.armboldmind.grandmarket.shared.globalextensions.toTransitionGroup
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.auth.formValidators.CreatePasswordFormValidator
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel

class RestorePasswordFragment : BaseFragment<FragmentRestorePasswordBinding>() {
    private val mCreatePasswordFormValidator by lazy { CreatePasswordFormValidator() }
    private val mUserViewModel: UserViewModel by lazy { createViewModel(UserViewModel::class.java, this) }
    private val mArgs: RestorePasswordFragmentArgs by navArgs()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRestorePasswordBinding
        get() = FragmentRestorePasswordBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentRestorePasswordBinding, keysFromDb: Keys) {
        binding.apply {
            keys = keysFromDb
            signUp.setText(keysFromDb.save)
            signUp.setOnClickListener {
                hideSoftInput()
                mCreatePasswordFormValidator.isFormValid(keysFromDb, password, repeatPassword, { showSnackBar(it) }) { pass, _ ->
                    mUserViewModel.sendIntent(ActionIntent.ResetPasswordActionIntent(signUpRequestModel {
                        code = mArgs.sendCodeRequestModel.code
                        uid = mArgs.sendCodeRequestModel.uid
                        userName = mArgs.sendCodeRequestModel.userName
                        password = pass
                    }))
                }
            }
            initAnimation()
        }
    }


    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mBinding.signUp.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.SuccessState -> goToSignIn()
            is ViewState.ErrorState -> showError(message = viewState.exception)
            else -> {
            }
        }
    }

    private fun goToSignIn() {
        setLoading(false)
        val extrasSignIn = FragmentNavigatorExtras(mBinding.mainLayout.toTransitionGroup(),
                                                   mBinding.title.toTransitionGroup(),
                                                   mBinding.description.toTransitionGroup(),
                                                   mBinding.passwordLayout.toTransitionGroup(),
                                                   mBinding.repeatPasswordLayout.toTransitionGroup(),
                                                   mBinding.signUp.toTransitionGroup(),
                                                   mBinding.logo.toTransitionGroup())
        view?.findNavController()
            ?.navigate(RestorePasswordFragmentDirections.actionRestorePasswordFragmentToSignInFragment(), extrasSignIn)
    }
}