package com.armboldmind.grandmarket.ui.more.addresses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Address
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentAddAddressBinding
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.globalextensions.dpToPx
import com.armboldmind.grandmarket.shared.globalextensions.gone
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


class AddAddressFragment : BaseFragment<FragmentAddAddressBinding>(), SuggestSession.SuggestListener {

    private val mArgs: AddAddressFragmentArgs by navArgs()
    private val mAddAddressFormValidator by lazy { AddAddressFormValidator() }
    private val mAddressesViewModel by lazy { createViewModel(AddressViewModel::class.java, this) }
    private val mAdapter = AdapterAutoComplete(this::onAddressItemClick)

    private lateinit var mSearchManager: SearchManager
    private lateinit var mSuggestSession: SuggestSession
    private val mSouthWest = Point(38.845753, 43.532827)
    private val mNorthEast = Point(41.342327, 46.460719)
    private var mAddressPoint = Point(41.342327, 46.460719)
    private var mAddressName = ""
    private var mRegion = ""
    private val mBoundingBox = BoundingBox(Point(mSouthWest.latitude, mSouthWest.longitude), Point(mNorthEast.latitude, mNorthEast.longitude))
    private val mSuggestOptions = SuggestOptions().setSuggestTypes(SuggestType.GEO.value or SuggestType.BIZ.value or SuggestType.TRANSIT.value)
    private val mSearchOption = SearchOptions()
    private val mSearchListener = object : Session.SearchListener {

        override fun onSearchError(error: Error) {
            onError(error)
        }

        override fun onSearchResponse(response: Response) {
            response.collection.children.first()?.obj?.geometry?.first()?.point?.let {
                mAddressPoint = it
                mBinding.address.setText(mAddressName)
                mBinding.addressAutoComplate.clearFocus()
                mBinding.clear.show()
                mBinding.clear.setOnClickListener {
                    mBinding.address.text?.clear()
                    mBinding.clear.gone()
                    mAddressName = ""
                    mRegion = ""
                }
            }
        }
    }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddAddressBinding
        get() = FragmentAddAddressBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAutoComplete()
    }

    override fun initView(
        binding: FragmentAddAddressBinding,
        keysFromDb: Keys,
    ) {
        binding.apply {
            addressModel = mArgs.address
            keys = keysFromDb
            listSize = mArgs.listSize
            mArgs.address?.latitude?.let { lang ->
                mArgs.address?.longitude?.let { long ->
                    mAddressPoint = Point(lang, long)
                }
            }
            mArgs.address?.region?.let { region ->
                mRegion = region
            }
            addressesList.adapter = mAdapter
            address.setOnClickListener {
                addressAutoComplate.requestFocus()
                addressAutoComplate.text = address.text
                addressAutoComplate.setSelection(addressAutoComplate.text.toString().length)
            }
            close.setOnClickListener {
                mBinding.address.text?.clear()
                mBinding.addressAutoComplate.text?.clear()
                mBinding.clear.gone()
                mAddressName = ""
                mRegion = ""
                mAdapter.submitList(arrayListOf())
            }
            addressAutoComplate.setOnFocusChangeListener { _, hasFocus ->
                autocomplateLayout.isVisible = hasFocus
                mAdapter.submitList(arrayListOf())
                addressAutoComplate.text?.clear()
                if (hasFocus) showKeyBoard(addressAutoComplate)
            }
            addressAutoComplate.doAfterTextChanged { text ->
                requestSuggest(text.toString())
                close.isVisible = !text.isNullOrEmpty()
            }
            save.setText(keysFromDb.save)
            save.setOnClickListener {
                mAddAddressFormValidator.validate(address) { address ->
                    hideSoftInput()
                    mAddressesViewModel.sendIntent(if (mArgs.address == null) ActionIntent.AddAddress(createAddressModel(address))
                                                   else ActionIntent.UpdateAddress(createAddressModel(address)))
                }
            }
            KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
                if (isOpen) {
                    mainContent.setPadding(0, 0, 0, 40.0.dpToPx())
                } else mainContent.setPadding(0, 0, 0, 120.0.dpToPx())
            }
        }
    }

    private fun createAddressModel(address: String): Address {
        return with(mBinding) {
            Address(id = mArgs.address?.id ?: 0,
                    title = title.text.toString()
                        .trimStart()
                        .trimEnd(),
                    addressName = address,
                    latitude = mAddressPoint.latitude,
                    longitude = mAddressPoint.longitude,
                    region = mRegion,
                    isDefaultForDelivery = isDefault.isChecked,
                    additionalInformation = if (!comment.text?.trimStart()
                            ?.trimEnd()
                            .isNullOrEmpty()
                    ) comment.text.toString()
                        .trimStart()
                        .trimEnd() else null,
                    entrance = if (!entrance.text?.trimStart()
                            ?.trimEnd()
                            .isNullOrEmpty()
                    ) entrance.text.toString()
                        .toInt() else null,
                    floor = if (!floor.text?.trimStart()
                            ?.trimEnd()
                            .isNullOrEmpty()
                    ) floor.text.toString()
                        .toInt() else null,
                    apartment = if (!apartment.text?.trimStart()
                            ?.trimEnd()
                            .isNullOrEmpty()
                    ) apartment.text.toString() else null,
                    isDeliveryAddress = true)
        }
    }

    private fun initAutoComplete() {
        MapKitFactory.initialize(requireContext())
        SearchFactory.initialize(requireContext())
        mSearchManager = SearchFactory.getInstance()
            .createSearchManager(SearchManagerType.ONLINE)
        mSuggestSession = mSearchManager.createSuggestSession()
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mBinding.save.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> showError(message = viewState.exception) { }
            is ViewState.SuccessState -> {
                setFragmentResult(BundleKeysEnum.ADD_ADDRESS_RESULT_KEY.key, bundleOf(BundleKeysEnum.IS_ADDRESS_ADDED.key to true))
                requireActivity().onBackPressed()
                setLoading(false)
            }
            else -> Unit
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance()
            .onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance()
            .onStop()
        super.onStop()
    }

    override fun onResponse(itemList: MutableList<SuggestItem>) {
        val results = itemList.filter { it.tags.contains("house") }
            .toMutableList()
        mBinding.line.isVisible = results.isNotEmpty()
        mBinding.addressesList.isVisible = results.isNotEmpty()
        mAdapter.submitList(if (results.size > 5) results.subList(0, 5) else results)
    }

    override fun onError(error: Error) {
        val errorMessage =
            if (error is NetworkError) getString(com.armboldmind.grandmarket.R.string.default_network_error_description) else getString(com.armboldmind.grandmarket.R.string.default_error_message)
        hideSoftInput()
        showSnackBar(errorMessage)
    }

    private fun onAddressItemClick(suggestItem: SuggestItem) {
        mAddressName = suggestItem.title.text
        mRegion = suggestItem.subtitle?.text ?: ""
        suggestItem.uri?.let { mSearchManager.resolveURI(it, mSearchOption, mSearchListener) } ?: let {
            mBinding.address.setText(suggestItem.title.text)
            mBinding.addressAutoComplate.clearFocus()
            mSearchManager.submit(suggestItem.searchText, Geometry.fromBoundingBox(mBoundingBox), mSearchOption, mSearchListener)
        }
    }

    private fun requestSuggest(query: String) {
        mSuggestSession.suggest(query, mBoundingBox, mSuggestOptions, this)
    }

}