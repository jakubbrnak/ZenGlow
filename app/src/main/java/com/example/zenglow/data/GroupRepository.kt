package com.example.zenglow.data

import com.example.zenglow.data.entities.relations.GroupWithDevices
import kotlinx.coroutines.flow.Flow

class SGroupRepository (private val groupDao: GroupDao) {
    val readAll: Flow<List<GroupWithDevices>> = groupDao.readAllGroups()
}