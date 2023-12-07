package com.example.zenglow

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
import com.example.zenglow.data.entities.Group
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.GroupState

@Composable
fun DeleteGroupDialog(
    state: GroupState,
    onEvent: (GroupEvent) -> Unit,
    modifier: Modifier = Modifier,
    group: Group
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(GroupEvent.HideDeleteDialog)
        },
        confirmButton = {
            Button(onClick = {
                onEvent(GroupEvent.DeleteGroup(group))
            }) {
                Text(text="Yes")
            }
        },
        dismissButton = {
            Button(onClick = {
                onEvent(GroupEvent.HideDeleteDialog)
            }) {
                Text(text="No")
            }
        },
        title = { Text(text= "Do you want to delete this group?")},
    )
}