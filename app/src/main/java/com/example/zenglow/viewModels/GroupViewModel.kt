package com.example.zenglow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenglow.data.entities.Group
import com.example.zenglow.data.GroupDao
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.GroupState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
 FILE: DeviceViewModel.kt
 AUTHOR: Daniel Blaško <xblask05>
 DESCRIPTION: ViewModel for handling frontend requests and backend operations for Group entities
 */

class GroupViewModel(
    private val dao: GroupDao
): ViewModel() {
    private val _groups = dao.readAllGroups().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    //Mutable state for the backend
    private val _state = MutableStateFlow(GroupState())
    //Immutable state for the frontend
    val state = combine(_state, _groups) {state, groups ->
        state.copy(groups = groups)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GroupState())

    fun onEvent(event: GroupEvent) {
        when(event) {
            //Save new group
            is GroupEvent.SaveGroup -> {
                val name = state.value.name
                if(name.isBlank()) {
                    return
                }
                val group = Group(
                    name = name
                )
                viewModelScope.launch {
                    dao.upsertGroup(group)
                }

                _state.update { it.copy(
                    isAddingGroup =  false,
                    name = ""
                ) }
            }
            is GroupEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            is GroupEvent.ShowCreateDialog -> {
                _state.update { it.copy(
                    isAddingGroup = true
                ) }
            }
            is GroupEvent.HideCreateDialog -> {
                _state.update { it.copy (
                    isAddingGroup = false
                ) }
            }

            is GroupEvent.ShowRenameDialog ->{
                _state.update {it.copy (
                    isUpdating = event.page
                )}
            }

            is GroupEvent.HideRenameDialog ->{
                _state.update {it.copy (
                    isUpdating = -1
                )}
            }

            is GroupEvent.RenameGroup ->{
                val name = state.value.name

                val group = Group(
                    name = name,
                    groupId = event.group.groupId,
                    onControl = event.group.onControl
                )

                viewModelScope.launch {
                    dao.upsertGroup(group)
                }

                _state.update { it.copy(
                    isUpdating =  -1,
                    name = ""
                ) }

            }

            is GroupEvent.UpdateGroup ->{

                val group = Group(
                    name = event.group.name,
                    groupId = event.group.groupId,
                    onControl = event.group.onControl
                )

                viewModelScope.launch {
                    dao.upsertGroup(group)
                }
            }

            is GroupEvent.ShowDeleteDialog ->{
                _state.update {it.copy (
                    isDeleting = event.page
                )}
            }

            is GroupEvent.HideDeleteDialog ->{
                _state.update {it.copy (
                    isDeleting = -1
                )}
            }

            //Delete group and set its devices to unassigned
            is GroupEvent.DeleteGroup -> {
                viewModelScope.launch {
                    dao.updateDevicesWithDeletedGroup(event.group.groupId)
                    dao.deleteGroup(event.group)
                }
            }
        }
    }
}