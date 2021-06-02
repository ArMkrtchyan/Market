package com.armboldmind.grandmarket.shared.managers

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.armboldmind.grandmarket.data.models.requestmodels.SendCodeRequestModel
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class FirebaseAuthenticationManager private constructor() {

    companion object {
        private const val TAG = "FirebaseAuthentication"
        private lateinit var instance: FirebaseAuthenticationManager
        fun getInstance(): FirebaseAuthenticationManager {
            return if (::instance.isInitialized) instance else {
                instance = FirebaseAuthenticationManager()
                instance
            }
        }
    }

    private var viewState: ((viewState: ViewState) -> Unit)? = null
    private lateinit var requestModel: SendCodeRequestModel
    private var storedVerificationId = ""
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var options: PhoneAuthOptions
    private lateinit var mActivity: AppCompatActivity
    private val auth = FirebaseAuth.getInstance()
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)
            keysLiveData().observeForever { keys ->
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        if (e.message?.contains("The format of the phone number provided is incorrect") == true) viewState?.invoke(ViewState.ErrorState(BaseDataSource.SuccessException(
                            keys.incorrect_phone)))
                        else viewState?.invoke(ViewState.ErrorState(BaseDataSource.SuccessException(keys.wrong_code_message)))
                    }

                    is FirebaseTooManyRequestsException -> {
                        viewState?.invoke(ViewState.ErrorState(BaseDataSource.SuccessException(keys.to_many_request_message)))
                    }
                }
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")
            storedVerificationId = verificationId
            resendToken = token
            viewState?.invoke(ViewState.FirebaseCodeSentSuccessState(requestModel))
        }
    }

    fun startAuthentication(activity: AppCompatActivity, requestModel: SendCodeRequestModel, viewState: (viewState: ViewState) -> Unit) {
        this.viewState = viewState
        this.requestModel = requestModel
        mActivity = activity
        options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(requestModel.phoneNumber ?: "")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        viewState.invoke(ViewState.LoadingState)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(mActivity) { task ->
                keysLiveData().observeForever { keys ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        Log.d(TAG, "${user?.uid}")
                        user?.uid?.let { viewState?.invoke(ViewState.FirebaseVerifyCodeSuccessState(requestModel.apply { uid = it })) } ?: run {
                            viewState?.invoke(ViewState.ErrorState(BaseDataSource.SuccessException(keys.default_error_message)))
                        }
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            viewState?.invoke(ViewState.ErrorState(BaseDataSource.SuccessException(keys.wrong_code_message)))
                        }
                    }
                }
            }
    }

    fun verifyCode(requestModel: SendCodeRequestModel, viewState: (viewState: ViewState) -> Unit) {
        this.viewState = viewState
        this.requestModel = requestModel
        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(storedVerificationId, requestModel.code ?: ""))
    }

    fun resendCode(viewState: (viewState: ViewState) -> Unit) {
        this.viewState = viewState
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(requestModel.phoneNumber ?: "")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(mActivity)
            .setCallbacks(callbacks)
        resendToken?.let { optionsBuilder.setForceResendingToken(it) }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

}

