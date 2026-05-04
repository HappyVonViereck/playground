package com.happy.vonviereck

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.File

class LaoderForTile {
    fun saveGrid(context: Context, tiles: List<Tile>, gridName: String) {
        val tileDataList = tiles.map { TileData(it.xCord, it.yCord, it.darfGehen, it.darfGehen2) }
        val gridData = GridData(name = gridName, tiles = tileDataList)

        val json = Gson().toJson(gridData)
        val file = File(context.filesDir, "$gridName.json")
        file.writeText(json)

        Log.d("Grid", "Grid gespeichert: $gridName bei ${context.filesDir}")
    }

    fun loadGrid(context: Context, gridName: String, tiles: List<Tile>) {
        val file = File(context.filesDir, "$gridName.json")
        if (!file.exists()) {
            Log.d("Grid", "Keine Datei gefunden: $gridName")
            return
        }

        val gridData = Gson().fromJson(file.readText(), GridData::class.java)

        gridData.tiles.forEach { tileData ->
            val tile = tiles.find { it.xCord == tileData.xCord && it.yCord == tileData.yCord }
            tile?.let {
                /*if (tileData.darfGehen) {
                    it.currentImageRes = R.drawable.tileboden23542352
                    it.darfGehen = true
                } else {
                    it.makeWandTile()
                    it.darfGehen=false
                }*/
                if (tileData.darfGehen2 == 0) {
                    it.currentImageRes = R.drawable.tileboden23542352
                    it.darfGehen2 = 0
                } else {
                    it.makeWandTile()
                    it.darfGehen2 = 1
                }
            }
        }

        Log.d("Grid", "Grid geladen: $gridName")
    }

    // Gibt alle gespeicherten Level-Namen zurück
    fun getAllSavedLevels(context: Context): List<String> {
        return context.filesDir
            .listFiles { file -> file.name.endsWith(".json") }
            ?.map { it.nameWithoutExtension }
            ?: emptyList()
    }

    fun deleteGrid(context: Context, gridName: String) {
        val file = File(context.filesDir, "$gridName.json")
        if (file.exists()) {
            file.delete()
            Log.d("Grid", "Grid gelöscht: $gridName")
        } else {
            Log.d("Grid", "Datei nicht gefunden: $gridName")
        }
    }
}