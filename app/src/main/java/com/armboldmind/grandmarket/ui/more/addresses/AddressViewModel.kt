package com.armboldmind.grandmarket.ui.more.addresses

import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.IAddressRepository
import com.armboldmind.grandmarket.data.models.domain.Address
import com.armboldmind.grandmarket.shared.globalextensions.appComponent
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressViewModel : BaseViewModel() {
    @Inject
    lateinit var mAddressRepository: IAddressRepository

    init {
        appComponent().addressComponent.build()
            .inject(this)
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch {
            when (actionIntent) {
                is ActionIntent.GetAddresses -> getAllAddresses()
                is ActionIntent.SetAddressAsDefault -> updateAddress(actionIntent.address, true)
                is ActionIntent.EditAddress -> withContext(Dispatchers.Main) {
                    setState.invoke(ViewState.EditAddress(actionIntent.address))
                }
                is ActionIntent.AddAddress -> addAddress(actionIntent.address)
                is ActionIntent.UpdateAddress -> updateAddress(actionIntent.address)
                is ActionIntent.DeleteAddress -> deleteAddress(actionIntent.address)
                is ActionIntent.ShowDeleteDialog -> setState.invoke(ViewState.ShowDeleteDialog(actionIntent.address))
                is ActionIntent.ShowDefaultDialog -> setState.invoke(ViewState.ShowDefaultDialog(actionIntent.address))
                else -> Unit
            }
        }
    }

    private suspend fun getAllAddresses(showLoading: Boolean = true) {
        mAddressRepository.getAllAddresses()
            .onStart { if (showLoading) withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect { withContext(Dispatchers.Main) { setState.invoke(if (it.isNotEmpty()) ViewState.GetAllAddresses(it.toMutableList()) else ViewState.EmptyState) } }
    }

    private suspend fun deleteAddress(address: Address) {
        mAddressRepository.deleteAddress(address)
            .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect { getAllAddresses(false) }
    }

    private suspend fun updateAddress(address: Address, isForDefault: Boolean = false) {
        mAddressRepository.updateAddress(address)
            .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect { withContext(Dispatchers.Main) { if (isForDefault) getAllAddresses(false) else setState.invoke(ViewState.SuccessState()) } }
    }

    private suspend fun addAddress(address: Address) {
        mAddressRepository.addAddress(address)
            .onStart { withContext(Dispatchers.Main) { setState.invoke(ViewState.LoadingState) } }
            .catch { throwable ->
                withContext(Dispatchers.Main) { setState.invoke(ViewState.ErrorState(throwable)) }
            }
            .collect { withContext(Dispatchers.Main) { setState.invoke(ViewState.SuccessState()) } }
    }
}