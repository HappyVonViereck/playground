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
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.remember


import androidx.compose.ui.draw.scale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()

}

val sizeForImg = 40.dp

@Composable
fun greetingWithImg(message: String, from: String, modifier: Modifier) {

    val maus = painterResource(R.drawable.maus)
    val imgGerman = painterResource(R.drawable.germancheese)
    val tile = painterResource(R.drawable.tileboden)

    Image(
        modifier = Modifier.size(sizeForImg + 10.dp, sizeForImg),
        painter = maus,
        contentDescription = null
    )
    Image(
        modifier = Modifier.size(sizeForImg, sizeForImg),
        painter = imgGerman,
        contentDescription = "das ist sehr deutsch"
    )
//    Image(
//        modifier = Modifier
//            .size(sizeForImg, sizeForImg),//Größe kann hier geändert werden
//        painter = tile,
//        contentDescription = null
//    )

}


@Composable
fun createTileSet() {

    var t = Text("Halllooo")
    Button(onClick = {}) { }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {

        val sizeForGrid = 10
        HorizontalGrid(sizeForGrid, sizeForGrid)

    }


}


//erstellt das grid und die maus
@Composable
fun HorizontalGrid(rows: Int, columns: Int) {
    val items = (100..200).toList()
    val maxItems = rows * columns
    val maus = remember { Maus() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center), // Column zentrieren
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            repeat(rows) { row ->
                Row {
                    repeat(columns) { col ->
                        val index = row * columns + col

                        if (index < items.size && index < maxItems) {
                            val x = col
                            val y = row
                            val tileBlock = remember { Tile() }

                            tileBlock.xCord = x
                            tileBlock.yCord = y

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                shape = CutCornerShape(0.dp),
                                modifier = Modifier.size(sizeForImg)
                            ) {
                                if (tileBlock.xCord == rows - 1 || tileBlock.yCord == columns - 1 || tileBlock.xCord == 0 || tileBlock.yCord == 0) {
                                    tileBlock.toggleTile()
                                }
//                                if(tileBlock.xCord==3&&tileBlock.yCord==3){
//                                    tileBlock.convertTileToHinderniss()
//                                }
                                tileBlock.createATile(maus)
                            }
                        }
                    }
                }
            }
        }

        // Maus als letztes im Box — liegt über allem
        maus.createMaus()
        maus.moveMouse(435, 1095)
    }
}


