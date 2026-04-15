package com.happy.vonviereck

import kotlinx.serialization.Serializable

@Serializable
data class TileData(
    val xCord: Int = 0,
    val yCord: Int = 0,
    val darfGehen: Boolean = true
)

@Serializable
data class GridData(
    val name: String,
    val tiles: List<TileData>
)

