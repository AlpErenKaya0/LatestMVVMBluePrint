package com.example.latestmvvmblueprint.presentation.coin_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.latestmvvmblueprint.presentation.coin_list.components.CoinListItem
import com.example.latestmvvmblueprint.presentation.navigation.Screen
import com.example.latestmvvmblueprint.presentation.view_model.CoinListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    navController: NavController,
    viewModel: CoinListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.value
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchBySymbolText by remember { mutableStateOf(TextFieldValue("")) }
    var justNewDataChecked by remember { mutableStateOf(false) }
    var isNewestCoinsSwitchOn by remember { mutableStateOf(false) }
    var sortByNameCheckboxIsChecked by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Switch widget'ı
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    onClick = {

                        showBottomSheet = true

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Show Filters")
                }



            }

            Text(
                text = if (state.isJustNewDataSwitchOn) "All Active Coins" else "All Coins",
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.coins) { coin ->
                    CoinListItem(
                        coin = coin,
                        onItemClick = {
                            if (navController != null) navController.navigate(Screen.CoinDetailScreen.route + "/${coin.id}")
                        })
                }
            }
        }
        if(state.error.isNotBlank()){
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp )
                    .align(Alignment.Center)
            )
        }
        if (state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 16.dp,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(50.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        ) {
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 6.dp, top = 6.dp),
                onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false

                        //viewModel'a coroutineScope içinden erişmeme gerek var mı emin değilim

                            viewModel.FilterElementsStatus(
                                isJustNewDataSwitchOn = justNewDataChecked,
                                searchBySymbolText = searchBySymbolText.text.toString(),
                                isNewCoinsSwitchOn = isNewestCoinsSwitchOn,
                                sortByNameCheckboxIsChecked = sortByNameCheckboxIsChecked
                            )

                        }
                    }
                }
            ) {
                Text("Apply Filters")
            }

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = searchBySymbolText,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = {
                        searchBySymbolText = it
                    },
                    label = { Text(text = "Search a Crypto By Symbol") },
                    placeholder = { Text(text = "Have to search like BTC for Bitcoin") },
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (justNewDataChecked) "Just Show Active Coins" else "Show All Coins",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Switch(
                    checked = justNewDataChecked,
                    onCheckedChange = { checked ->
                        justNewDataChecked = checked
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isNewestCoinsSwitchOn) "Just Fresh Coins" else "Show All Coins",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Switch(
                    checked = isNewestCoinsSwitchOn,
                    onCheckedChange = { checked ->
                        isNewestCoinsSwitchOn = checked
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "alphabetically",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Checkbox(
                    checked = sortByNameCheckboxIsChecked,
                    onCheckedChange = { checked ->
                        sortByNameCheckboxIsChecked = checked
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
