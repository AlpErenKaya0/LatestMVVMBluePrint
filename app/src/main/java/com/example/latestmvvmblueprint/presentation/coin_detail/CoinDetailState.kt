package com.example.latestmvvmblueprint.presentation.coin_detail

import com.example.latestmvvmblueprint.domain.model.CoinDetail

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val error: String = ""
)