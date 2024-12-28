package com.example.latestmvvmblueprint.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.latestmvvmblueprint.common.Resource
import com.example.latestmvvmblueprint.domain.use_case.get_coins.GetCoinsUseCase
import com.example.latestmvvmblueprint.presentation.coin_list.CoinListState
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
): ViewModel() {
    private val _state = mutableStateOf(CoinListState())
    val state: State<CoinListState> = _state

    init {
        getCoins() // İlk başta verileri çekiyoruz
    }

    private fun getCoins(){
        getCoinsUseCase(_state.value.isSwitchOn).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = CoinListState(coins = result.data ?: emptyList(), isSwitchOn = _state.value.isSwitchOn)
                }
                is Resource.Error -> {
                    _state.value = CoinListState(error = result.message ?: "An unexpected error occurred", isSwitchOn = _state.value.isSwitchOn)
                }
                is Resource.Loading -> {
                    _state.value = CoinListState(isLoading = true, isSwitchOn = _state.value.isSwitchOn)
                }
            }
        }.launchIn(viewModelScope)
    }

    // Switch durumu değiştiğinde çağrılacak fonksiyon
    fun toggleSwitch(isChecked: Boolean) {
        _state.value = _state.value.copy(isSwitchOn = isChecked) // Switch durumu güncelleniyor
        getCoins() // Switch durumu değiştiğinde verileri tekrar çekiyoruz
    }
}
