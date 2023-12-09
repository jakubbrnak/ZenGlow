package com.example.zenglow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.zenglow.data.entities.AppState
import com.example.zenglow.data.entities.Device
import com.example.zenglow.data.entities.Group
/*
 FILE: GroupDatabase
 AUTHOR: Daniel Blaško <xblask05>
 DESCRIPTION: File for initializing database and database migrations
 */
@Database(
    entities = [
        Group::class,
        Device::class,
        AppState::class,
    ],
    version = 6,
)

abstract class GroupDatabase : RoomDatabase() {

    abstract val dao: GroupDao


    companion object {
        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS device (deviceId INTEGER PRIMARY KEY AUTOINCREMENT,displayName TEXT NOT NULL,groupId INTEGER NOT NULL)")
                db.execSQL("ALTER TABLE group RENAME COLUMN id TO groupId")
            }

        }

        val migration2to3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Perform migration logic to add new columns
                database.execSQL("ALTER TABLE device ADD COLUMN color INTEGER NOT NULL DEFAULT 0xFFFFFF")
                database.execSQL("ALTER TABLE device ADD COLUMN brightness REAL NOT NULL DEFAULT 1.0")
                database.execSQL("ALTER TABLE device ADD COLUMN temperature REAL NOT NULL DEFAULT 0.0")
            }
        }

        val migration3to4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS appState (stateTableId INTEGER PRIMARY KEY AUTOINCREMENT, brightness REAL NOT NULL DEFAULT 1.0, temperature REAL NOT NULL DEFAULT 0.0, stressIndex REAL NOT NULL DEFAULT 0.0, energy REAL NOT NULL DEFAULT 0.0, mentalState INTEGER NOT NULL DEFAULT 0, currentMood INTEGER NOT NULL DEFAULT 0)")
            }
        }

        val migration4to5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'group' ADD COLUMN onControl INTEGER NOT NULL DEFAULT 1")
                database.execSQL("ALTER TABLE device ADD COLUMN onState INTEGER NOT NULL DEFAULT 1")
            }
        }

        val migration5to6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE appState ADD COLUMN notifications INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE appState ADD COLUMN connection INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}