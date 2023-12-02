package com.example.zenglow

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.DeviceState
import com.example.zenglow.states.GroupState

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    groupState: GroupState,
    deviceState: DeviceState,
    onGroupEvent: (GroupEvent) -> Unit,
    onDeviceEvent: (DeviceEvent) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController = navController, state = groupState, onEvent = onGroupEvent)
        }
        composable(
            route = Screen.NewDevice.route
        ) {
            NewDeviceScreen(navController = navController, state = deviceState, onEvent = onDeviceEvent)
        }
        composable(
            route = Screen.Settings.route
        ) {
            SettingsScreen(navController = navController)
        }
    }
}