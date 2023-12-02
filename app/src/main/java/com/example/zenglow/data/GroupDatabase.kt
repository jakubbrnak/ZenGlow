package com.example.zenglow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zenglow.data.Group
import com.example.zenglow.data.GroupDao

@Database(entities = [Group::class], version = 1)
abstract class GroupDatabase: RoomDatabase() {
    abstract val dao: GroupDao
}