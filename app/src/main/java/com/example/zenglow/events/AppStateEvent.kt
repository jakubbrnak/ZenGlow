package com.example.zenglow.events

import com.example.zenglow.states.AppStateState

sealed interface AppStateEvent {
    data class UpdateAppState(val appState: AppStateState): AppStateEvent
}