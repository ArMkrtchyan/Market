package com.armboldmind.grandmarket.ui.more.personalInformation

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import coil.load
import coil.transform.BlurTransformation
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.ChangePasswordRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.UpdateUserRequestModel
import com.armboldmind.grandmarket.data.models.requestmodels.updateUserRequestModel
import com.armboldmind.grandmarket.databinding.FragmentPersonalInformationBinding
import com.armboldmind.grandmarket.shared.customview.DialogFactory
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import com.armboldmind.grandmarket.shared.formatters.Formatter
import com.armboldmind.grandmarket.shared.formatters.IFormatter
import com.armboldmind.grandmarket.shared.globalextensions.*
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.shared.utils.AnimationUtil
import com.armboldmind.grandmarket.ui.MainActivity
import com.armboldmind.grandmarket.ui.auth.dialogs.DatePickerDialog
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import com.armboldmind.grandmarket.ui.more.addresses.dialogs.EditAddressDialog
import com.armboldmind.grandmarket.ui.more.personalInformation.dialogs.PhotoPickerDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class PersonalInformationFragment : BaseFragment<FragmentPersonalInformationBinding>() {

    private val mUserViewModel: UserViewModel by lazy { (activity as MainActivity).createViewModel(UserViewModel::class.java, this) }
    private var mPhotoPicker: PhotoPickerDialog? = null
    private val mFormatter: IFormatter by lazy { Formatter() }
    private val mPersonalInformationFormValidator by lazy { PersonalInformationFormValidator() }

    private var mBitmap: Bitmap? = null
    private var mUri: Uri? = null

    private var changePassword = false

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

    private val openGalleryContract = registerForActivityResult(ActivityResultContracts.GetContent(), this::setProfilePhoto)
    private val openCameraContract = registerForActivityResult(ActivityResultContracts.TakePicturePreview(), this::setProfilePhoto)

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPersonalInformationBinding
        get() = FragmentPersonalInformationBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentPersonalInformationBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            initUserInfo()
            AnimationUtil.alphaFrom0To1(mainContent)
            photoPicker.setOnClickListener {
                hideSoftInput()
                if (mPhotoPicker == null || mPhotoPicker?.isVisible == false) {
                    mPhotoPicker = PhotoPickerDialog(preferencesManager().findByKey<User>(BundleKeysEnum.USER.key).imageUrl, this@PersonalInformationFragment::handleViewState)
                    mPhotoPicker?.show(childFragmentManager, PhotoPickerDialog::class.java.name)
                    mPhotoPicker?.dialog?.setOnDismissListener { mPhotoPicker = null }
                }
            }
            dateOfBirth.setOnClickListener {
                hideSoftInput()
                val datePickerDialog = DatePickerDialog(dateOfBirth.text.toString()) {
                    dateOfBirth.setText((mFormatter.formatToPattern(it, DatePatternsEnum.DAY_MONTH_YEAR)))
                }
                datePickerDialog.show(childFragmentManager, EditAddressDialog::class.java.name)
            }
            saveInfo.setText(keysFromDb.save)
            saveInfo.setOnClickListener {
                mPersonalInformationFormValidator.validateInfo(keysFromDb, fullName, dateOfBirth, ::showSnackBar) { fullName, dateOfBirth ->
                    changePassword = false
                    hideSoftInput()
                    mUserViewModel.sendIntent(ActionIntent.UpdateActionIntent(updateUserRequestModel {
                        physicalUser = UpdateUserRequestModel.PhysicalUser(fullName, dateOfBirth)
                    }))
                }
            }
            savePassword.setText(keysFromDb.save)
            savePassword.setOnClickListener {
                mPersonalInformationFormValidator.validatePassword(keysFromDb, currentPassword, newPassword, confirmNewPassword, ::showSnackBar) { currentPassword, password ->
                    changePassword = true
                    hideSoftInput()
                    mUserViewModel.sendIntent(ActionIntent.ChangePasswordActionIntent(ChangePasswordRequestModel(currentPassword, password)))
                }
            }
            addChangePhone.paintFlags = addChangePhone.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            addChangeEmail.paintFlags = addChangeEmail.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            addChangePhone.setOnClickListener {
                view?.findNavController()
                        ?.navigate(PersonalInformationFragmentDirections.actionPersonalInformationFragmentToChangeLoginInfoFragment(false))
            }
            addChangeEmail.setOnClickListener {
                view?.findNavController()
                        ?.navigate(PersonalInformationFragmentDirections.actionPersonalInformationFragmentToChangeLoginInfoFragment(true))
            }
            KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
                if (isOpen) {
                    mainContent.setPadding(0, 0, 0, 20.0.dpToPx())
                } else mainContent.setPadding(0, 0, 0, 100.0.dpToPx())
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        if (changePassword) mBinding.savePassword.setIsLoading(isLoading)
        else mBinding.saveInfo.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> {
                showError(message = viewState.exception) { }
                if (mBitmap != null || mUri != null) {
                    mBinding.progress.hide()
                    mBinding.photoPicker.isEnabled = true
                    mBinding.userPhoto.load(mBinding.user?.imageUrl ?: "")
                }
            }
            is ViewState.SuccessState -> {
                viewState.message?.let { showToast(it) }
                setLoading(false)
                mBinding.apply {
                    progress.hide()
                    photoPicker.isEnabled = true
                    clearFocus()
                    mBitmap?.let { userPhoto.setImageBitmap(it) } ?: run { mUri?.let { userPhoto.setImageURI(it) } }
                    mBitmap = null
                    mUri = null
                    (activity as MainActivity).checkBottomNavigationProfileIcon()
                }
            }
            is ViewState.OpenCamera -> cameraPermission.launch(Manifest.permission.CAMERA)
            is ViewState.OpenGallery -> galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            is ViewState.DeletePhoto -> deleteProfilePhoto()
            else -> Unit
        }
    }

    private fun deleteProfilePhoto() {
        DialogFactory.Builder(requireContext())
                .title(mKeys.delete_photo_title)
                .description(mKeys.delete_photo_description)
                .positiveButtonText(mKeys.delete)
                .negativeButtonText(mKeys.cancel)
                .positiveButtonClick {
                    mUserViewModel.sendIntent(ActionIntent.DeleteImageActionIntent)
                    mBinding.userPhoto.setImageResource(R.drawable.ic_profile)
                }
                .build()
    }

    private fun setProfilePhoto(bitmap: Bitmap?) {
        lifecycleScope.launch {
            bitmap?.let {
                val user: User = preferencesManager().findByKey(BundleKeysEnum.USER.key)
                mBitmap = it
                mBinding.photoPicker.isEnabled = false
                delay(100)
                mBinding.userPhoto.load(it) {
                    transformations(BlurTransformation(requireActivity(), 9f, 1f))
                }
                mUserViewModel.sendIntent(ActionIntent.UploadImageActionIntent(it.createFormData(requireContext(), "file") { progress ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        mBinding.progress.show()
                        mBinding.progress.setProgressCompat(progress, true)
                    }
                }, user.imageUrl == ""))
            }
        }
    }

    private fun setProfilePhoto(uri: Uri?) {
        lifecycleScope.launch {
            uri?.let {
                val user: User = preferencesManager().findByKey(BundleKeysEnum.USER.key)
                mUri = it
                mBinding.photoPicker.isEnabled = false
                delay(100)
                mBinding.userPhoto.load(it) {
                    transformations(BlurTransformation(requireActivity(), 9f, 1f))
                }
                mUserViewModel.sendIntent(ActionIntent.UploadImageActionIntent(it.createFormData(requireContext(), "file") { progress ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        mBinding.progress.show()
                        mBinding.progress.setProgressCompat(progress, true)
                    }
                }, user.imageUrl == ""))
            }
        }
    }

    private fun clearFocus() {
        mBinding.currentPassword.text?.clear()
        mBinding.newPassword.text?.clear()
        mBinding.confirmNewPassword.text?.clear()
        mBinding.mainContent.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        initUserInfo()
    }

    private fun initUserInfo() {
        (activity as MainActivity).checkBottomNavigationProfileIcon()
        val user: User = preferencesManager().findByKey<User>(BundleKeysEnum.USER.key)
                .apply {
                    if (phoneNumber.length == 12) {
                        val countryCode = phoneNumber.substring(0, 4)
                        val code = phoneNumber.substring(4, 6)
                        val number1 = phoneNumber.substring(6, 8)
                        val number2 = phoneNumber.substring(8, 10)
                        val number3 = phoneNumber.substring(10, 12)
                        phoneNumber = "$countryCode $code $number1 $number2 $number3"
                    }
                }
        mBinding.apply {
            this.user = user
            email.text = if (user.email.isNotEmpty()) user.email else getString(R.string.email_address)
            phone.text = if (user.phoneNumber.isNotEmpty()) user.phoneNumber else getString(R.string.phone_number)
            addChangeEmail.text = if (user.email.isNotEmpty()) mKeys.change else mKeys.add
            addChangePhone.text = if (user.phoneNumber.isNotEmpty()) mKeys.change else mKeys.add
            mBinding.root.requestLayout()
            userPhoto.load(user.imageUrl) {
                error(R.drawable.ic_profile)
            }
        }
    }
}