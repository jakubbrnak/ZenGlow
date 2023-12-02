package com.example.zenglow.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.zenglow.data.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Upsert
    suspend fun upsertGroup(group: Group)
    @Delete
    suspend fun deleteGroup(group: Group)


    @Query("SELECT * FROM 'group'")
    fun readAll(): Flow<List<Group>>
}

