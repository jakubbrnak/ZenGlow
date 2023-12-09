package com.example.zenglow.states

data class AppStateState(
    val brightness: Float = 0f,
    val temperature: Float = 0f,
    val stressIndex: Float = 0f,
    val energy: Float = 0f,
    val mentalState: Int = 0,
    val currentMood: Int = 0,
    val notifications: Int = 0,
    val connection: Int = 0
)
