package com.example.zenglow.data

import android.devicelock.DeviceId
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.zenglow.data.entities.AppState
import com.example.zenglow.data.entities.Device
import com.example.zenglow.data.entities.Group
import com.example.zenglow.data.entities.relations.GroupWithDevices
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Upsert
    suspend fun upsertGroup(group: Group)
    @Delete
    suspend fun deleteGroup(group: Group)
    @Upsert
    suspend fun upsertDevice(device: Device)
    @Delete
    suspend fun deleteDevice(device: Device)
    @Upsert
    suspend fun upsertAppState(appState: AppState)

    @Transaction
    @Query("SELECT * FROM 'group'")
    fun readAllGroups(): Flow<List<GroupWithDevices>>

    @Transaction
    @Query("SELECT * FROM 'group' WHERE groupId = :groupId")
    suspend fun getGroupWithDevices(groupId: Int): List<GroupWithDevices>

    @Transaction
    @Query("UPDATE device SET groupId = -1 WHERE groupId = :deletedGroupId")
    suspend fun updateDevicesWithDeletedGroup(deletedGroupId: Int)

    @Transaction
    @Query("SELECT * FROM 'device'")
    fun readAllDevices(): Flow<List<Device>>

    @Transaction
    @Query("SELECT * FROM 'device' WHERE groupId = -1")
    fun getDevicesWithoutGroup(): Flow<List<Device>>

    @Transaction
    @Query("SELECT * FROM 'appState' LIMIT 1")
    fun readState(): Flow<AppState?>
}

