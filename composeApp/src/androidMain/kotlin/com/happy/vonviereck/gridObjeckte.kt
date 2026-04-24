package com.happy.vonviereck

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class gridObjeckte {

    var xCordPos by mutableStateOf(0)
    var yCordPos by mutableStateOf(0)
    var currentTile by mutableStateOf<Tile?>(null)

    fun sucheTile(xCord: Int, yCord: Int, allTiles: List<Tile>): Tile? {

        val tile = allTiles.find { it.xCord == xCord && it.yCord == yCord }

        if (tile == null) {
            Log.d("GridObject", "Tile ($xCord, $yCord) nicht gefunden")
        } else {
            Log.d("GridObject", "Tile gefunden: (${tile.xCord}, ${tile.yCord})")
        }

        return tile
    }
    fun moveTo(xCord: Int, yCord: Int, allTiles: List<Tile>): Boolean {
        val tile = sucheTile(xCord, yCord, allTiles) ?: return false

        if (!tile.darfGehen) {
            Log.d("GridObject", "Tile ($xCord, $yCord) ist eine Wand")
            return false
        }

        xCordPos = tile.xCordPos
        yCordPos = tile.yCordPos
        currentTile = tile
        return true
    }

    }
