package com.happy.vonviereck

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import learningareproject.composeapp.generated.resources.Res
import learningareproject.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {

    MaterialTheme() {

        mainScreen()
        counterapp()
        createTileSet()
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            greetingWithImg("Hallo das ist eine Maus", "from Haps", modifier = Modifier)
        }
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
