package com.example.zenglow

import MoodBoostScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.DeviceState
import com.example.zenglow.states.GroupState
import com.example.zenglow.views.HomeScreen
import com.example.zenglow.views.NewDeviceScreen
import com.example.zenglow.views.SettingsScreen

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
            route = "${Screen.NewDevice.route}/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.IntType})
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getInt("groupId")

            NewDeviceScreen(navController = navController, state = deviceState, onEvent = onDeviceEvent)
        }
        composable(
            route = Screen.Settings.route
        ) {
            SettingsScreen(navController = navController)
        }
        composable(
            route = Screen.MoodBoost.route
        ) {
            MoodBoostScreen(navController = navController)
        }
    }
}