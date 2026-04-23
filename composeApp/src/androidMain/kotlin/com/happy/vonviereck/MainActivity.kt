package com.happy.vonviereck

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
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


import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

// ─── ViewModel ───────────────────────────────────────────────────────────────

class GameViewModel : ViewModel() {
    val allTiles = mutableListOf<Tile>()
    val maus = Maus()
    val germanCheese= cheeseGerman()
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
    Box(modifier = Modifier.fillMaxSize()){
        mainScreen()

    Box(modifier = Modifier.padding(top = 150.dp))
        {
        Column(
            modifier = Modifier
                .padding(top = 80.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            dropDownMenu()
            Row(horizontalArrangement = Arrangement.Start) {
                createSavebtn(vm.allTiles)
                changetilesBtn(vm.allTiles)
                knopf(vm.maus)
            }
            createTileSet(vm.allTiles, vm.maus)

        }


    }
        // 2. Maus ganz am Ende → liegt automatisch über allem
        vm.maus.createMaus()
        vm.maus.moveMouse(120, 1109)
        vm.germanCheese.createCheese()
}
}
// ─── Buttons ─────────────────────────────────────────────────────────────────

@Composable
fun createSavebtn(allTiles: MutableList<Tile>) {
    val context = LocalContext.current
    val loader = LaoderForTile()
    var savedLevels by remember { mutableStateOf(loader.getAllSavedLevels(context)) }

    // Dialog-State
    var showSaveDialog by remember { mutableStateOf(false) }
    var levelName by remember { mutableStateOf("") }

    Button(onClick = {
        Log.d("Btn", "es wurde auf speichern gedrückt ")
        showSaveDialog = true
    }) {
        Text("Speichern")  //TODO:Den speichern btn von der logik der anderen lvl btns trennen
    }

    // Speichern-Dialog
    if (showSaveDialog) {
        AlertDialog(onDismissRequest = {
            showSaveDialog = false
            levelName = ""
        }, title = { Text("Level benennen") }, text = {
            OutlinedTextField(
                value = levelName,
                onValueChange = { levelName = it },
                label = { Text("Level-Name") },
                singleLine = true,
                placeholder = { Text("z.B. level1") })
        }, confirmButton = {
            Button(
                onClick = {
                    if (levelName.isNotBlank()) {
                        loader.saveGrid(context, allTiles, levelName)
                        showSaveDialog = false
                        levelName = ""
                    }
                }) {
                Text("Speichern")
            }
        }, dismissButton = {
            OutlinedButton(onClick = {
                showSaveDialog = false
            }) {
                Text("Abbrechen")
            }
        })
    }
}

@Composable
fun changetilesBtn(allTiles: MutableList<Tile>){

    Button(onClick = {
        allTiles.forEach { tile->
            tile.isInEditMode=!tile.isInEditMode
        }
        Log.d("Btn", "es wurde in den Bearbeitungsmodus gewechelts ")
    })
    {
        Text("bearbeiten")
    }
}

@Composable
fun knopf(maus: Maus) {
    Log.d("cheese", "knopf: Käase soll gefudnen werden starten")
    var start = remember { mutableStateOf(false) }
    Button(onClick = {
        Log.d("cheese", "knopf wurde gedrückt")
        start.value = true
    }
    ){
        Text("Käse finden")
    }
    bewegenMaustest(start,maus)
}

@Composable
fun bewegenMaustest(start: MutableState<Boolean>,maus: Maus){
    if (start.value) {
        Log.d("cheese", "Käase soll gefudnen werden start ist true")
        LaunchedEffect(Unit) {
            maus.moveMouse(120, 1109)
            delay(2000L)
            maus.moveMouse(225, 1305)
            delay(2000L)
            maus.moveMouse(750, 1410)
            delay(2000L)
        }
    }
}
    //käse platzieren irgndwo
    //maus bewegen
    //Zuschauer modus aktivieren

// ─── TileSet ─────────────────────────────────────────────────────────────────

@Composable
fun createTileSet(allTiles: MutableList<Tile>, maus: Maus) {
        HorizontalGrid(rows = 10, columns = 10, allTiles = allTiles, maus)
}

// ─── Grid + Maus ─────────────────────────────────────────────────────────────

@Composable
fun HorizontalGrid(rows: Int, columns: Int, allTiles: MutableList<Tile>, maus: Maus) {

        Column(
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
                            if (col == 0 || col == columns - 1 || row == 0 || row == rows - 1) {
                                LaunchedEffect(Unit) { tileBlock.toggleTile() }
                            }
                            tileBlock.createATile(maus, allTiles)
                        }
                    }
                }
            }
        }
    }



@Composable
fun dropDownMenu(vm: GameViewModel = viewModel()) {
    var expandBool = remember { mutableStateOf(false) }
    var selectedDataGrid = remember { mutableStateOf("") }
    var textfiledSize by remember { mutableStateOf(Size.Zero) }

    //ToogleSwitch
    val iconToggle = if (expandBool.value) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    Column(modifier = Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = selectedDataGrid.value,
            onValueChange = { selectedDataGrid.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textfiledSize = coordinates.size.toSize()
                },
            label = { Text(text = "Level auswählen") },
            trailingIcon = {
                Icon(iconToggle, "", Modifier.clickable { expandBool.value = !expandBool.value })
            })

        DropdownMenu(
            expanded = expandBool.value,
            onDismissRequest = { expandBool.value = false },
            modifier = Modifier.width(with(LocalDensity.current) { textfiledSize.width.toDp() })
        ) {
            gridLadenButtons(vm.allTiles, enabledState = expandBool, selectedDataGrid)
        }
    }
}


@Composable
fun gridLadenButtons(
    allTiles: MutableList<Tile>, enabledState: MutableState<Boolean>, titel: MutableState<String>
) {

//Lvl loader mehtoden abschnitt
    val context = LocalContext.current
    val loader = LaoderForTile()
    var savedLevels by remember { mutableStateOf(loader.getAllSavedLevels(context)) }

    savedLevels.forEach { label ->
        Log.d(
            "Btn",
            "createBtns: hier ist die liste gridList:${savedLevels.size} es wurde grade $label hinuzugefügt"
        )
        DropdownMenuItem({ Text(text = label) }, onClick = {
            loader.loadGrid(context, label, allTiles)
            enabledState.value = !enabledState.value
            titel.value = label
        })
    }

}
//Ziel: die laden buttons mit den dropdown verbinden->fertig
//Das hab ich gelernt: Man kann in den function parameter datentypen übergeben wenn sie ein mutableState haben
// und mit
// dasObjectZumÄndern.value="GeänderterText"
//ändern es ist dann immer noch das selbe objeckt es wird kein neues erzeugt oder so



