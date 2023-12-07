package com.example.zenglow.views

import DatabaseProgressIndicator
import EditButtonExample
import TimeDisplay
import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenglow.Screen
import com.example.zenglow.events.AppStateEvent
import com.example.zenglow.states.AppStateState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditMoodScreen(
    navController: NavController,
    appStateState: AppStateState,
    onAppStateEvent: (AppStateEvent) -> Unit
    ) {

    Scaffold(
        topBar = { MoodBoostTopBar { navController.navigateUp() } },

        ) {
            innerPadding -> MainScrollContent(innerPadding, navController, appStateState, onAppStateEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScrollContent(
    innerPadding: PaddingValues,
    navController: NavController,
    appStateState: AppStateState,
    onAppStateEvent: (AppStateEvent) -> Unit
    ){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xEC, 0xEC, 0xEC)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        SliderBoxPhysical(appStateState = appStateState, onAppStateEvent = onAppStateEvent)
        SliderBoxStress(appStateState = appStateState, onAppStateEvent = onAppStateEvent)
        RadioButtonGroup(appStateState = appStateState, onAppStateEvent = onAppStateEvent)
        OutlinedButton(onClick = { navController.navigateUp()},
                    modifier = Modifier.padding(top = 10.dp)
            ) {
            Text("Done")
        }
    }
}

fun onClick() {
    TODO("Not yet implemented")
}

@Composable
fun RadioButtonGroup(
    appStateState: AppStateState,
    onAppStateEvent: (AppStateEvent) -> Unit
) {
    // State to hold the index of the currently selected radio button
    var selectedOption = appStateState.mentalState

    Card(
        Modifier
            .width(340.dp)
            .height(150.dp)
            .padding(top = 40.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){
        Text(
            text = "Mental State",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.CenterHorizontally)
        )
        Row(
            Modifier
                .selectableGroup()
                .align(Alignment.Start)
                .padding(top = 10.dp),
            verticalAlignment = Alignment.Bottom

        )
        {
            // Create 5 radio buttons
            Text(text = "\uD83D\uDE14", fontSize = 24.sp, modifier = Modifier.padding(start = 58.dp))
            Text(text = "\uD83D\uDE44", fontSize = 24.sp, modifier = Modifier.padding(start = 19.dp))
            Text(text = "\uD83D\uDE10", fontSize = 24.sp, modifier = Modifier.padding(start = 18.dp))
            Text(text = "\uD83D\uDE42", fontSize = 24.sp, modifier = Modifier.padding(start = 18.dp))
            Text(text = "\uD83E\uDD29", fontSize = 24.sp, modifier = Modifier.padding(start = 17.dp))
        }
        Row(
            Modifier
                .selectableGroup()
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom

            )
            {
            // Create 5 radio buttons
            for (index in 0 until 5) {
                RadioButton(
                    selected = selectedOption == index,
                    onClick = {
                        selectedOption = index
                        val updatedAppState = appStateState.copy(mentalState = index)
                        onAppStateEvent(AppStateEvent.UpdateAppState(updatedAppState))

                        },
                    modifier = Modifier.semantics { contentDescription = "Option ${index + 1}" }
                )
            }
        }
    }
}

@Composable
fun SliderBoxPhysical(
    appStateState: AppStateState,
    onAppStateEvent: (AppStateEvent) -> Unit
){
    var physicalValue = appStateState.energy
    Card(
        Modifier
            .width(340.dp)
            .height(120.dp)
            .padding(top = 40.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)

    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Physical energy",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
            Row(
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
                Text(text = "LOW")
                Column {
                    Slider(
                        value = physicalValue,
                        onValueChange = {
                            physicalValue = it
                            val updatedAppState = appStateState.copy(energy = physicalValue)
                            onAppStateEvent(AppStateEvent.UpdateAppState(updatedAppState))
                                        },
                        modifier = Modifier
                            .width(240.dp)
                    )
                }
                Text(text = "HIGH")
            }
        }
    }
}

@Composable
fun SliderBoxStress(
    appStateState: AppStateState,
    onAppStateEvent: (AppStateEvent) -> Unit
){
    var stressValue = appStateState.stressIndex
    Card(
        Modifier
            .width(340.dp)
            .height(120.dp)
            .padding(top = 40.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)

    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Stress Level",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
            Row(
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
                Text(text = "LOW")
                Column {
                    Slider(
                        value = stressValue,
                        onValueChange = {
                            stressValue = it
                            val updatedAppState = appStateState.copy(stressIndex = stressValue)
                            onAppStateEvent(AppStateEvent.UpdateAppState(updatedAppState))
                            },
                        modifier = Modifier
                            .width(240.dp)
                    )
                }
                Text(text = "HIGH")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodBoostTopBar(onGoBackClicked: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text ="Mood Boost",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onGoBackClicked) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    modifier = Modifier.size(36.dp),
                    contentDescription = "Return back to home-page"
                )
            }
        }
    )
}





