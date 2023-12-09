package com.example.zenglow.events

import com.example.zenglow.data.entities.Group

sealed interface GroupEvent {
    data object SaveGroup: GroupEvent
    data class SetName(val name: String): GroupEvent
    data object ShowCreateDialog: GroupEvent
    data object HideCreateDialog: GroupEvent

    data class ShowRenameDialog(val page: Int): GroupEvent
    data object HideRenameDialog: GroupEvent
    data class RenameGroup(val group: Group): GroupEvent
    data class UpdateGroup(val group: Group): GroupEvent

    data class ShowDeleteDialog(val page: Int): GroupEvent
    data object HideDeleteDialog: GroupEvent
    data class DeleteGroup(val group: Group): GroupEvent
}