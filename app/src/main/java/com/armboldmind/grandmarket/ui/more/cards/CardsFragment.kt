package com.armboldmind.grandmarket.ui.more.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.domain.Card
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentCardsBinding
import com.armboldmind.grandmarket.shared.customview.DialogFactory
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState


class CardsFragment : BaseFragment<FragmentCardsBinding>() {
    private val mCardsViewModel by lazy { createViewModel(CardsViewModel::class.java, this) }
    private val mAdapter by lazy { AdapterCards(this::deleteCard) }
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCardsBinding
        get() = FragmentCardsBinding::inflate


    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentCardsBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            cards.adapter = mAdapter
            add.setOnClickListener { mCardsViewModel.sendIntent(ActionIntent.AddCard) }
            mCardsViewModel.sendIntent(ActionIntent.GetCards)
        }
    }

    override fun setLoading(isLoading: Boolean) {
        mBinding.loadingView.setViewState(if (isLoading) ViewState.LoadingViewState else ViewState.SuccessState())
    }

    override fun handleViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> setLoading(true)
            is ViewState.ErrorState -> showError(message = viewState.exception) { }
            is ViewState.GetAllCards -> {
                setLoading(false)
                mAdapter.submitList(viewState.cards)
            }
            is ViewState.AddNewCard -> navigateToAddNewCardScreen()
            else -> Unit
        }
    }

    private fun deleteCard(card: Card) {
        DialogFactory.Builder(requireContext())
            .title(getString(R.string.delete_card_title))
            .description(getString(R.string.delete_card_description))
            .positiveButtonText(getString(R.string.delete))
            .negativeButtonText(getString(R.string.cancel))
            .positiveButtonClick {
                mCardsViewModel.sendIntent(ActionIntent.DeleteCard(card))
            }
            .build()
    }

    private fun navigateToAddNewCardScreen() {
        view?.findNavController()
            ?.navigate(CardsFragmentDirections.actionCardsFragmentToAddCardFragment())
    }
}