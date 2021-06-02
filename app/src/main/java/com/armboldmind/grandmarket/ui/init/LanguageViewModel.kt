package com.armboldmind.grandmarket.ui.init

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.ILanguageRepository
import com.armboldmind.grandmarket.data.IUserRepository
import com.armboldmind.grandmarket.data.database.repositories.LanguagesRepositoryLocale
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.UserRoleEnum
import com.armboldmind.grandmarket.shared.globalextensions.appComponent
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LanguageViewModel : BaseViewModel() {
    @Inject
    lateinit var mILanguageRepository: ILanguageRepository

    @Inject
    lateinit var mILanguageRepositoryLocal: LanguagesRepositoryLocale

    @Inject
    lateinit var mUserRepositoryRemote: IUserRepository

    init {
        appComponent().languagesComponent.build()
            .inject(this)
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.d("ExceptionTag", throwable.localizedMessage ?: "")
        }) {
            when (actionIntent) {
                is ActionIntent.ChangeLanguage -> changeLanguage(actionIntent.language)
                is ActionIntent.GetLanguages -> getLanguages()
                is ActionIntent.GetAllKeysAdnSignUpAsGuest -> getAllKeysAndSignInAsGuest()
                is ActionIntent.GetAllKeysFromNetwork -> getAllKeysFromNetwork()
                is ActionIntent.GetAllKeysFromDB -> getKeysFromDB()
                else -> Unit
            }
        }
    }

    private suspend fun getLanguages() {
        mILanguageRepository.getLanguages()
            .onStart {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
            }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                setState.invoke(ViewState.FetchLanguages(it))
            }
    }

    private fun saveLanguage(language: Language) {
        preferencesManager().saveByKey(BundleKeysEnum.APP_LANGUAGE.key, language)
    }

    private suspend fun changeLanguage(language: Language) {
        mILanguageRepository.changeLanguage(language)
            .onStart {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
            }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {
                if (it) {
                    saveLanguage(language)
                    mILanguageRepository.getAllKeys()
                        .collect { keys ->
                            mILanguageRepositoryLocal.insertKeys(keys)
                            keysLiveData().postValue(keys)
                            setState.invoke(ViewState.ChangeLanguageState(language))
                        }
                }
            }
    }

    private suspend fun getAllKeysAndSignInAsGuest() {
        val responseKeys = mILanguageRepository.getAllKeys()
        val responseSignUpGuest = mUserRepositoryRemote.signInAsGuest()
        responseKeys.zip(responseSignUpGuest) { keys, guest ->
            Log.i("KeysTag",keys.toString())
            mILanguageRepositoryLocal.insertKeys(keys)
            keysLiveData().postValue(keys)
            preferencesManager().saveByKey(BundleKeysEnum.ACCESS_TOKEN.key, guest.token)
            preferencesManager().saveByKey(BundleKeysEnum.USER_ROLE.key, UserRoleEnum.GUEST)
            preferencesManager().remove(BundleKeysEnum.USER.key)
            withContext(Dispatchers.Main) {
                setState.invoke(ViewState.SuccessState())
            }
        }
            .onStart {
                withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) }
            }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect {

            }
    }

    private suspend fun getAllKeysFromNetwork() {
        mILanguageRepository.getAllKeys()
            .collect { keys ->
                mILanguageRepositoryLocal.insertKeys(keys)
                keysLiveData().postValue(keys)
            }
    }

    private suspend fun getKeysFromDB() {
        mILanguageRepositoryLocal.getKeys()
            .collect { keys ->
                keysLiveData().postValue(keys)
            }
    }
}