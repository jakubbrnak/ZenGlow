package com.example.zenglow.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.zenglow.data.entities.Device
import com.example.zenglow.data.entities.Group
/*
 FILE: GroupWithDevices.kt
 AUTHOR: Daniel Blaško <xblask05>
 DESCRIPTION: Data class of relation between Group and Device (1:N)
 */
data class GroupWithDevices (
    @Embedded val group: Group,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId"
    )
    val devices: List<Device>
)
