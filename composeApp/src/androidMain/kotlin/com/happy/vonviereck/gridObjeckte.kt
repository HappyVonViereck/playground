package com.happy.vonviereck

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class gridObjeckte {

    var xCordPos by mutableStateOf(0)
    var yCordPos by mutableStateOf(0)
    var currentTile by mutableStateOf<Tile?>(null)

    fun sucheTile(xCord: Int, yCord: Int, allTiles: List<Tile>): Tile {

        val tile = allTiles.find { it.xCord == xCord && it.yCord == yCord }

        var failtile = Tile()
        failtile.xCord = -1
        failtile.yCord = -1

        if (tile == null) {
            Log.d("GridObject", "Tile ($xCord, $yCord) nicht gefunden")
            return failtile
        }

        Log.d("GridObject", "Tile gefunden: (${tile.xCord}, ${tile.yCord})")
        return tile
    }

    fun moveTo(xCord: Int, yCord: Int, allTiles: List<Tile>): Boolean {
        val tile = sucheTile(xCord, yCord, allTiles)

        if (!tile.darfGehen &&
            tile.xCord != -1 &&
            tile.yCord != -1
        ) {
            Log.d("GridObject", "Tile ($xCord, $yCord) ist eine Wand")
            return false
        }
        if (tile.xCord != -1 && tile.yCord != -1)

            xCordPos = tile.xCordPos
            yCordPos = tile.yCordPos
            currentTile = tile

            Log.d(
                "GridObject",
                "${this::class.simpleName} bewegt zu (${tile.xCord}, ${tile.yCord})"
            )
            return true
        }
    }
