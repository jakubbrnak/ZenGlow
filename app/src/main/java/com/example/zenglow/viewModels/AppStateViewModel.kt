package com.example.zenglow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenglow.data.GroupDao
import com.example.zenglow.data.entities.AppState
import com.example.zenglow.events.AppStateEvent
import com.example.zenglow.states.AppStateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/*
 FILE: AppStateViewModel.kt
 AUTHOR: Jakub Brnák <xbrnak01>
 DESCRIPTION: ViewModel for handling frontend requests and backend operations of the app's state, mainly for the moodBoost functionality
              and overall brightness and temperature levels

 */
class AppStateViewModel(
    private val dao: GroupDao
) : ViewModel() {
    private val dbAppStateFlow = dao.readState()
        .map { appState ->
            AppStateState(
                brightness = appState?.brightness ?: 0f,
                temperature = appState?.temperature ?: 0f,
                stressIndex = appState?.stressIndex ?: 0f,
                energy = appState?.energy ?: 0f,
                mentalState = appState?.mentalState ?: 0,
                currentMood = appState?.currentMood ?: 0,
                notifications = appState?.notifications ?: 0,
                connection = appState?.connection ?: 0
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            AppStateState()
        )

    // Mutable state for the backend
    private val _mutableState = MutableStateFlow(AppStateState())

    // Immutable state for the frontend
    val state: StateFlow<AppStateState> = combine(
        _mutableState,
        dbAppStateFlow
    ) { mutableState, dbState ->
        mutableState.copy(
            brightness = dbState.brightness,
            temperature = dbState.temperature,
            stressIndex = dbState.stressIndex,
            energy = dbState.energy,
            mentalState = dbState.mentalState,
            currentMood = dbState.currentMood,
            notifications = dbState.notifications,
            connection = dbState.connection
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AppStateState()
    )


    fun onEvent(event: AppStateEvent) {
        when(event) {
            is AppStateEvent.UpdateAppState ->{

                val appState = AppState(
                    stateTableId = 1,
                    brightness = event.appState.brightness,
                    temperature = event.appState.temperature,
                    stressIndex = event.appState.stressIndex,
                    energy = event.appState.energy,
                    mentalState = event.appState.mentalState,
                    currentMood = event.appState.currentMood,
                    notifications = event.appState.notifications,
                    connection = event.appState.connection
                )

                viewModelScope.launch {
                    dao.upsertAppState(appState)
                }

                _mutableState.update { it.copy(
                    brightness = 1f,
                    temperature = 0f,
                    stressIndex = 0f,
                    energy = 0f,
                    mentalState = 0,
                    currentMood = 0,
                    notifications = 0,
                    connection = 0
                ) }

            }
        }
    }
}