package com.happy.vonviereck

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class DifferentScreens {
    Start,
    Maze
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MouseAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        /*colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            scrolledContainerColor = Color.Unspecified,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        ),*/
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun StartApp(
    navController: NavHostController = rememberNavController()
) {

    Scaffold(
        topBar = {
            MouseAppBar(
                canNavigateBack = false,
                navigateUp = { /* TODO: implement back navigation */ }
            )
        }
    ) { innerPadding ->
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
           /*  composable(route = DifferentScreens.Pickup.name) {
                SelectOptionScreen( // potential third screen
                    onNextButtonClicked = { navController.navigate(DifferentScreens.Summary.name) },
                    onBackButtonClicked = {
                        returnToStart(navController)
                    },
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