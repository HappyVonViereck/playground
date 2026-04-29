package com.happy.vonviereck

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

class Maus : gridObjeckte(){
    @Composable
    fun createMaus() {
        val painter = painterResource(R.drawable.maus)
        val animatedX = remember { Animatable(xCordPos.toFloat()) }
        val animatedY = remember { Animatable(yCordPos.toFloat()) }

        LaunchedEffect(xCordPos, yCordPos) {
            animatedX.animateTo(xCordPos.toFloat())
            animatedY.animateTo(yCordPos.toFloat())
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .offset { IntOffset(animatedX.value.toInt(), animatedY.value.toInt()) }
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentDescription = null,
            )
        }
    }

}

//Alter Code hänge aber emotional noch dran, weil es meine erste idee war aber noch nicht optimiert genug :c wähhh
//    fun moveMouse(targetX: Int, targetY: Int, allTiles: List<Tile> = emptyList(), tileSize: Int = 40) {
//        val targetTile = allTiles.find { tile ->
//            targetX >= tile.xCordPos &&
//                    targetX <= tile.xCordPos + tileSize &&
//                    targetY >= tile.yCordPos &&
//                    targetY <= tile.yCordPos + tileSize
//        }
//        xCordPos = targetX
//        yCordPos = targetY
//        currentTile = targetTile
//        Log.d("Maus", "Bewegt auf Tile: (${currentTile?.xCord}, ${currentTile?.yCord})")
//        Log.d("Maus", "Bewegt auf Tile:GlobalePosition: (${currentTile?.xCordPos}, ${currentTile?.yCordPos})")
//    }
