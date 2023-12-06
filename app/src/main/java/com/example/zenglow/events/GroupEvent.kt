package com.example.zenglow.events

import com.example.zenglow.data.entities.Group

sealed interface GroupEvent {
    object SaveGroup: GroupEvent
    data class SetName(val name: String): GroupEvent
    object ShowCreateDialog: GroupEvent
    object HideCreateDialog: GroupEvent

    data class ShowRenameDialog(val page: Int): GroupEvent
    object HideRenameDialog: GroupEvent
    data class RenameGroup(val group: Group): GroupEvent

    data class ShowDeleteDialog(val page: Int): GroupEvent
    object HideDeleteDialog: GroupEvent
    data class DeleteGroup(val group: Group): GroupEvent
}