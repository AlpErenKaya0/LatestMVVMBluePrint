package com.example.latestmvvmblueprint.domain.repository

import com.example.latestmvvmblueprint.data.remote.dto.CoinDetailDto
import com.example.latestmvvmblueprint.data.remote.dto.CoinDto

interface CoinRepository {
    suspend fun  getCoins(): List<CoinDto>
    suspend fun getCoinById(coinId:String): CoinDetailDto
}