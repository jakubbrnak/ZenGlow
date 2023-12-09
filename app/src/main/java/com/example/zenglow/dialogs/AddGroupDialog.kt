package com.example.zenglow.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.GroupState
/*
 FILE: AddGroupDialog.kt
 AUTHOR: Daniel Blaško <xblask05>
 DESCRIPTION: Dialog for creating a new group
 */
@Composable
fun AddGroupDialog(
    state: GroupState,
    onEvent: (GroupEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
           onEvent(GroupEvent.HideDeleteDialog)
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(onClick = {
                    onEvent(GroupEvent.SaveGroup)
                }) {
                  Text(text="Save group")
                }
            }
        },
        title = { Text(text= "Add Group")},
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