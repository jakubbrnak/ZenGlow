package com.example.zenglow.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group")
data class Group(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val groupId: Int = 0
)