package com.example.zenglow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.states.DeviceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDeviceScreen(
    navController: NavController,
    state: DeviceState,
    onEvent: (DeviceEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add a new device")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        NewDeviceScrollContent(innerPadding, navController, state, onEvent)
    }

}

@Composable
fun NewDeviceScrollContent(
    innerPadding: PaddingValues,
    navController: NavController,
    state: DeviceState,
    onEvent: (DeviceEvent) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xEC, 0xEC, 0xEC)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "New Device",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = MaterialTheme.typography.displayLarge.fontSize
        )

        FloatingActionButton(onClick = {
            onEvent(DeviceEvent.ShowDialog)
        }) {
            Row{
                Icon(Icons.Filled.Add, "Add New Device")
                Text(text = "Add new Device")
            }
        }
    }
}
