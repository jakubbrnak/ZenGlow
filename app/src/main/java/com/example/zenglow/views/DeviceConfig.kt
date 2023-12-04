package com.example.zenglow.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.zenglow.AddDeviceDialog
import com.example.zenglow.R
import com.example.zenglow.Screen
import com.example.zenglow.data.entities.Device
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.states.DeviceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceConfigScreen(
    navController: NavController,
    state: DeviceState,
    onEvent: (DeviceEvent) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var deviceId = -1
    navBackStackEntry?.arguments?.getInt("deviceId")?.let {selectedGroupId ->
        deviceId = selectedGroupId
    } ?: run {
        deviceId = -1
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add new device to group")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        DeviceConfigScrollContent(innerPadding, navController, state, deviceId, onEvent)
    }
}

@Composable
fun DeviceConfigScrollContent(
    innerPadding: PaddingValues,
    navController: NavController,
    state: DeviceState,
    deviceId: Int,
    onEvent: (DeviceEvent) -> Unit) {

    val deviceById: Device? = state.devices.find { it.deviceId == deviceId }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xEC, 0xEC, 0xEC)),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        if (deviceById != null) {
            Text(
                text = "${deviceById.displayName}",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }

    }
}
