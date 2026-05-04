package com.happy.vonviereck

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun victoryScreen(  onNextButtonClicked: () -> Unit,
                    modifier: Modifier = Modifier){
    Box(contentAlignment = Alignment.Center){
        Text(text = "Du hast gewonnen")
    }
}