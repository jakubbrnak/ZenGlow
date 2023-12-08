package com.example.zenglow.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.DeviceState

@Composable
fun AddDeviceDialog(
    state: DeviceState,
    onEvent: (DeviceEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(DeviceEvent.HideDialog)
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(DeviceEvent.SaveDevice)
                }) {
                    Text(text="Save device")
                }
            }
        },
        title = { Text(text= "Add Device")},
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.displayName,
                    onValueChange = {
                        onEvent(DeviceEvent.SetName(it))
                    },
                    placeholder = {
                        Text(text = "Device name")
                    }
                )
            }
        }
    )
}