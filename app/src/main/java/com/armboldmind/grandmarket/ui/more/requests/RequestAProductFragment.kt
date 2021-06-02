package com.armboldmind.grandmarket.ui.more.requests

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.mappers.BrandsToRadioButtonMapper
import com.armboldmind.grandmarket.data.mappers.CategoriesToRadioButtonMapper
import com.armboldmind.grandmarket.data.models.domain.PhotoModel
import com.armboldmind.grandmarket.data.models.domain.RadioButtonModel
import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentRequestAProductBinding
import com.armboldmind.grandmarket.shared.customview.DialogFactory
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.MenuEnum
import com.armboldmind.grandmarket.shared.enums.PhotoUploadingStatusEnum
import com.armboldmind.grandmarket.shared.enums.UserRoleEnum
import com.armboldmind.grandmarket.shared.globalextensions.*
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.shared.utils.GridSpacingItemDecoration
import com.armboldmind.grandmarket.ui.MainActivity
import com.armboldmind.grandmarket.ui.more.personalInformation.dialogs.PhotoPickerDialog
import com.armboldmind.grandmarket.ui.more.requests.adapters.AdapterRequestPhoto
import com.armboldmind.grandmarket.ui.more.requests.dialogs.CategoriesBrandsBottomSheetDialog
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class RequestAProductFragment : BaseFragment<FragmentRequestAProductBinding>() {
    private val mRequestProductViewModel by lazy { createViewModel(RequestProductViewModel::class.java, this) }
    private val mAdapter by lazy { AdapterRequestPhoto(::onAddPhotoClick, ::onRemovePhoto) }
    private val sendRequestFormValidator by lazy { SendRequestFormValidator() }

    private val mCategories = arrayListOf<RadioButtonModel>()
    private val mBrands = arrayListOf<RadioButtonModel>()

    private var mSelectedCategory: Long = -1
    private var mSelectedBrand: Long = -1
    private var mSelectedPhotos = arrayListOf<PhotoModel>().apply { add(PhotoModel(0)) }
    private var mCurrentActionIntent: ActionIntent = ActionIntent.GetCategoriesWithBrands

    private var mCategoriesDialog: CategoriesBrandsBottomSheetDialog? = null
    private var mBrandsDialog: CategoriesBrandsBottomSheetDialog? = null
    private var mPhotoPicker: PhotoPickerDialog? = null

    private val galleryPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when {
            granted -> openGalleryContract.launch("image/*")
            !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> showLongToast(keysLiveData().value?.gallery_permission_denied ?: "")
            else -> Unit
        }
    }
    private val cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when {
            granted -> openCameraContract.launch()
            !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showLongToast(keysLiveData().value?.gallery_permission_denied ?: "")
            else -> Unit
        }
    }

    private val openGalleryContract = registerForActivityResult(ActivityResultContracts.GetContent(), this::setPhoto)
    private val openCameraContract = registerForActivityResult(ActivityResultContracts.TakePicturePreview(), this::setPhoto)

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRequestAProductBinding
        get() = FragmentRequestAProductBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentRequestAProductBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            mRequestProductViewModel.sendIntent(mCurrentActionIntent)
            photos.adapter = mAdapter.apply { submitList(mSelectedPhotos.toMutableList()) }
            photos.addItemDecoration(GridSpacingItemDecoration(3,
                                                               requireContext().getDimensions(R.dimen.view_size_2)
                                                                   .toInt()))
            preferencesManager().findByKey<User?>(BundleKeysEnum.USER.key)
                ?.let { user = it }
            send.setText(keysFromDb.send_request)
            send.setOnClickListener {
                sendRequestFormValidator.isFormValid(keysFromDb,
                                                     fullName,
                                                     contact,
                                                     mSelectedCategory,
                                                     category,
                                                     mSelectedBrand,
                                                     brand,
                                                     productName,
                                                     description,
                                                     ::showSnackBar) { fullName, contact, selectedCategory, selectedBrand, productName, description ->
                    mCurrentActionIntent =
                        ActionIntent.CreateRequestForProduct(fullName, contact, productName, description, selectedCategory, selectedBrand, createFormDataForImages())
                    mRequestProductViewModel.sendIntent(mCurrentActionIntent)
                }
            }
            category.setOnClickListener {
                when {
                    mCategories.isNotEmpty() && (mCategoriesDialog == null || mCategoriesDialog?.isVisible == false) -> {
                        mCategoriesDialog = CategoriesBrandsBottomSheetDialog(keysFromDb.select_category, mCategories) {
                            category.setText(it.title)
                            mSelectedCategory = it.id
                            mCategories.map { category -> category.isSelected = category.id == it.id }
                        }
                        mCategoriesDialog?.show(childFragmentManager, CategoriesBrandsBottomSheetDialog::class.java.name)
                        mCategoriesDialog?.dialog?.setOnDismissListener { mCategoriesDialog = null }
                    }
                }
            }
            brand.setOnClickListener {
                when {
                    mBrands.isNotEmpty() && (mBrandsDialog == null || mBrandsDialog?.isVisible == false) -> {
                        mBrandsDialog = CategoriesBrandsBottomSheetDialog(keysFromDb.select_brand, mBrands) {
                            brand.setText(it.title)
                            mSelectedBrand = it.id
                            mBrands.map { brand -> brand.isSelected = brand.id == it.id }
                        }
                        mBrandsDialog?.show(childFragmentManager, CategoriesBrandsBottomSheetDialog::class.java.name)
                        mBrandsDialog?.dialog?.setOnDismissListener { mBrandsDialog = null }
                    }
                }
            }
        }
    }

    private fun createFormDataForImages(): List<MultipartBody.Part?>? {
        val list = mSelectedPhotos.filter { it.id != 0 }
        return if (list.isNotEmpty()) list.map { photoModel ->
            photoModel.status.value = PhotoUploadingStatusEnum.UPLOADING
            photoModel.bitmap?.let {
                it.createFormData(requireContext(), "files") { percent -> if (percent == 100) photoModel.status.postValue(PhotoUploadingStatusEnum.UPLOADED) }
            }
            photoModel.uri?.let { it.createFormData(requireContext(), "files") { percent -> if (percent == 100) photoModel.status.postValue(PhotoUploadingStatusEnum.UPLOADED) } }
        } else null
    }

    private fun onAddPhotoClick() {
        hideSoftInput()
        if (mBinding.isLoading == null || mBinding.isLoading == false) {
            if (mPhotoPicker == null || mPhotoPicker?.isVisible == false) {
                mPhotoPicker = PhotoPickerDialog("", ::handleViewState)
                mPhotoPicker?.show(childFragmentManager, PhotoPickerDialog::class.java.name)
                mPhotoPicker?.dialog?.setOnDismissListener { mPhotoPicker = null }
            }
        }
    }

    private fun onRemovePhoto(position: Int) {
        if (mBinding.isLoading == null || mBinding.isLoading == false) mSelectedPhotos.apply {
            removeAt(position)
            if (get(0).id != 0) add(0, PhotoModel(0))
            mAdapter.submitList(toMutableList())
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        if (mCurrentActionIntent is ActionIntent.GetCategoriesWithBrands) mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
        else mBinding.send.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> lifecycleScope.launch {
                if (mBinding.mainContent.isVisible) showError(message = viewState.exception) { mRequestProductViewModel.sendIntent(mCurrentActionIntent) }
                else {
                    mBinding.loadingView.setViewState(ViewState.ErrorState(viewState.exception))
                    mBinding.loadingView.setOnButtonClick(mKeys.retry) { mRequestProductViewModel.sendIntent(mCurrentActionIntent) }
                }
            }
            is ViewState.FetchCategoriesWithBrands -> {
                mCategories.addAll(viewState.categories.map { CategoriesToRadioButtonMapper().map(it) })
                if (mCategories.isNotEmpty()) mCategories[0].isSelected = true
                mBrands.addAll(viewState.brands.map { BrandsToRadioButtonMapper().map(it) })
                if (mBrands.isNotEmpty()) mBrands[0].isSelected = true
                setLoading(false)
                mBinding.mainContent.show()
            }
            is ViewState.OpenCamera -> cameraPermission.launch(Manifest.permission.CAMERA)
            is ViewState.OpenGallery -> galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            is ViewState.RequestForProductCreated -> showCreatedDialog()
            else -> setLoading(false)
        }
    }

    private fun showCreatedDialog() {
        setLoading(false)
        DialogFactory.Builder(requireContext())
            .imageRes(R.drawable.ic_requests_empty)
            .title(mKeys.thank_you)
            .description(mKeys.request_created_description)
            .setCancelable(false)
            .positiveButtonText(mKeys.close)
            .positiveButtonClick {
                if (preferencesManager().findByKey<UserRoleEnum>(BundleKeysEnum.USER_ROLE.key) == UserRoleEnum.GUEST) {
                    (activity as MainActivity).moreNavController()
                        .popBackStack(R.id.moreFragment, false)
                    (activity as MainActivity).navigateToTab(MenuEnum.HOME)
                } else {
                    if (view?.findNavController()?.previousBackStackEntry?.destination?.id == R.id.requestProductInfoFragment) {
                        (activity as MainActivity).moreNavController()
                            .popBackStack(R.id.moreFragment, false)
                        (activity as MainActivity).moreNavController()
                            .navigate(R.id.requestsFragment)
                    } else if (view?.findNavController()?.previousBackStackEntry?.destination?.id == R.id.requestsFragment) {
                        setFragmentResult(BundleKeysEnum.ADD_REQUEST_RESULT_KEY.key, bundleOf(BundleKeysEnum.IS_REQUEST_ADDED.key to true))
                        requireActivity().onBackPressed()
                    }
                }
            }
            .build()
    }

    private fun setPhoto(bitmap: Bitmap?) {
        bitmap?.let {
            mSelectedPhotos.map { model ->
                if (model.bitmap == it) {
                    showSnackBar(mKeys.photo_already_selected)
                    return@let
                }
            }
            if (mSelectedPhotos.size == 6) mSelectedPhotos.removeAt(0)
            mSelectedPhotos.add(PhotoModel(mSelectedPhotos.size, bitmap = it))
            mAdapter.submitList(mSelectedPhotos.toMutableList())
        }
    }

    private fun setPhoto(uri: Uri?) {
        uri?.let {
            mSelectedPhotos.map { model ->
                if (model.uri == it) {
                    showSnackBar(mKeys.photo_already_selected)
                    return@let
                }
            }
            if (mSelectedPhotos.size == 6) mSelectedPhotos.removeAt(0)
            mSelectedPhotos.add(PhotoModel(mSelectedPhotos.size, uri = it))
            mAdapter.submitList(mSelectedPhotos.toMutableList())
        }
    }
}