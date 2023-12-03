package com.example.zenglow

sealed class Screen(var route: String){
    object Home: Screen(route = "homeScreen")
    object NewDevice: Screen(route = "newDeviceScreen")
    object Settings: Screen(route = "settingsScreen")
    object MoodBoost: Screen(route = "moodBoostScreen")
}