package com.armboldmind.grandmarket.ui.more.informative

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.NewsAndEvents
import com.armboldmind.grandmarket.data.models.domain.NewsAndEventsDetails
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentNewsDetailsBinding
import com.armboldmind.grandmarket.shared.globalextensions.invisible
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.shared.utils.AnimationUtil
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


class NewsDetailsFragment : BaseFragment<FragmentNewsDetailsBinding>() {

    private val mInfoViewModel by lazy { createViewModel(InfoViewModel::class.java, this) }
    private val mArgs: NewsDetailsFragmentArgs by navArgs()
    private val adapter = NewsImagesAdapter()
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsDetailsBinding
        get() = FragmentNewsDetailsBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentNewsDetailsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            this.newsAndEvents = mArgs.newsAndEvent
            if (mArgs.id != 0L) mInfoViewModel.sendIntent(ActionIntent.GetNewsDetails(mArgs.id)) else {
                mInfoViewModel.sendIntent(ActionIntent.GetNewsDetails(mArgs.newsAndEvent?.id ?: 0))
                content.show()
                tabLayout.invisible()
                adapter.submitList(arrayListOf<NewsAndEventsDetails.MediaPreviews>().apply { add(NewsAndEventsDetails.MediaPreviews(mArgs.newsAndEvent?.newsCoverPhoto ?: "")) })
                AnimationUtil.alphaFrom0To1(description)
                initAnimation()
            }
            imagesPager.adapter = adapter
            imagesPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            TabLayoutMediator(tabLayout, imagesPager) { tab, position -> }.attach()
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(mArgs.id != 0L)
            is ViewState.ErrorState -> showError(message = viewState.exception) { }
            is ViewState.FetchNewsDetails -> lifecycleScope.launch {
                setLoading(false);mBinding.newsAndEvents = NewsAndEvents(viewState.news.id, viewState.news.title, viewState.news.description, "", viewState.news.createdDate)
                adapter.submitList(viewState.news.mediaPreview)
                mBinding.content.show()
                if (viewState.news.mediaPreview.size > 1) mBinding.tabLayout.show() else mBinding.tabLayout.invisible()
            }
            else -> Unit
        }
    }


}