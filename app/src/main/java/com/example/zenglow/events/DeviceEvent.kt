package com.example.zenglow.events

import com.example.zenglow.data.entities.Device

sealed interface DeviceEvent {
    object SaveDevice: DeviceEvent
    data class SetName(val displayName: String): DeviceEvent
    object ShowDialog: DeviceEvent
    object HideDialog: DeviceEvent
    data class  DeleteDevice(val device: Device): DeviceEvent

    data class ShowRenameDialog(val page: Int): DeviceEvent
    object HideRenameDialog: DeviceEvent
    data class UpdateDevice(val device: Device): DeviceEvent
}