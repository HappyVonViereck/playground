package com.happy.vonviereck

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

class cheeseGerman: gridObjeckte() {
    @Composable
    fun createCheese() {
        val painter = painterResource(R.drawable.germancheese)
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

//platziere Käse
//KäseErreicht bool
//kann erreichtwerden bool
//käse platzieren irgndwo
//Zuschauer modus aktivieren