package com.example.zenglow.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
data class Device(
    @ColumnInfo(name = "onState", defaultValue = "1")
    val onState: Int = 1,
    @ColumnInfo(name = "temperature", defaultValue = "0.0")
    val temperature: Float = 0.0f,
    @ColumnInfo(name = "brightness", defaultValue = "1.0")
    val brightness: Float = 1.0f,
    @ColumnInfo(name = "color", defaultValue = "0xFFFFFF")
    val color: Int = 0xFFFFFF,
    val displayName: String,
    val groupId: Int,
    @PrimaryKey(autoGenerate = true)
    val deviceId: Int = 0,
)

