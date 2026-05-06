package com.happy.vonviereck

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class DifferentScreens {
    Start,
    Maze
}

@Composable
fun StartApp(
    navController: NavHostController = rememberNavController()
) {

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DifferentScreens.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = DifferentScreens.Start.name) {
                MainMenuScreen(
                    onNextButtonClicked = {
                        navController.navigate(DifferentScreens.Maze.name)
                    },
                    modifier = Modifier.fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = DifferentScreens.Maze.name) {
                App()
            }

             /*composable(route = DifferentScreens.Victory.name) {
                VictoryScreen( // potential third screen
                    // a condition, which on win would trigger
                   onNextButtonClicked = { navController.navigate(DifferentScreens.Maze.name) },
                    *//*onBackButtonClicked = {
                        returnToStart(navController)
                    },*//*
                    modifier = Modifier.fillMaxHeight()
                )
            }*/
        }
    }
}

private fun returnToStart(
    navController: NavHostController
) {
    navController.popBackStack(DifferentScreens.Start.name, false)
}