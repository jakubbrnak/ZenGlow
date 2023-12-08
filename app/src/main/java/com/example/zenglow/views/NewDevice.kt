package com.example.zenglow.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.zenglow.dialogs.AddDeviceDialog
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
                    Text("Assign device to group")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        NewDeviceScrollContent(innerPadding, navController, state, groupId, onEvent)
    }
}

/*
    DESCRIPTION:    NewDevice -> NewDeviceScrollContent
                    Composable for displaying the content of the page
*/
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
        Text(
            "Unassigned devices",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.Start) // Aligns the text to the start within the column
                .padding(start = 16.dp, top = 20.dp)  // Adds padding to the start
        )
        // Display list of devices
        if (state.freeDevices.isNotEmpty()) {
            BoxWithConstraints {
                val constraints = maxWidth - 32.dp
                val maxHeight = maxHeight * 0.8f
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .heightIn(0.dp, maxHeight)
                ) {
                    LazyColumn(contentPadding = PaddingValues(12.dp)) {
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
            }
        }
        FloatingActionButton(
            onClick = {
                onEvent(DeviceEvent.ShowDialog)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(Icons.Filled.Add, "Add New Device")
                Text(text = "Add new device")
            }
        }

        if (state.isAddingDevice) {
            AddDeviceDialog(state = state, onEvent = onEvent)
        }
    }
}

/*
    DESCRIPTION:    NewDevice -> DeviceItem
                    Composable for displaying a device in a lazy column
*/
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
                        painter = painterResource(id = R.drawable.bulb),
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
