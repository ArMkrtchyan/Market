package com.armboldmind.grandmarket.ui.more.support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.searchRequestModel
import com.armboldmind.grandmarket.databinding.FragmentFaqBinding
import com.armboldmind.grandmarket.shared.globalextensions.getColorCompat
import com.armboldmind.grandmarket.shared.globalextensions.getDrawableCompat
import com.armboldmind.grandmarket.shared.globalextensions.gone
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.more.informative.InfoViewModel
import kotlinx.coroutines.launch


class FAQFragment : BaseFragment<FragmentFaqBinding>() {
    private val mInfoViewModel by lazy { createViewModel(InfoViewModel::class.java, this) }

    private val mBodyAdapter by lazy { FAQBodyAdapter() }
    private val secondLoadingLiveData = MutableLiveData(false)
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFaqBinding
        get() = FragmentFaqBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentFaqBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            mInfoViewModel.sendIntent(ActionIntent.FetchFAQData(searchRequestModel { }))
            faqRecycler.adapter = mBodyAdapter
            searchLayout.apply { findViewById<View>(R.id.text_input_end_icon).setBackgroundColor(requireContext().getColorCompat(R.color.transparent)) }
            search.doAfterTextChanged {
                if (it.toString()
                        .isEmpty()
                ) {
                    searchLayout.endIconDrawable = requireContext().getDrawableCompat(R.drawable.ic_search)
                    searchLayout.setEndIconOnClickListener { }
                } else {
                    searchLayout.endIconDrawable = requireContext().getDrawableCompat(R.drawable.ic_close)
                    searchLayout.setEndIconOnClickListener { search.text?.clear() }
                }
                mInfoViewModel.sendIntent(ActionIntent.FetchFAQData(searchRequestModel { searchText = it.toString() }, true))
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> {
                setLoading(true)
                secondLoadingLiveData.postValue(false)
            }
            is ViewState.SecondaryLoadingState -> {
                setLoading(false)
                secondLoadingLiveData.postValue(true)
            }
            is ViewState.ErrorState -> {
                lifecycleScope.launch {
                    mBinding.loadingView.setViewState(ViewState.ErrorState(viewState.exception))
                    mBinding.loadingView.setOnButtonClick(mKeys.retry) { mInfoViewModel.sendIntent(ActionIntent.FetchFAQData(searchRequestModel { })) }
                }
                secondLoadingLiveData.postValue(false)
            }
            is ViewState.EmptyState -> {
                mBinding.faqRecycler.gone()
                mBinding.empty.show()
                secondLoadingLiveData.postValue(false)
                mBodyAdapter.setAnswers(arrayListOf())
                mBodyAdapter.submitList(arrayListOf())
            }
            is ViewState.FetchFaqData -> {
                setLoading(false)
                mBinding.faqRecycler.show()
                mBinding.empty.gone()
                secondLoadingLiveData.postValue(false)
                mBodyAdapter.setAnswers(viewState.faqAnswerList)
                mBodyAdapter.submitList(viewState.faqQuestionList)
            }
            else -> {
            }
        }
    }


}