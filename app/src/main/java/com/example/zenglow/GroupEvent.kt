package com.example.zenglow

import android.graphics.pdf.PdfDocument.Page
import com.example.zenglow.data.Group

sealed interface GroupEvent {
    object SaveGroup: GroupEvent
    data class SetName(val name: String): GroupEvent
    object ShowDialog: GroupEvent
    object HideDialog: GroupEvent
    data class  DeleteGroup(val group: Group): GroupEvent

    data class ShowRenameDialog(val page: Int): GroupEvent
    object HideRenameDialog: GroupEvent
    data class RenameGroup(val group: Group): GroupEvent
}