package com.example.zenglow

import com.example.zenglow.data.Group

data class GroupState(
    val groups: List<Group> = emptyList(),
    val name: String = "",
    val isAddingGroup: Boolean = false,
    val isRenaming: Int = -1
)
