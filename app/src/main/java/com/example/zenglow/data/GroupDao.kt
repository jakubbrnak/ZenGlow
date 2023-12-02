package com.example.zenglow.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
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

    @Transaction
    @Query("SELECT * FROM 'group'")
    fun readAll(): Flow<List<Group>>

    @Transaction
    @Query("SELECT * FROM 'group' WHERE groupId = :groupId")
    suspend fun getGroupWithDevices(groupId: Int): List<GroupWithDevices>
}

