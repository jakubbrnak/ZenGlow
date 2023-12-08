package com.example.zenglow.dialogs


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zenglow.data.entities.Group
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.GroupState

@Composable
fun RenameGroupDialog(
    state: GroupState,
    onEvent: (GroupEvent) -> Unit,
    modifier: Modifier = Modifier,
    group: Group
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(GroupEvent.HideRenameDialog)
        },
        confirmButton = {

            Button(onClick = {
                onEvent(GroupEvent.RenameGroup(group))
            }) {
                Text(text="Confirm")
            }
        },
        dismissButton = {
            Button(onClick = {
                onEvent(GroupEvent.HideRenameDialog)
            }) {
                Text(text="Cancel")
            }
        },
        title = { Text(text= "Rename Group")},
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(GroupEvent.SetName(it))
                    },
                    placeholder = {
                        Text(text = "Group name")
                    }
                )
            }
        }
    )
}