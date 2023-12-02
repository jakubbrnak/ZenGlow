package com.example.zenglow.states

import com.example.zenglow.data.entities.Group

data class GroupState(
    val groups: List<Group> = emptyList(),
    val name: String = "",
    val isAddingGroup: Boolean = false,
    val isRenaming: Int = -1
)
