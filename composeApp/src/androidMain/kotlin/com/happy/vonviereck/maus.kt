package com.happy.vonviereck

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class Maus {
    var xCordPos by mutableStateOf(0)
    var yCordPos by mutableStateOf(0)

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
                .onGloballyPositioned {
                    it.positionInRoot()

                }) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentDescription = null,
            )
        }
    }

    fun moveMouse(targetX: Int, targetY: Int) {
        xCordPos = targetX
        yCordPos = targetY
    }
}
