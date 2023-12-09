package com.example.zenglow.dialogs


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zenglow.data.entities.Group
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.GroupState
/*
 FILE: RenameGroupDialog.kt
 AUTHOR: Daniel Blaško <xblask05>
 DESCRIPTION: Dialog for renaming a group
 */
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

            TextButton(onClick = {
                if (state.name != "") {
                    onEvent(GroupEvent.RenameGroup(group))
                }
            }) {
                Text(text="Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = {
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