package com.happy.vonviereck

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

import learningareproject.composeapp.generated.resources.Res
import learningareproject.composeapp.generated.resources.compose_multiplatform
import kotlin.math.roundToInt

@Composable
@Preview
fun App() {

    MaterialTheme() {
        mainScreen()
        counterapp()
        createTileSet()
        mausAniamtion()
    }
}
@Composable
fun counterapp() {

    var counter by remember { mutableStateOf(0) }
    //1.var wird verwedent um trackingparameter die sich verändern können/sollen zu erstellen
    //2.remember ist dafür da das sich der wert erhöhen kann und der mutableStateOf() bestimmt den Anfangparameter/Value/wert

    Column(//Das ist ein layout wie ein canvas in unity

        modifier = Modifier
            .fillMaxWidth()//grössesetzen
            .padding(12.dp)

    ) {
        Text("Counter:$counter")//Text ist ein Label mit String ,wird optisch dargestellt
        Button(
            onClick = { counter++ }
        )
        {
            Text("Clickhere!")
        }

        Button(
            onClick = { counter += 2 }

        ) {
            Text("Clickherefor2")

        }


        Button(
            onClick = { counter = 0 }
        ) {

            Text(text = "Reset the counter here")
        }
    }
}

@Composable
fun mausAniamtion(){
    val animatedOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    Box(
        Modifier.fillMaxSize().pointerInput(Unit) {

            coroutineScope {
                while (true) {
                    val offset = awaitPointerEventScope { awaitFirstDown().position }
                    // Launch a new coroutine for animation so the touch detection thread is not
                    // blocked.
                    launch {
                        // Animates to the pressed position, with the given animation spec.
                        animatedOffset.animateTo(
                            offset,
                            animationSpec = spring(stiffness = Spring.StiffnessLow),
                        )
                    }
                }
            }
        }
    ) {
        Box(
            Modifier.offset {
                // Use the animated offset as the offset of the Box.
                IntOffset(
                    animatedOffset.value.x.roundToInt(),
                    animatedOffset.value.y.roundToInt(),
                )
            }
                .size(80.dp)
        ){
        val p = painterResource(R.drawable.maus)
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = p,
            contentDescription = null,
        )
        }
    }



}