package com.example.zenglow.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenglow.AddGroupDialog
import com.example.zenglow.RenameDialog
import com.example.zenglow.Screen
import com.example.zenglow.data.entities.Device
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.GroupState
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    state: GroupState,
    onEvent: (GroupEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Zenglow")
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Go to Settings"
                        )
                    }
                },

                )
        }
    ) { innerPadding ->
        MainScrollContent(innerPadding, navController, state, onEvent)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScrollContent(
    innerPadding: PaddingValues,
    navController: NavController,
    state: GroupState,
    onEvent: (GroupEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .background(Color(0xEC, 0xEC, 0xEC)),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(
            text = "Home",
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.displayLarge.fontSize
        )
        FloatingActionButton(onClick = {
            onEvent(GroupEvent.ShowDialog)
        }) {
            Row{
                Icon(Icons.Filled.Add, "Add New Group")
                Text(text = "Add new group")
            }
        }
        FloatingActionButton(onClick = {
            navController.navigate(Screen.MoodBoost.route)
        }) {
            Text(text = "MoodBoost")
        }
        if(state.isAddingGroup) {
            AddGroupDialog(state = state, onEvent = onEvent)
        }

//        Spacer(modifier = Modifier.weight(1f))
        val pagerState = rememberPagerState(pageCount = {
            state.groups.size
        })
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(start = 46.dp, end = 24.dp)
        ) {page->
            Card(
                Modifier
                    .size(300.dp)
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${state.groups[page].group.name}",
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.W900
                    )
                    //Contain the lazycolumn into a box so that it doesn't push other components away
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            items(state.groups[page].devices.size) { device ->
                                GroupDeviceItem(
                                    modifier = Modifier,
                                    device = state.groups[page].devices[device],
                                    navController = navController,
                                )
                            }
                        }
                    }

                    Row(
                    ){
                        IconButton(onClick = {
                            navController.navigate("${Screen.NewDevice.route}/${state.groups[page].group.groupId}")}
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add New Device")
                        }
                        IconButton(onClick = {
                            onEvent(GroupEvent.DeleteGroup(state.groups[page].group))
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "delete group")
                        }

                        IconButton(onClick = {
                            onEvent(GroupEvent.ShowRenameDialog(page))
                        }) {
                            Icon(Icons.Filled.Create, contentDescription = "rename group")
                        }

                        if(state.isRenaming == page) {
                            RenameDialog(state = state, onEvent = onEvent, group = state.groups[page].group)
                        }
                    }


                }

            }

        }
    }
}
@Composable
fun GroupDeviceItem(
    modifier: Modifier,
    device: Device,
    navController: NavController,
) {
    Card(
        modifier
            .padding(10.dp)
            .wrapContentSize()
            .clickable {
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(Icons.Outlined.List, "Add New Device To Group")
            Text(text = device.displayName)
        }
    }
}