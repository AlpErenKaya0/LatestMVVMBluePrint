package com.example.latestmvvmblueprint.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.latestmvvmblueprint.common.Resource
import com.example.latestmvvmblueprint.domain.use_case.get_coins.GetCoinsUseCase
import com.example.latestmvvmblueprint.presentation.coin_list.CoinListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CoinListState())
    private val _searchBySymbolText = mutableStateOf("")
    private val _isNewCoinsSwitchOn = mutableStateOf(true)
    private val _sortByNameCheckboxIsChecked = mutableStateOf(false)

    val state: State<CoinListState> = _state

    init {
        getCoins() // İlk başta verileri çekiyoruz
    }

    private fun getCoins() {
        getCoinsUseCase(
            isSwitchOn = _state.value.isJustNewDataSwitchOn,
            searchBySymbolText = _searchBySymbolText.value,
            isNewCoinsSwitchOn = _isNewCoinsSwitchOn.value,
            sortByNameCheckboxIsChecked = _sortByNameCheckboxIsChecked.value
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        coins = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message ?: "An unexpected error occurred",
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    // Filtreleme ve sıralama durumu güncelleme
    fun FilterElementsStatus(
        isJustNewDataSwitchOn: Boolean,
        searchBySymbolText: String,
        isNewCoinsSwitchOn: Boolean,
        sortByNameCheckboxIsChecked: Boolean
    ) {
        // Durumları güncelle
        _state.value = _state.value.copy(isJustNewDataSwitchOn = isJustNewDataSwitchOn)
        _searchBySymbolText.value = searchBySymbolText.uppercase() // Arama metnini büyük harfe çevir
        _isNewCoinsSwitchOn.value = isNewCoinsSwitchOn
        _sortByNameCheckboxIsChecked.value = sortByNameCheckboxIsChecked

        // Yeni verileri çek
        getCoins()
    }
}
