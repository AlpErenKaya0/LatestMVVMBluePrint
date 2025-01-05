package com.example.latestmvvmblueprint.presentation.coin_detail

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.latestmvvmblueprint.R
import com.example.latestmvvmblueprint.presentation.coin_detail.components.CoinTag
import com.example.latestmvvmblueprint.presentation.coin_detail.components.TeamListItem
import com.example.latestmvvmblueprint.presentation.view_model.CoinDetailViewModel
import com.google.accompanist.flowlayout.FlowRow
@Composable
fun CoinDetailScreen(
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    var isCardOpen by remember { mutableStateOf(true) }


    Box(modifier = Modifier.fillMaxSize()
        .padding(top = 50.dp)
    ) {
        if (isCardOpen && !state.isLoading && !state.error.isNotBlank() ) {
            Spacer(modifier = Modifier.height(15.dp))
            Card(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .padding(16.dp)
                    .clickable(onClick = {isCardOpen= false})
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp)),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF2C3B2D),
                                    Color(0xFF8EC9A6),
                                    Color(0xFFBFEAA0),
                                    Color(0xFFBFECC0),
                                )
                            )
                        )
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Rank: ${state.coin?.rank ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        )

                        Text(
                            text = state.coin?.name ?: "Unknown",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontSize = 24.sp
                            ),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        )
                        Text(
                            text = "Rank: ${state.coin?.symbol ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }

















        else {
            state.coin?.let { coin ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = buildString {
                                    append(
                                        coin.rank?.toString() ?: "N/A"
                                    ) // Null kontrolü yapılıyor, varsayılan "N/A"
                                    append(". ")
                                    append(
                                        coin.name ?: "Unknown"
                                    ) // Null kontrolü yapılıyor, varsayılan "Unknown"
                                    append(" (")
                                    append(
                                        coin.symbol ?: "Symbol Missing"
                                    ) // Null kontrolü yapılıyor, varsayılan mesaj
                                    append(")")
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(8f)
                            )
                            Text(
                                text = if (coin.isActive) "active" else "inactive",
                                color = if (coin.isActive) Color.Green else Color.Red,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .align(CenterVertically)
                                    .weight(2f)
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        // Null kontrolü ile açıklama
                        Text(
                            text = coin.description ?: "No description available",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "Tags",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        FlowRow(
                            mainAxisSpacing = 10.dp,
                            crossAxisSpacing = 10.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Null kontrolü ile tag listesi
                            coin.tags?.forEach { tag ->
                                CoinTag(tag = tag)
                            } ?: Text("No tags available")
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "Team members",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                    // Null kontrolü ile team listesi
                    items(coin.team ?: emptyList()) { teamMember ->
                        TeamListItem(
                            teamMember = teamMember,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                    }
                }
            }
            if(state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
            if(state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}