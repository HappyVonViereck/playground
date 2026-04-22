package com.happy.vonviereck

import android.R
import android.R.attr.icon
import android.R.attr.name
import android.media.Image
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.json.Json
import learningareproject.composeapp.generated.resources.Res
import kotlin.math.log

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
    Box(modifier = Modifier.fillMaxSize()) {
        mainScreen()
        createTileSet(vm.allTiles)

        // Buttons ganz oben im Z-Stack
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .align(Alignment.TopCenter)
                .zIndex(10f),  // ← liegt über allem

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            createBtns(vm.allTiles)
            dropDownMenu()
        }
    }
}

// ─── Buttons ─────────────────────────────────────────────────────────────────

@Composable
fun createBtns(allTiles: MutableList<Tile>) {
    val context = LocalContext.current
    val loader = LaoderForTile()
    var savedLevels by remember { mutableStateOf(loader.getAllSavedLevels(context)) }

    // Dialog-State
    var showSaveDialog by remember { mutableStateOf(false) }
    var levelName by remember { mutableStateOf("") }

    Column {
        Button(onClick = {

            Log.d("Btn", "es wurde auf speichern gedrückt ")
            showSaveDialog = true })
        {

            Text("Speichern")  //TODO:Den speichern btn von der logik der anderen lvl btns trennen
        }

//Lvl loader mehtoden abschnitt
//        savedLevels?.forEach { name ->
//            Button(onClick = { loader.loadGrid(context, name, allTiles)
//                             /*Hier die mehtode rein tun damit der lvl name in das textfield oben geht*/
//                             },//TODO:Den safe button gestalten sieht komsich aus
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Blue,        // Hintergrundfarbe
//                    contentColor = Color.Black         // Text/Icon Farbe
//                )
//            )
//            {
//
//                Text("$levelName")
//
//            }
//
//         }
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
//                            savedLevels = loader.getAllSavedLevels(context)
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
//                    levelName = ""
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


@Composable
fun dropDownMenu(vm: GameViewModel = viewModel()){
    var expandBool = remember { mutableStateOf(false) }
//    var gridList by remember { mutableStateOf(listOf<String>("h","b","c","b")) }
    var selectedDataGrid = remember { mutableStateOf("") }
    var textfiledSize by remember { mutableStateOf(Size.Zero) }

    //ToogleSwitch
    val iconToggle= if(expandBool.value){
        Icons.Filled.KeyboardArrowUp
    }else{
        Icons.Filled.KeyboardArrowDown
    }

    Column(modifier = Modifier.padding(20.dp)) {
    OutlinedTextField(
            value = selectedDataGrid.value,
            onValueChange = {selectedDataGrid.value=it},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textfiledSize = coordinates.size.toSize()
                },
            label = {Text(text="Level auswählen")},
            trailingIcon = {
                Icon(iconToggle, "", Modifier.clickable { expandBool.value = !expandBool.value })
            }
        )

        DropdownMenu(
            expanded=expandBool.value,
            onDismissRequest = {expandBool.value=false},
            modifier = Modifier
                .width(with(LocalDensity.current){textfiledSize.width.toDp()})
        ) {

gridLadenButtons(vm.allTiles,enabledState=expandBool,selectedDataGrid)

        }
    }
}

//Ziel: die laden buttons mit den dropdown verbinden->fertig
@Composable
fun gridLadenButtons(allTiles: MutableList<Tile>,enabledState: MutableState<Boolean>,titel:MutableState<String>){
    createBtns(allTiles)
//Lvl loader mehtoden abschnitt
    val context = LocalContext.current
    val loader = LaoderForTile()
    var savedLevels by remember { mutableStateOf(loader.getAllSavedLevels(context)) }

    savedLevels.forEach { label ->
        Log.d("Btn", "createBtns: hier ist die liste gridList:${savedLevels.size} es wurde grade $label hinuzugefügt")
        DropdownMenuItem(
            { Text(text=label) },
            onClick = {
                loader.loadGrid(context, label, allTiles)
enabledState.value= !enabledState.value
                titel.value=label
            })
    }

         }

//Das hab ich gelernt: Man kann in den function parameter datentypen übergeben wenn sie ein mutableState haben
// und mit
// dasObjectZumÄndern.value="GeänderterText"
//ändern es ist dann immer noch das selbe objeckt es wird kein neues erzeugt oder so



