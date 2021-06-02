package com.armboldmind.grandmarket.ui.more.support

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.contactUsRequestModel
import com.armboldmind.grandmarket.databinding.FragmentContactUsBinding
import com.armboldmind.grandmarket.shared.customview.DialogFactory
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.MessageSubjectEnum
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.more.informative.InfoViewModel
import kotlinx.coroutines.launch

class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {

    private val mInfoViewModel by lazy { createViewModel(InfoViewModel::class.java, this) }
    private val mContactUsFormValidator by lazy { ContactUsFormValidator() }
    private var mSelectedOption: Int = -1
    private var topicPicker: TopicBottomSheetDialog? = null

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactUsBinding
        get() = FragmentContactUsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(binding: FragmentContactUsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            preferencesManager().findByKey<User?>(BundleKeysEnum.USER.key)
                    ?.let { user = it }
            topic.setOnClickListener {
                if (topicPicker == null || topicPicker?.isVisible == false) {
                    topicPicker = TopicBottomSheetDialog(mSelectedOption) { name, type ->
                        mSelectedOption = type
                        topic.setText(name)
                    }
                    topicPicker?.show(childFragmentManager, TopicBottomSheetDialog::class.java.name)
                    topicPicker?.dialog?.setOnDismissListener { topicPicker = null }
                }
            }
            send.setText(keysFromDb.send)
            send.setOnClickListener {
                mContactUsFormValidator.isFormValid(fullName, contact, message, topic, mSelectedOption) { fullName, phone, email, message ->
                    mInfoViewModel.sendIntent(ActionIntent.SendContactData(contactUsRequestModel {
                        this.fullName = fullName
                        this.message = message
                        this.phoneNumber = phone
                        this.email = email
                        this.messageSubjectEnumValue = mSelectedOption
                        this.subject = if (mSelectedOption == MessageSubjectEnum.OTHER.type) "Other" else null
                    }))
                }
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.isLoading = isLoading
        mBinding.send.setIsLoading(isLoading)
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> showError(message = viewState.exception) { }
            is ViewState.SuccessState -> lifecycleScope.launch {
                setLoading(false)
                mBinding.message.text?.clear()
                preferencesManager().findByKey<User?>(BundleKeysEnum.USER.key)
                        ?.let { mBinding.user = it }
                DialogFactory.Builder(requireContext())
                        .imageRes(R.drawable.ic_contact_us_dialog)
                        .title(mKeys.thank_you)
                        .description(mKeys.your_message_was_succesfuly_sent)
                        .positiveButtonText(mKeys.close)
                        .positiveButtonClick {}
                        .build()
            }
            else -> Unit
        }
    }
}