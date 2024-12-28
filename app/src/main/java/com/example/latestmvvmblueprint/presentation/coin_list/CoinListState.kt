package com.example.latestmvvmblueprint.presentation.coin_list

import com.example.latestmvvmblueprint.domain.model.Coin

data class CoinListState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isSwitchOn: Boolean =true  // Switch durumu
)
