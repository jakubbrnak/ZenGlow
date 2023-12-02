package com.example.zenglow.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.zenglow.data.entities.Device
import com.example.zenglow.data.entities.Group

data class GroupWithDevices (
    @Embedded val group: Group,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId"
    )
    val devices: List<Device>
)
