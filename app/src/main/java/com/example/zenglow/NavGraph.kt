package com.example.zenglow

import MoodBoostScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.zenglow.events.AppStateEvent
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.AppStateState
import com.example.zenglow.states.DeviceState
import com.example.zenglow.states.GroupState
import com.example.zenglow.views.DeviceConfigScreen
import com.example.zenglow.views.EditMoodScreen
import com.example.zenglow.views.HomeScreen
import com.example.zenglow.views.NewDeviceScreen
import com.example.zenglow.views.SettingsScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    groupState: GroupState,
    deviceState: DeviceState,
    appStateState: AppStateState,
    onGroupEvent: (GroupEvent) -> Unit,
    onDeviceEvent: (DeviceEvent) -> Unit,
    onAppStateEvent: (AppStateEvent) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController = navController, groupState = groupState, deviceState = deviceState, appStateState = appStateState, onGroupEvent = onGroupEvent, onDeviceEvent = onDeviceEvent, onAppStateEvent = onAppStateEvent)
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
            SettingsScreen(navController = navController, appState = appStateState, onEvent = onAppStateEvent)
        }
        composable(
            route = Screen.EditMood.route
        ) {
            EditMoodScreen(navController = navController, appStateState = appStateState,onAppStateEvent = onAppStateEvent)
        }

        composable(
            route = Screen.MoodBoost.route
        ) {
            MoodBoostScreen(navController = navController, appStateState = appStateState, onAppStateEvent = onAppStateEvent)
        }

        composable(
            route = "${Screen.DeviceConfig.route}/{deviceId}",
            arguments = listOf(navArgument("deviceId") { type = NavType.IntType})
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getInt("deviceId")

            DeviceConfigScreen(navController = navController, state = deviceState, onEvent = onDeviceEvent)
        }
    }
}