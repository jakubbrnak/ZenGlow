package com.example.zenglow.events

import com.example.zenglow.data.entities.Device

sealed interface DeviceEvent {
    data object SaveDevice: DeviceEvent
    data class SetName(val displayName: String): DeviceEvent
    data object ShowDialog: DeviceEvent
    data object HideDialog: DeviceEvent
    data class  DeleteDevice(val device: Device): DeviceEvent
    data class RenameDevice(val device: Device): DeviceEvent
    data class UpdateDevice(val device: Device): DeviceEvent
}