package com.example.zenglow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.zenglow.data.GroupDatabase
import com.example.zenglow.ui.theme.ZenGlowTheme
import com.example.zenglow.viewModels.AppStateViewModel
import com.example.zenglow.viewModels.DeviceViewModel
import com.example.zenglow.viewModels.GroupViewModel
/*
 FILE: MainActivity.kt
 AUTHOR: Daniel Blaško <xblask05>
 DESCRIPTION: Initialization class for the app
 */
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            GroupDatabase::class.java,
            "group.db"
        ).addMigrations(
            GroupDatabase.migration1to2, GroupDatabase.migration2to3, GroupDatabase.migration3to4, GroupDatabase.migration4to5, GroupDatabase.migration5to6
        ).build()
    }
    private val groupViewModel by viewModels<GroupViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GroupViewModel(db.dao) as T
                }
            }
        }
    )

    private val deviceViewModel by viewModels<DeviceViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DeviceViewModel(db.dao) as T
                }
            }
        }
    )

    private val appStateViewModel by viewModels<AppStateViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppStateViewModel(db.dao) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZenGlowTheme {
                val groupState by groupViewModel. state.collectAsState()
                val deviceState by deviceViewModel.state.collectAsState()
                val appStateState by appStateViewModel.state.collectAsState()
                navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    groupState = groupState,
                    deviceState = deviceState,
                    appStateState = appStateState,
                    onGroupEvent = groupViewModel::onEvent,
                    onDeviceEvent = deviceViewModel::onEvent,
                    onAppStateEvent = appStateViewModel::onEvent
                )
            }
        }
    }
}
