package com.happy.vonviereck

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

class Tile {
    var xCord =0
    var yCord = 0
var darfGehen=true

    var currentImageRes by mutableStateOf(R.drawable.tileboden23542352)
    var modifier= Modifier
    @Composable
    fun createATile() {
        val painter = painterResource(currentImageRes)

        Image(
            modifier = Modifier.size(sizeForImg),
            painter = painter,
            contentDescription = null
        )
    }

    fun convertTileToHinderniss() {
        currentImageRes = R.drawable.hindernisse
        darfGehen=false

    }

    @Composable
    fun refrehsCords(x:Int, y:Int){
        xCord=x
        yCord=y

    }
}