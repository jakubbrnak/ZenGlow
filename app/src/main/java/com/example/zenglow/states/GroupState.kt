package com.example.zenglow.states

import com.example.zenglow.data.entities.relations.GroupWithDevices

data class GroupState(
    val groups: List<GroupWithDevices> = emptyList(),
    val name: String = "",
    val isAddingGroup: Boolean = false,
    val isRenaming: Int = -1
)
