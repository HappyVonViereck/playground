package com.happy.vonviereck

import android.media.Image
import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.interaction.DragInteraction
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.json.Json

// ─── ViewModel ───────────────────────────────────────────────────────────────

class GameViewModel : ViewModel() {
    val allTiles = mutableListOf<Tile>()
}

// ─── Activity ────────────────────────────────────────────────────────────────

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

// ─── Konstanten ──────────────────────────────────────────────────────────────

val sizeForImg = 40.dp

// ─── App Einstiegspunkt ──────────────────────────────────────────────────────

@Composable
fun App(vm: GameViewModel = viewModel()) {
    mainScreen()
    createTileSet(vm.allTiles)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        createBtns(vm.allTiles)
    }
}

// ─── Buttons ─────────────────────────────────────────────────────────────────

@Composable
fun createBtns(allTiles: MutableList<Tile>) {
    val context = LocalContext.current
    val loader = LaoderForTile()
    var savedLevels by remember { mutableStateOf(loader.getAllSavedLevels(context)) }

    var i=1

    // Dialog-State
    var showSaveDialog by remember { mutableStateOf(false) }
    var levelName by remember { mutableStateOf("") }

    Column {
        Button(onClick = { showSaveDialog = true }) {
            Text("Speichern")
        }

        savedLevels.forEach { name ->
            Button(onClick = { loader.loadGrid(context, name, allTiles) },//TODO:Den safe button gestalten sieht komsich aus
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,        // Hintergrundfarbe
                    contentColor = Color.Black         // Text/Icon Farbe
                )
            )
            {
                Text("$i $name")
                i++

                //TODO:Maybe alles in eine Box wrappen aber nur das Icon und der Button damit der Buton ne eigene grösse hat?
                Button(onClick = {} , modifier = Modifier.size(10.dp)) { //TODO: 10 ist zu klein und 50 sieht man erst das icon ist aber dann zu gross die click fläche
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Löschen",
                            tint=Color.Red
                    )
                }
            }
        }
    }

    // Speichern-Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = {
                showSaveDialog = false
                levelName = ""
            },
            title = { Text("Level benennen") },
            text = {
                OutlinedTextField(
                    value = levelName,
                    onValueChange = { levelName = it },
                    label = { Text("Level-Name") },
                    singleLine = true,
                    placeholder = { Text("z.B. level1") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (levelName.isNotBlank()) {
                            loader.saveGrid(context, allTiles, levelName)
                            savedLevels = loader.getAllSavedLevels(context)
                            showSaveDialog = false
                            levelName = ""
                        }
                    }
                ) {
                    Text("Speichern")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    showSaveDialog = false
                    levelName = ""
                }) {
                    Text("Abbrechen")
                }
            }
        )
    }
}

// ─── TileSet ─────────────────────────────────────────────────────────────────

@Composable
fun createTileSet(allTiles: MutableList<Tile>) {
    Box {
        HorizontalGrid(rows = 10, columns = 10, allTiles = allTiles)
    }
}

// ─── Grid + Maus ─────────────────────────────────────────────────────────────

@Composable
fun HorizontalGrid(rows: Int, columns: Int, allTiles: MutableList<Tile>) {
    val maus = remember { Maus() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            repeat(rows) { row ->
                Row {
                    repeat(columns) { col ->
                        val tileBlock = remember(row, col) {
                            Tile().also { tile ->
                                tile.xCord = col
                                tile.yCord = row
                                allTiles.add(tile)
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = CutCornerShape(0.dp),
                            modifier = Modifier.size(sizeForImg)
                        ) {
                            // Rand-Tiles als Mauer setzen
                            if (col == 0 || col == columns - 1 ||
                                row == 0 || row == rows - 1
                            ) {
                                LaunchedEffect(Unit) { tileBlock.toggleTile() }
                            }
                            tileBlock.createATile(maus)
                        }
                    }
                }
            }
        }

        maus.createMaus()
        maus.moveMouse(435, 1095)
    }
}


