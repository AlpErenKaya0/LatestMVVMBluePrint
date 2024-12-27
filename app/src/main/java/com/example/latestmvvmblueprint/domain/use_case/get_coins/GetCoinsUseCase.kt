package com.example.latestmvvmblueprint.domain.use_case.get_coins

import com.example.latestmvvmblueprint.common.Resource
import com.example.latestmvvmblueprint.data.remote.dto.toCoin
import com.example.latestmvvmblueprint.domain.model.Coin
import com.example.latestmvvmblueprint.domain.repository.CoinRepository
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading<List<Coin>>())
            val coins = repository.getCoins().map { it.toCoin() }
            emit(Resource.Success(coins))
        } catch(e: HttpException) {
            emit(Resource.Error<List<Coin>>(e.localizedMessage ?: "An Error Occured."))
        } catch(e: IOException) {
            emit(Resource.Error<List<Coin>>(e.localizedMessage ?: "Bad internet or Server Connection"))
        }
    }
}