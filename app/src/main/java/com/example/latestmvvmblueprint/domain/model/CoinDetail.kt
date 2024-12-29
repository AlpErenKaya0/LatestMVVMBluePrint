package com.example.latestmvvmblueprint.domain.model

import com.example.latestmvvmblueprint.data.remote.dto.TeamMember

data class CoinDetail(
    val coinId: String,
    val name: String,
    val description: String,
    val symbol: String,
    val rank: Int,
    val isActive: Boolean,
    val tags: List<String>,
    val team: List<TeamMember>,
    val isNew: Boolean
)