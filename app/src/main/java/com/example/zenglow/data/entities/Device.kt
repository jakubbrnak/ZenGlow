package com.example.zenglow.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
data class Device(
    val displayName: String,
    val groupId: Int,
    @PrimaryKey(autoGenerate = true)
    val deviceId: Int = 0
)