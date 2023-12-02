package com.example.zenglow.states

import com.example.zenglow.data.entities.Group

data class DeviceState(
    val devices: List<Group> = emptyList(),
    val displayName: String = "",
    val isAddingDevice: Boolean = false,
    val isRenaming: Int = -1
)