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

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            GroupDatabase::class.java,
            "group.db"
        ).build()
    }
    private val viewModel by viewModels<GroupViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GroupViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZenGlowTheme {
                val state by viewModel. state.collectAsState()
                navController = rememberNavController()
                SetupNavGraph(navController = navController, state = state, onEvent = viewModel::onEvent)
            }
        }
    }
}
