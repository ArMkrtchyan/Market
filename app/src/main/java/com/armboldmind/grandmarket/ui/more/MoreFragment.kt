package com.armboldmind.grandmarket.ui.more

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Language
import com.armboldmind.grandmarket.data.models.domain.More
import com.armboldmind.grandmarket.data.models.domain.User
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentMoreBinding
import com.armboldmind.grandmarket.databinding.LayoutUserBinding
import com.armboldmind.grandmarket.databinding.LayoutWelcomeBinding
import com.armboldmind.grandmarket.shared.customview.DialogFactory
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.preferencesManager
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.MainActivity
import com.armboldmind.grandmarket.ui.auth.AuthorizationActivity
import com.armboldmind.grandmarket.ui.auth.viewmodels.UserViewModel
import com.armboldmind.grandmarket.ui.more.notifications.NotificationsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoreFragment : BaseFragment<FragmentMoreBinding>() {

    private val mUserViewModel: UserViewModel by lazy { (activity as MainActivity).createViewModel(UserViewModel::class.java, this) }
    private val mNotificationViewModel: NotificationsViewModel by lazy { (activity as MainActivity).createViewModel(NotificationsViewModel::class.java, this) }
    private val mMoreViewModel: MoreViewModel by lazy { createViewModel(MoreViewModel::class.java, this) }

    private lateinit var mUserBinding: LayoutUserBinding
    private lateinit var mWelcomeBinding: LayoutWelcomeBinding

    private val items = ArrayList<More>()
    private val mGlobalAdapter = MoreAdapter { action ->
        action?.let {
            view?.findNavController()
                ?.navigate(it)
        }
    }

    private var mCurrentLanguage = ""

    private val authorization = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val user: User? = preferencesManager().findByKey(BundleKeysEnum.USER.key)
            user?.let { setMoreItems(it) }
        }
    }
    private var isInitialized = false
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMoreBinding
        get() = FragmentMoreBinding::inflate

    override fun initView(binding: FragmentMoreBinding, keysFromDb: Keys) {
        if (!isInitialized) {
            isInitialized = true
            lifecycleScope.launch {
                mMoreViewModel.getKeys()
                    .collect {
                        mKeys = keysFromDb
                        mCurrentLanguage = preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).uniqueSeoCode
                        mUserViewModel.sendIntent(ActionIntent.GetUserInfoActionIntent(false))
                        mUserBinding = LayoutUserBinding.inflate(requireContext().inflater(), mBinding.header, true)
                        mWelcomeBinding = LayoutWelcomeBinding.inflate(requireContext().inflater(), mBinding.header, true)
                            .apply { keys = it }
                        mUserViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                            setMoreItems(user)
                        }
                        mWelcomeBinding.signIn.setOnClickListener {
                            authorization.launch(android.content.Intent((activity as MainActivity), AuthorizationActivity::class.java))
                            (activity as MainActivity).overridePendingTransition(R.anim.fade_in_500, R.anim.fade_out_500)
                        }
                        mUserBinding.logout.setOnClickListener {
                            DialogFactory.Builder(requireContext())
                                .title(keysFromDb.sign_out)
                                .description(keysFromDb.sign_out_popup_text)
                                .positiveButtonText(keysFromDb.sign_out)
                                .negativeButtonText(keysFromDb.cancel)
                                .positiveButtonClick {
                                    mUserViewModel.sendIntent(ActionIntent.SignOutActionIntent)
                                }
                                .build()
                        }
                    }
            }
        }
    }

    private fun setMoreItems(user: User? = null) {
        items.clear()
        mBinding.header.removeAllViews()
        if (user != null) {
            mMoreViewModel.sendIntent(ActionIntent.FetchUserItems)
            mBinding.header.addView(mUserBinding.root)
            mUserBinding.user = user
        } else { //   items.add(More(8, R.drawable.ic_requests, mKeys.request_product, MoreFragmentDirections.actionMoreFragmentToRequestedProductsFragment()))
            items.add(More(8, R.drawable.ic_requests, mKeys.request_product, null))
            mMoreViewModel.sendIntent(ActionIntent.FetchGlobalItems)
            mBinding.header.addView(mWelcomeBinding.root)
        }
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.FetchUserItemsState -> {
                items.clear()
                items.addAll(viewState.items)
                mMoreViewModel.sendIntent(ActionIntent.FetchGlobalItems)
            }
            is ViewState.FetchGlobalItemsState -> {
                items.addAll(viewState.items)
                initRecycler()
            }
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> if (isResumed) showError(message = viewState.exception) { mUserViewModel.sendIntent(ActionIntent.SignInAsGuestActionIntent) }
            is ViewState.MoreSignedOutState -> {
                items.clear() //   items.add(More(8, R.drawable.ic_requests, mKeys.request_product, MoreFragmentDirections.actionMoreFragmentToRequestedProductsFragment()))
                items.add(More(8, R.drawable.ic_requests, mKeys.request_product, null))
                mMoreViewModel.sendIntent(ActionIntent.FetchGlobalItems)
                mBinding.header.removeAllViews()
                mBinding.header.addView(mWelcomeBinding.root)
                setLoading(false)
                (activity as MainActivity).checkBottomNavigationProfileIcon()
            }
            else -> {
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    private fun initRecycler() {
        mBinding.moreItems.adapter = mGlobalAdapter.apply { submitList(items) }
    }

    override fun onResume() {
        super.onResume()
        mMoreViewModel.setStateHandler(this::handleViewState)
        if (mCurrentLanguage != preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).uniqueSeoCode) {
            mCurrentLanguage = preferencesManager().findByKey<Language>(BundleKeysEnum.APP_LANGUAGE.key).uniqueSeoCode
            isInitialized = false
            initView(mBinding, mKeys)
        }
        initUserInfo()
        items.map { item ->
            if (item.id == 4) {
                mNotificationViewModel.unseenNotificationsLiveData.observe(viewLifecycleOwner) { count ->
                    item.badgeCount.value = count
                }

            }
        }
    }

    private fun initUserInfo() {
        val user: User? = preferencesManager().findByKey<User?>(BundleKeysEnum.USER.key)
            ?.apply {
                if (phoneNumber.length == 12) {
                    val countryCode = phoneNumber.substring(0, 4)
                    val code = phoneNumber.substring(4, 6)
                    val number1 = phoneNumber.substring(6, 8)
                    val number2 = phoneNumber.substring(8, 10)
                    val number3 = phoneNumber.substring(10, 12)
                    phoneNumber = "$countryCode $code $number1 $number2 $number3"
                }
            }
        mUserBinding.user = user
        (activity as MainActivity).checkBottomNavigationProfileIcon()
    }
}