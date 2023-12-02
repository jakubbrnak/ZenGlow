package com.example.zenglow.data

import com.example.zenglow.data.Group
import com.example.zenglow.data.GroupDao
import kotlinx.coroutines.flow.Flow

class SGroupRepository (private val groupDao: GroupDao) {
    val readAll: Flow<List<Group>> = groupDao.readAll()
}