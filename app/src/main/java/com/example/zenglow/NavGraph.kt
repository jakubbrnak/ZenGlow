package com.example.zenglow

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    state: GroupState,
    onEvent: (GroupEvent) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController = navController, state = state, onEvent = onEvent)
        }
        composable(
            route = Screen.NewDevice.route
        ) {
            NewDeviceScreen(navController = navController)
        }
        composable(
            route = Screen.Settings.route
        ) {
            SettingsScreen(navController = navController)
        }
    }
}