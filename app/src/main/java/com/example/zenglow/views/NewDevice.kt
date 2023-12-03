package com.example.zenglow.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.zenglow.AddDeviceDialog
import com.example.zenglow.Screen
import com.example.zenglow.data.entities.Device
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.states.DeviceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDeviceScreen(
    navController: NavController,
    state: DeviceState,
    onEvent: (DeviceEvent) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var groupId = -1
    navBackStackEntry?.arguments?.getInt("groupId")?.let {selectedGroupId ->
        groupId = selectedGroupId
    } ?: run {
       groupId = -1
    }
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
        NewDeviceScrollContent(innerPadding, navController, state, groupId, onEvent)
    }

}

@Composable
fun NewDeviceScrollContent(
    innerPadding: PaddingValues,
    navController: NavController,
    state: DeviceState,
    groupId: Int,
    onEvent: (DeviceEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .background(Color(0xEC, 0xEC, 0xEC)),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(
            text = "New Device",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = MaterialTheme.typography.displayLarge.fontSize
        )
        // Display list of devices
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(state.freeDevices.size) { device->
                DeviceItem(
                    modifier = Modifier,
                    device = state.freeDevices[device],
                    navController = navController,
                    groupId = groupId,
                    onEvent = onEvent
                )
            }
        }
        FloatingActionButton(onClick = {
            onEvent(DeviceEvent.ShowDialog)
        }) {
            Row{
                Icon(Icons.Filled.Add, "Add New Device")
                Text(text = "Add device manually")
            }
        }
        if(state.isAddingDevice) {
            AddDeviceDialog(state = state, onEvent = onEvent)
        }
    }
}

@Composable
fun DeviceItem(
    modifier: Modifier,
    device: Device,
    navController: NavController,
    groupId: Int,
    onEvent: (DeviceEvent) -> Unit
) {
    Card(
        modifier
            .padding(10.dp)
            .wrapContentSize()
            .clickable {
                //Add groupId to device, then navigate back to home screen
                val updatedDevice = device.copy(groupId = groupId)
                onEvent(DeviceEvent.UpdateDevice(updatedDevice))
                navController.navigate(Screen.Home.route)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(Icons.Outlined.List, "Add New Device To Group")
            Text(text = device.displayName)
        }
    }
}
