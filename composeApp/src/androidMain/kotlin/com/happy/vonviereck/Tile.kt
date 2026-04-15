package com.happy.vonviereck

import android.media.Image
import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class Tile {
    var xCord = 0 //für grid
    var yCord = 0  //für grid


    var xCordPos = 0 //BildschirmPosition in pixelGenau
    var yCordPos = 0 //BildschirmPosition in pixelGenau

    var currentImageRes by mutableStateOf(R.drawable.tileboden23542352)  //Der originalTile kann aber zu hinderniss werden -> mutableStateOF wird dann benötigt weil man es ändert
    var darfGehen = true //Bool zum überprüfen ob man das Tile betreten darf
    var isInEditMode = false


    //Kurze Zusammenfassung: Es wird ein Img von den TIle erstellt mit nen Button drinne der aber unsichtbar gemacht wird(funktioniert)
    // trz nur dann sieht es so aus als wäre das Tile der Knopf)
    //Dann bekommt dieser Button eine onClick mehtode von der Maus die wir übergeben damit es imemr die selbe maus ist
    // und nicht jedes Tile seine eigene erzeugt

    @Composable
    fun createATile(maus: Maus) {
        val painter = painterResource(currentImageRes)
        Box(
            modifier = Modifier //Die Box wird benötigt damit man mehrer ui elemente zusammenfügen kann
                .fillMaxSize()
                .onGloballyPositioned {
                    val pos = it.positionInRoot()
                    xCordPos = pos.x.toInt()
                    yCordPos = pos.y.toInt()
                    Log.d("Tile", "POSITION gesetzt: xCord=$xCordPos, yCord=$yCordPos")
                }) {
            Image(
                //1.UIelement das Tile
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentDescription = null,
            )
            Button( //2.UIElement aber unsichtbar
                onClick = {
                    if (darfGehen == false) { //Kontrolle ob die Maus sich bewegen darf
                        Log.d(
                            "Maus",
                            "Dieses Feld(xCord=$xCordPos, yCord=$yCordPos)  darf nicht betretren werden! Bewegung abgebrochem"
                        )
                    } else {
                        maus.moveMouse(xCordPos, yCordPos)
                        Log.d("Tile", "Dies Tile darf betreten werden: $darfGehen")
                        Log.d("Tile", "POSITION: xCord=$xCordPos, yCord=$yCordPos")

                    }

                    if (isInEditMode) {
                        toggleTile()
                    }
                }, modifier = Modifier //Hier wird der button unsichtbar gemacht
                    .alpha(0f)
                    .align(Alignment.Center)
            ) {}
        }
    }

    //Macht das Tile zu einen Hinderniss also die Mauer des Labyrinths
    fun toggleTile() {
        if (darfGehen) {
            currentImageRes = R.drawable.hindernisse
            darfGehen = false
            Log.d("Tile", "Tile x=$xCord, y=$yCord → Mauer")
        } else {
            currentImageRes = R.drawable.tileboden23542352
            darfGehen = true
            Log.d("Tile", "Tile x=$xCord, y=$yCord → Boden")
        }
    }
}