package com.armboldmind.grandmarket.ui.more.addresses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Address
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentAddressesBinding
import com.armboldmind.grandmarket.shared.customview.DialogFactory
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.EmptyStatesEnum
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.more.addresses.dialogs.EditAddressDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AddressesFragment : BaseFragment<FragmentAddressesBinding>() {

    private var mEditAddress: EditAddressDialog? = null
    private val mAddressesViewModel by lazy { createViewModel(AddressViewModel::class.java, this) }
    private val mAdapter by lazy { AdapterAddresses(this@AddressesFragment::showAddressEditDialog) }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddressesBinding
        get() = FragmentAddressesBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentAddressesBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            addresses.adapter = mAdapter
            add.setOnClickListener { navigateToAddOrEditAddress() }
            mAddressesViewModel.sendIntent(ActionIntent.GetAddresses)
            loadingView.setEmpty(EmptyStatesEnum.ADDRESSES.emptyModel)
            setFragmentResultListener(BundleKeysEnum.ADD_ADDRESS_RESULT_KEY.key) { key, bundle ->
                if (key == BundleKeysEnum.ADD_ADDRESS_RESULT_KEY.key && bundle.containsKey(BundleKeysEnum.IS_ADDRESS_ADDED.key) && bundle.getBoolean(BundleKeysEnum.IS_ADDRESS_ADDED.key)) {
                    mAddressesViewModel.sendIntent(ActionIntent.GetAddresses)
                }
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> mBinding.loadingView.apply {
                setViewState(ViewState.ErrorState(viewState.exception))
                setOnButtonClick(mKeys.retry) { mAddressesViewModel.sendIntent(ActionIntent.GetAddresses) }
            }
            is ViewState.GetAllAddresses -> lifecycleScope.launch { setLoading(false); mAdapter.submitList(viewState.addresses) }
            is ViewState.EmptyState -> lifecycleScope.launch {
                mAdapter.submitList(arrayListOf()); delay(100); mBinding.loadingView.setViewState(ViewState.EmptyState)
            }
            is ViewState.EditAddress -> navigateToAddOrEditAddress(viewState.address)
            is ViewState.ShowDeleteDialog -> showDeleteDialog(viewState.address)
            is ViewState.ShowDefaultDialog -> showDefaultDialog(viewState.address)
            is ViewState.SuccessState -> setLoading(false)
            else -> Unit
        }
    }

    private fun showAddressEditDialog(address: Address) {
        if (mEditAddress == null || mEditAddress?.isVisible == false) {
            mEditAddress = EditAddressDialog(address, mAddressesViewModel)
            mEditAddress?.show(childFragmentManager, EditAddressDialog::class.java.name)
            mEditAddress?.dialog?.setOnDismissListener { mEditAddress = null }
        }
    }

    private fun showDeleteDialog(address: Address) {
        DialogFactory.Builder(requireContext())
                .title(mKeys.delete_address_title)
                .description(mKeys.delete_address_description)
                .positiveButtonText(mKeys.delete)
                .negativeButtonText(mKeys.cancel)
                .positiveButtonClick { mAddressesViewModel.sendIntent(ActionIntent.DeleteAddress(address)) }
            .build()
    }

    private fun showDefaultDialog(address: Address) {
        DialogFactory.Builder(requireContext())
                .title(mKeys.default_address_title)
                .description(mKeys.default_address_description)
                .positiveButtonText(mKeys.make_default)
                .negativeButtonText(mKeys.cancel)
                .positiveButtonClick { mAddressesViewModel.sendIntent(ActionIntent.SetAddressAsDefault(address.copy(isDefaultForDelivery = true))) }
            .build()
    }

    private fun navigateToAddOrEditAddress(address: Address? = null) {
        view?.findNavController()
            ?.navigate(AddressesFragmentDirections.actionAddressesFragmentToAddAddressFragment(address, mAdapter.itemCount))
    }
}