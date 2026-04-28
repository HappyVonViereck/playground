package com.happy.vonviereck

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun mainScreen() {
    val infiniteTransition = rememberInfiniteTransition()

    val rotationScale by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse)
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1.3f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse)
    )

    val brush = linearGradient(
        colors = listOf(
            Color(0x404A051C),
            Color(0xFF06AED5),
            Color(0xFF52FFB8),
            Color(0x70FF7F11),
            Color(0xFFEE4B6A),
        )
    )
    Image(
        painter = painterResource(id = R.drawable.homescrreeeam),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop // or ContentScale.Fit
    )

    Text(
        modifier = Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotationScale,
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
