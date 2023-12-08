package com.example.zenglow.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zenglow.data.entities.Device
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.states.DeviceState

@Composable
fun RenameDeviceDialog(
    state: DeviceState,
    onEvent: (DeviceEvent) -> Unit,
    modifier: Modifier = Modifier,
    device: Device
) {
//    AlertDialog(
//        modifier = modifier,
//        onDismissRequest = {
//            onEvent(DeviceEvent.HideRenameDialog)
//        },
//        confirmButton = {
//
//            Button(onClick = {
//                onEvent(DeviceEvent.RenameDevice(device))
//            }) {
//                Text(text="Confirm")
//            }
//        },
//        dismissButton = {
//            Button(onClick = {
//                onEvent(DeviceEvent.HideRenameDialog)
//            }) {
//                Text(text="Cancel")
//            }
//        },
//        title = { Text(text= "Rename Device") },
//        text = {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                TextField(
//                    value = state.name,
//                    onValueChange = {
//                        onEvent(DeviceEvent.SetName(it))
//                    },
//                    placeholder = {
//                        Text(text = "Device name")
//                    }
//                )
//            }
//        }
//    )
}