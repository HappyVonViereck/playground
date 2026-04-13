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

val sizeForImg = 60.dp


@Composable
fun greetingWithImg(message: String, from: String, modifier: Modifier) {

    val maus = painterResource(R.drawable.maus)
    val imgGerman = painterResource(R.drawable.germancheese)
    val tile = painterResource(R.drawable.tileboden)

    Image(
        modifier = Modifier
            .size(sizeForImg + 10.dp, sizeForImg),
        painter = maus,
        contentDescription = null
    )
    Image(
        modifier = Modifier
            .size(sizeForImg, sizeForImg),
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
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
        ){
        HorizontalGrid(6, 6)
    }



}




@Composable
fun HorizontalGrid(rows: Int, columns: Int) {
    val items = (100..200).toList()
    val maxItems = rows * columns

    Column {
        repeat(rows) { row ->
            Row {
                repeat(columns) { col ->

                    val index = row * columns + col

                    if (index < items.size && index < maxItems) {

                        val x = col   // Spalte
                        val y = row   // Zeile
                        val tileBlock = remember { Tile() }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            shape = CutCornerShape(0.dp),
                            modifier = Modifier

                                .size(60.dp)
                        ) {


                            tileBlock.refrehsCords(x, y)

                            println("x: ${tileBlock.xCord}, y: ${tileBlock.yCord}")
                            println("row: ${x}, columm: ${y}")
                            if (tileBlock.xCord==rows-1||tileBlock.yCord==columns-1 ||tileBlock.xCord==0||tileBlock.yCord==0){

                                tileBlock.convertTileToHinderniss()

                            }
                            tileBlock.createATile()
                        }


                    }
                }
            }
        }
    }
}


