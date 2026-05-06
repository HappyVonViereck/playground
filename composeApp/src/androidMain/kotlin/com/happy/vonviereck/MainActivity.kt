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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import kotlinx.coroutines.delay
import kotlin.math.abs

// ─── ViewModel ───────────────────────────────────────────────────────────────
//Ein viewmodel ist so zu sagen eine box, wo man varibalen abstellen kann
// und man dann die box holt kann man drauf zugreifen mit ver mehtoden,
//Das heißt man kann mit mehreren methoden die varibalen /werte verändern man muss nur das viewmodel
//an die methode weitergeben
class GameViewModel : ViewModel() {
    val allTiles = mutableListOf<Tile>()
    val aroundOptions = mutableListOf<Tile>()
    var currentPath = mutableStateOf<List<Tile>>(emptyList())
    val germanCheese = cheeseGerman()
    val maus = Maus()
    val tileSize = 40.dp
}

// ─── Activity ────────────────────────────────────────────────────────────────

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            StartApp()
        }
    }
}

// ─── App Einstiegspunkt ──────────────────────────────────────────────────────

@Composable
fun App(vm: GameViewModel = viewModel()) {
    LaunchedEffect(vm.allTiles.size) {
        if(vm.allTiles.size>=100) {
            vm.maus.moveTo(7, 5, vm.allTiles)
            vm.allTiles.filter { it.xCord == 7 && it.yCord == 5}[0].darfGehen2 = 2
            vm.germanCheese.placeCheeseOnFreeTileRandom(vm)
            Log.d("Debug", "Startpositionen gesetzt")
        }
    }

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
                    knopf(vm)
                    // käsePlatzierenTestbtn(vm)
                }
                createTileSet(vm)
            }
        }
        // hier alle anderen elemente die globale position brauchen
        vm.maus.createMaus()
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

    Button(
        onClick =
            {
                Log.d("Btn", "es wurde auf speichern gedrückt ")
                showSaveDialog = true  //Trigger für den SpeicherAlert(DialogBox)
            })
    { Text("Speichern") }

    // Speichern-Dialog
    if (showSaveDialog) { //wenn der trigger von knopf auf true ist wird der alert ausgelöst
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
                    placeholder = { Text("z.B. level1") })
            },
            confirmButton = { //Der knopf zum Bestätigen wurde gedrückt
                Button(
                    onClick = {
                        if (levelName.isNotBlank()) {
                            loader.saveGrid(context, allTiles, levelName)
                            showSaveDialog = false
                            levelName = "" //leert die textbox damit man beim nächten level benennen
                            // eine freie Textbox hat
                        }
                    }) { Text("Speichern") }
            },
            dismissButton =
                {
                    OutlinedButton(onClick = { showSaveDialog = false })//Der Vorgang wird abgerochen
                    { Text("Abbrechen") }
                })
    }
}

@Composable
fun gridLadenButtons(allTiles: MutableList<Tile>, enabledState: MutableState<Boolean>, titel: MutableState<String>)
{
    val context = LocalContext.current
    val loader = LaoderForTile()
    var savedLevels by remember { mutableStateOf(loader.getAllSavedLevels(context)) }

    savedLevels.forEach { label ->
        DropdownMenuItem(
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = label)

                    // Delete-Button
                    IconButton(onClick = {
                        loader.deleteGrid(context, label)
                        savedLevels = loader.getAllSavedLevels(context) // Liste aktualisieren
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Löschen",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            onClick = {
                loader.loadGrid(context, label, allTiles)
                enabledState.value = false
                titel.value = label
            }
        )
    }
}

@Composable
fun changetilesBtn(allTiles: MutableList<Tile>){

    Button(onClick = {
        allTiles.forEach { tile->
            tile.isInEditMode=!tile.isInEditMode
        }
        Log.d("Btn", "es wurde in den Bearbeitungsmodus gewechselt ")
    })
    {
        Text("bearbeiten")
    }
}

@Composable
fun knopf(vm: GameViewModel) {
    val start = remember { mutableStateOf(false) }
    Log.d("cheese", "knopf: Käse soll gefunden werden starten")
    Button(onClick = {
        // bewegenMaustest(vm)
        start.value = true
        Log.d("cheese", "knopf wurde gedrückt")
    }
    ){
        Text("Käse finden")
    }
    bewegenMaustest(start, vm)

  /*  if (start.value)
    {
        // loop this until mouse hits cheese tile
        do{     bewegenMaustest(start, vm)
            goAPath(vm, vm.currentPath.value)
        }
        while (vm.tileWithMouse != vm.tileWithCheese)
    }
*/
    goAPath(vm, vm.currentPath.value)
}

fun bewegenMaustest(start: MutableState<Boolean>, vm: GameViewModel) { // need a loop somewhere to repeat it until the cheese has been reached
    if (start.value) {
        val tileWithCheese = vm.allTiles.filter { it.darfGehen2 == 3 }[0]
        val tileWithMouse = vm.allTiles.filter { it.darfGehen2 == 2 }[0]
        val xDifference = abs(tileWithMouse.xCord - tileWithCheese.xCord)
        val yDifference = abs(tileWithMouse.yCord - tileWithCheese.yCord)
        val xIsCloser = xDifference < yDifference // defines whether rat will close in on the cheese on the x- or the y-axis first
        var chosenTile = vm.allTiles.filter { it.darfGehen2 == 3 }[0]

        checkAround(vm)
        // define a 1-step path here, which uses a tile in aroundOptions and brings the rat the closest to the cheese
        if (xIsCloser && xDifference != 0 || yDifference == 0) {
            if (tileWithMouse.xCord > tileWithCheese.xCord) { // move left
                if (vm.aroundOptions[2].darfGehen2 != 1) {
                    chosenTile = vm.aroundOptions[2]
                }
            } else { // move right
                if (vm.aroundOptions[3].darfGehen2 != 1) {
                    chosenTile = vm.aroundOptions[3]
                }
            }
        } else {
            if (tileWithMouse.yCord > tileWithCheese.yCord) { // move down
                if (vm.aroundOptions[0].darfGehen2 != 1) {
                    chosenTile = vm.aroundOptions[0]
                }
            } else { // move up
                if (vm.aroundOptions[1].darfGehen2 != 1) {
                    chosenTile = vm.aroundOptions[1]
                }
            }
        }
        tileWithMouse.darfGehen2 = 0 // sets prior tile with mouse to open
        chosenTile.darfGehen2 = 2 // sets tile mouse moved to, to mouse
        vm.currentPath.value = createAPath(
            tileWithMouse,
            chosenTile,
            vm
        ) // moves mouse to adjacent tile that brings it closest to the cheese
        start.value = false
    }
}

fun checkAround(vm: GameViewModel) { // find tile Mouse is standing on and inspect neighboring tiles
    val tileWithMouse = vm.allTiles.filter { it.darfGehen2 == 2 }[0]
    val currentX = tileWithMouse.xCord
    val currentY = tileWithMouse.yCord
    val aboveMouse = vm.allTiles.filter { it.xCord == currentX && it.yCord == currentY - 1 }[0]
    val belowMouse = vm.allTiles.filter { it.xCord == currentX && it.yCord == currentY + 1 }[0]
    val leftMouse = vm.allTiles.filter { it.xCord == currentX - 1 && it.yCord == currentY }[0]
    val rightMouse = vm.allTiles.filter { it.xCord == currentX + 1 && it.yCord == currentY }[0]

    vm.aroundOptions.clear()
    vm.aroundOptions.add(aboveMouse)
    vm.aroundOptions.add(belowMouse)
    vm.aroundOptions.add(leftMouse)
    vm.aroundOptions.add(rightMouse)

    /*for (option in vm.aroundOptions) {
        if (option.darfGehen2 != 0)
        {
            vm.aroundOptions.remove(option) // removes surrounding tiles that aren't free
        }
    }*/
}

/*@Composable
fun käsePlatzierenTestbtn(vm: GameViewModel){
    Button(onClick = { vm.germanCheese.placeCheeseOnFreeTileRandom(vm)}){
        Text("käse los")
    }
}*/

// ─── TileSet ─────────────────────────────────────────────────────────────────

@Composable
fun createTileSet(vm: GameViewModel) {
    HorizontalGrid( 10, 10,  vm.allTiles, vm.maus,vm)
}

// ─── Grid ─────────────────────────────────────────────────────────────

@Composable
fun HorizontalGrid(rows: Int, columns: Int, allTiles: MutableList<Tile>, maus: Maus,vm: GameViewModel) {
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

                            // Rand-Tiles als Mauer setzen
                            if (col == 0 || col == columns - 1 || row == 0 || row == rows - 1) {
                                tile.toggleTile()
                            }
                            allTiles.add(tile)
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = CutCornerShape(0.dp),
                        modifier = Modifier.size(vm.tileSize.value.dp)
                    ) {
                        tileBlock.createATile(maus, allTiles)
                    }
                }
            }
        }
    }
}

//───────DropDownMenu ─────────────────────────────────────────────────────────────

@Composable
fun dropDownMenu(vm: GameViewModel = viewModel()) {
    val expandBool = remember { mutableStateOf(false) }
    val selectedDataGrid = remember { mutableStateOf("") }
    var textfiledSize by remember { mutableStateOf(Size.Zero) }

    //ToggleSwitch
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

//───────pathFinding ─────────────────────────────────────────────────────────────

fun createAPath(tileA: Tile?, tileB: Tile?, vm: GameViewModel): List<Tile> {

    if (tileA == null || tileB == null) {
        return emptyList()
    }

    val path = mutableListOf<Tile>()

    // Schritt 1: Erst horizontal von A.x bis B.x (auf A.y bleiben)
    val xRange = if (tileA.xCord <= tileB.xCord) {
        tileA.xCord..tileB.xCord
    } else {
        tileA.xCord downTo tileB.xCord
    }

    for (x in xRange) {
        val tile = vm.allTiles.find { it.xCord == x && it.yCord == tileA.yCord }
        if (tile != null) {
            path.add(tile)
        }
    }

    // Schritt 2: Dann vertikal von A.y bis B.y (auf B.x bleiben)
    val yRange = if (tileA.yCord <= tileB.yCord) {
        (tileA.yCord + 1)..tileB.yCord   // +1 damit Ecke nicht doppelt
    } else {
        (tileA.yCord - 1) downTo tileB.yCord
    }

    for (y in yRange) {
        val tile = vm.allTiles.find { it.xCord == tileB.xCord && it.yCord == y }
        if (tile != null) path.add(tile)
    }

    path.forEach { t->
        Log.d("path","path besteht aus Tile(${t.xCord},${t.yCord})")
    }

    return path
}
@Composable
fun goAPath(vm: GameViewModel,l:List<Tile>){
    LaunchedEffect(l) {
        l.forEach { tile ->
            vm.maus.moveTo(tile.xCord, tile.yCord, vm.allTiles)
            delay(500L)
        }
    }
}

//Ziel: die laden buttons mit den dropdown verbinden->fertig
//Das hab ich gelernt: Man kann in den function parameter datentypen übergeben wenn sie ein mutableState haben
// und mit
// dasObjectZumÄndern.value="GeänderterText"
//ändern es ist dann immer noch das selbe objekt es wird kein neues erzeugt oder so



