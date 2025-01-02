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
    operator fun invoke( isSwitchOn: Boolean = true,
                         searchBySymbolText: String ="",
                         isNewCoinsSwitchOn: Boolean = false,
                         sortByNameCheckboxIsChecked: Boolean = false
    ): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading<List<Coin>>())

            val coins = repository.getCoins().map { it.toCoin() }

            // Filtreleme işlemleri
            var filteredCoins = coins

            // SYMBOL filtreleme
            if (searchBySymbolText.isNotEmpty()) {
                filteredCoins = filteredCoins.filter {
                    it.symbol.uppercase() == searchBySymbolText
                }
            }

            // Active/Passive kontrolü
            if (isSwitchOn) {
                filteredCoins = filteredCoins.filter { it.isActive }
            }

            // Yeni coin kontrolü
            if (isNewCoinsSwitchOn) {
                filteredCoins = filteredCoins.filter { it.isNew }
            }

            // Alfabetik sıralama
            if (sortByNameCheckboxIsChecked) {
                filteredCoins = filteredCoins.sortedBy { it.name }
            }

            emit(Resource.Success(filteredCoins))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Coin>>(e.localizedMessage ?: "An Error Occurred."))
        } catch (e: IOException) {
            emit(Resource.Error<List<Coin>>(e.localizedMessage ?: "Bad internet or Server Connection"))
        }
    }
}
