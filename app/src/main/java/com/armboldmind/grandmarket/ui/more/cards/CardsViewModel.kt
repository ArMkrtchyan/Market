package com.armboldmind.grandmarket.ui.more.cards

import androidx.lifecycle.viewModelScope
import com.armboldmind.grandmarket.base.BaseViewModel
import com.armboldmind.grandmarket.data.models.domain.Card
import com.armboldmind.grandmarket.shared.mvi.intents.ActionIntent
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import kotlinx.coroutines.launch

class CardsViewModel : BaseViewModel() {
    private val list = arrayListOf<Card>().apply {
        add(Card(1, isDefault = true))
        add(Card(2))
        add(Card(3))
        add(Card(4))
        add(Card(5))
        add(Card(6))
    }

    override fun sendIntent(actionIntent: ActionIntent) {
        viewModelScope.launch {
            when (actionIntent) {
                is ActionIntent.GetCards -> getAllCards()
                is ActionIntent.AddCard -> addCard()
                is ActionIntent.DeleteCard -> deleteCard(actionIntent.card)
                else -> Unit
            }
        }
    }

    private suspend fun getAllCards() {
        setState.invoke(ViewState.GetAllCards(list.toMutableList()))
    }

    private suspend fun addCard() {
        setState.invoke(ViewState.AddNewCard)
    }

    private suspend fun deleteCard(card: Card) {
        list.remove(card)
        getAllCards()
    }

}