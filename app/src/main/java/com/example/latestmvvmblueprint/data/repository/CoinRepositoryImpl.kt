package com.example.latestmvvmblueprint.data.repository

import com.example.latestmvvmblueprint.data.remote.CoinPaprikaApi
import com.example.latestmvvmblueprint.data.remote.dto.CoinDetailDto
import com.example.latestmvvmblueprint.data.remote.dto.CoinDto
import com.example.latestmvvmblueprint.domain.repository.CoinRepository
import dagger.assisted.AssistedInject
import javax.inject.Inject

class CoinRepositoryImpl @AssistedInject constructor(
    private val api: CoinPaprikaApi
): CoinRepository {
    override suspend fun getCoins(): List<CoinDto> {
        return api.getCoins()
    }
    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }

}