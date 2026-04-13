package com.happy.vonviereck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun mainScreen(
    modifier: Modifier=Modifier.fillMaxSize(),

){

    Column(
        modifier = modifier
            .background(Color.Green),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "1.Abschnitt",
            modifier= Modifier
            .padding(0.dp,30.dp)
        )
        Text(text = "2.Abschnitt")

    }

}
