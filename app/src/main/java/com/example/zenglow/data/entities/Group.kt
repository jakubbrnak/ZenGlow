package com.example.zenglow.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group")
data class Group(
    @ColumnInfo(name = "onControl", defaultValue = "1")
    val onControl: Int = 1,
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val groupId: Int = 0
)