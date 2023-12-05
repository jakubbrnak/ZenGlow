package com.example.zenglow.states

import androidx.room.ColumnInfo
import com.example.zenglow.data.entities.AppState
import com.example.zenglow.data.entities.Device
import kotlinx.coroutines.flow.StateFlow

data class AppStateState(
    val brightness: Float = 0f,
    val temperature: Float = 0f,
    val stressIndex: Float = 0f,
    val energy: Float = 0f,
    val mentalState: Int = 0,
    val currentMood: Int = 0,
)
