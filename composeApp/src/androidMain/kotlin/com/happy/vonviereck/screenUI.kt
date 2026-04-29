package com.happy.vonviereck


import android.util.Log
import androidx.compose.animation.VectorConverter
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun mainScreen() {

    var pauseDauer by remember { mutableStateOf((1000L..20000L).random()) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val density = LocalDensity.current

// Bool-Trigger für Scale und Rotation
    var effekteAktiv by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            effekteAktiv = true
            delay(1000)
            effekteAktiv = false
            delay(pauseDauer)
            // Nach der Pause neue Zeit würfeln
            pauseDauer = (1000L..20000L).random()
            Log.d("Debug", "Neue Pausendauer: ${pauseDauer}ms (${pauseDauer / 1000}s)")
        }
    }

    val animX = rememberInfiniteTransition(label = "scroll")
    val offsetX by animX.animateFloat(
        initialValue = with(density) { -screenWidth.toPx() },
        targetValue = with(density) { screenWidth.toPx() },
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "textScroll"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "animation")
    val schrittDauer = 1000

    val rotationScale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "rotation"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )

    val farben = listOf(
        Color(0x404A051C),
        Color(0xFF06AED5),
        Color(0xFF52FFB8),
        Color(0x70FF7F11),
        Color(0xFFEE4B6A),
    )

    val farbe1 by infiniteTransition.animateColor(
        initialValue = farben.first(),
        targetValue = farben.last(),
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = farben.size * schrittDauer
                farben.forEachIndexed { index, farbe ->
                    farbe at (index * schrittDauer) using LinearEasing
                }
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "farbe1"
    )

    val farbe2 by infiniteTransition.animateColor(
        initialValue = farben[farben.size / 2],
        targetValue = farben.first(),
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = farben.size * schrittDauer
                val verschoben = farben.drop(farben.size / 2) + farben.take(farben.size / 2)
                verschoben.forEachIndexed { index, farbe ->
                    farbe at (index * schrittDauer) using LinearEasing
                }
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "farbe2"
    )

    val brush = Brush.linearGradient(
        colors = listOf(farbe1, farbe2, farbe1)
    )

    Image(
        painter = painterResource(id = R.drawable.homescrreeeam),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Text(
        modifier = Modifier
            .offset { IntOffset(offsetX.toInt(), 0) }
            .graphicsLayer(
                scaleX = if (effekteAktiv) scale else 1f,
                scaleY = if (effekteAktiv) scale else 1f,
                rotationZ = if (effekteAktiv) rotationScale else 0f,
                transformOrigin = TransformOrigin.Center
            )
            .padding(50.dp),
        style = TextStyle(
            brush = brush,
            textMotion = TextMotion.Animated,
            fontSize = 30.sp
        ),
        text = stringResource(R.string.app_name)
    )
}

