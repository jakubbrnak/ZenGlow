package com.example.zenglow.states

import com.example.zenglow.data.entities.Device

data class DeviceState(
    val devices: List<Device> = emptyList(),
    val freeDevices: List<Device> = emptyList(),
    val displayName: String = "",
    val brightness: Float = 0f,
    val color: Int = 0xFFFFFF,
    val temperature: Float = 0f,
    val isAddingDevice: Boolean = false,
//    val isRenaming: Int = -1,
    val name: String = "",
)