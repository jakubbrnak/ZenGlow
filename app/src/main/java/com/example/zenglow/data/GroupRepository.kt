package com.example.zenglow.data

import com.example.zenglow.data.entities.Group
import kotlinx.coroutines.flow.Flow

class SGroupRepository (private val groupDao: GroupDao) {
    val readAll: Flow<List<Group>> = groupDao.readAll()
}