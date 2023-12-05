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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.HorizontalDivider
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
/*
 FILE: NewDevice.kt
 AUTHOR: Daniel Blaško <xblask05>
 DESCRIPTION: Page for assigning a new device to a group from the list of unassigned devices.
              A new device can also be created here.

 */

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
            .padding(innerPadding)
            .background(Color(0xEC, 0xEC, 0xEC)),

    ) {
        FloatingActionButton(
            onClick = {
                onEvent(DeviceEvent.ShowDialog)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
//                .align(Alignment.BottomEnd) // Align to the bottom end of the screen
        ) {
            Row {
                Icon(Icons.Filled.Add, "Add New Device")
                Text(text = "Add device manually")
            }
        }
        Text(
            text = "Available devices",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        // Display list of devices
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(contentPadding = PaddingValues(top = 16.dp)) {
                items(state.freeDevices.size) { device ->
                    DeviceItem(
                        modifier = Modifier,
                        device = state.freeDevices[device],
                        navController = navController,
                        groupId = groupId,
                        onEvent = onEvent
                    )
                }
            }

        }

        if (state.isAddingDevice) {
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
        ListItem(
            modifier = Modifier
                .height(80.dp)
                .clickable {
                        //Add groupId to device, then navigate back to home screen
                        val updatedDevice = device.copy(groupId = groupId)
                        onEvent(DeviceEvent.UpdateDevice(updatedDevice))
                        navController.navigate(Screen.Home.route)
                },
            headlineContent = {Text(device.displayName)},
            leadingContent = {
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Black, CircleShape)
                        .clip(CircleShape)
                        .background(color = Color(0xfffcba03))
                        .size(45.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.emoji_objects),
                        contentDescription = "bulbIcon",
                        modifier = Modifier
                            .size(36.dp)
                    )
                }
            },
            trailingContent = {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "add")
            }
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Black
        )
}
