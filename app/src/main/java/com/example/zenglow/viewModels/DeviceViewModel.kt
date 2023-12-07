package com.example.zenglow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenglow.data.GroupDao
import com.example.zenglow.data.entities.Device
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.states.DeviceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeviceViewModel(
    private val dao: GroupDao
): ViewModel() {
    private val _devices = dao.readAllDevices().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _freeDevices = dao.getDevicesWithoutGroup().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    //Mutable state for the backend
    private val _state = MutableStateFlow(DeviceState())
    //Immutable state for the frontend
    val state = combine(_state, _devices, _freeDevices) { state, devices, freeDevices ->
        state.copy(devices = devices, freeDevices = freeDevices)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DeviceState())

    fun onEvent(event: DeviceEvent) {
        when(event) {
            //Delete device
            is DeviceEvent.DeleteDevice -> {
                viewModelScope.launch {
                    dao.deleteDevice(event.device)
                }
            }
            //Save new device
            is DeviceEvent.SaveDevice -> {
                val displayName = state.value.displayName
                if(displayName.isBlank()) {
                    return
                }

                val device = Device(
                    displayName = displayName,
                    groupId = -1,
                )

                viewModelScope.launch {
                    dao.upsertDevice(device)
                }

                _state.update { it.copy(
                    isAddingDevice =  false,
                    displayName = ""
                ) }
            }
            is DeviceEvent.SetName -> {
                _state.update { it.copy(
                    displayName = event.displayName
                ) }
            }
            is DeviceEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingDevice = true
                ) }
            }
            is DeviceEvent.HideDialog -> {
                _state.update { it.copy (
                    isAddingDevice = false
                ) }
            }

            is DeviceEvent.ShowRenameDialog ->{
                _state.update {it.copy (
                    isRenaming = event.page
                )}
            }

            is DeviceEvent.HideRenameDialog ->{
                _state.update {it.copy (
                    isRenaming = -1
                )}
            }

            is DeviceEvent.UpdateDevice ->{

                val device = Device(
                    displayName = event.device.displayName,
                    deviceId = event.device.deviceId,
                    groupId = event.device.groupId,
                    temperature = event.device.temperature,
                    brightness = event.device.brightness,
                    color = event.device.color,
                    onState = event.device.onState
                )

                viewModelScope.launch {
                    dao.upsertDevice(device)
                }

                _state.update { it.copy(
                    displayName = "",
                    temperature = 0f,
                    brightness = 1f,
                    color = 0xFFFFFF
                ) }

            }
        }
    }
}