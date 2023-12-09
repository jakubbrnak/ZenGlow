package com.example.zenglow.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appState")
data class AppState(
    @ColumnInfo(name = "brightness", defaultValue = "1.0")
    val brightness: Float = 1.0f,
    @ColumnInfo(name = "temperature", defaultValue = "0.0")
    val temperature: Float = 0.0f,
    @ColumnInfo(name = "stressIndex", defaultValue = "0.0")
    val stressIndex: Float = 0.0f,
    @ColumnInfo(name = "energy", defaultValue = "0.0")
    val energy: Float = 0.0f,
    @ColumnInfo(name = "mentalState", defaultValue = "0")
    val mentalState: Int = 0,
    @ColumnInfo(name = "currentMood", defaultValue = "0")
    val currentMood: Int = 0,
    @ColumnInfo(name = "notifications", defaultValue = "0")
    val notifications: Int = 0,
    @ColumnInfo(name = "connection", defaultValue = "0")
    val connection: Int = 0,

    @PrimaryKey(autoGenerate = true)
    val stateTableId: Int = 0,
)

