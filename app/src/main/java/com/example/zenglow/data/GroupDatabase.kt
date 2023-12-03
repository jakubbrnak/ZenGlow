package com.example.zenglow.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.example.zenglow.data.entities.Device
import com.example.zenglow.data.entities.Group

@Database(
    entities = [
        Group::class,
        Device::class,
    ],
    version = 2,
    autoMigrations = [
        AutoMigration (from = 1, to = 2, spec = GroupDatabase.MyAutoMigration::class)
    ]

)
abstract class GroupDatabase: RoomDatabase() {
    @RenameColumn(tableName = "group", fromColumnName = "id", toColumnName = "groupId")
    class MyAutoMigration : AutoMigrationSpec

    abstract val dao: GroupDao
}