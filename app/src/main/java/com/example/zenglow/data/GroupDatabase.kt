package com.example.zenglow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zenglow.data.entities.Device
import com.example.zenglow.data.entities.Group

@Database(
    entities = [
        Group::class,
        Device::class,
    ],
    version = 2,
)
abstract class GroupDatabase: RoomDatabase() {
    abstract val dao: GroupDao
}