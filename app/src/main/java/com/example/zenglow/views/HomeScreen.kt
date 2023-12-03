package com.example.zenglow.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenglow.AddGroupDialog
import com.example.zenglow.R
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
            .padding(innerPadding)
            .background(Color(0xEC, 0xEC, 0xEC)),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.moodboosticon),
                contentDescription = "moodBoostIcon",
                modifier = Modifier
                    .size(150.dp)
                    .clickable { navController.navigate(Screen.MoodBoost.route) }
            )
            Row() {
                var value1 by remember { mutableStateOf(0f) }
                var value2 by remember { mutableStateOf(0f) }
                VerticalSlider(
                    value = value1,
                    onValueChange = {value1 = it},
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                )
                VerticalSlider(
                    value = value2,
                    onValueChange = {value2 = it},
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                )
            }
        }

        //Pager for groups
        val pagerState = rememberPagerState(pageCount = {
            state.groups.size + 1 //For group creating card
        })

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(start = 46.dp, end = 24.dp)
        ) {page->
            if (page < state.groups.size) {
                // Render regular pages based on state.groups
                Card(
                    Modifier
                        .width(300.dp)
                        .height(450.dp)
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
            } else {
                // Render the extra page (new content for the additional page)
                Card(
                    Modifier
                        .size(450.dp)
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        FloatingActionButton(
                            onClick = {
                                onEvent(GroupEvent.ShowDialog)
                            },
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically // Align icon and text vertically
                            ) {
                                Icon(Icons.Filled.Add, "Add New Group")
                                Text(text = "Add new group")
                            }
                        }
                    }
                }
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
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
            .padding(5.dp)
            .wrapContentSize(),
//            .clickable {
//            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column() {
//            Row(
//                modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.Top,
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Icon(Icons.Outlined.List, "Add New Device To Group")
//                Text(text = device.displayName)
//                var checked by remember { mutableStateOf(true) }
//                Switch(
//                    checked = checked,
//                    onCheckedChange = {
//                        checked = it
//                    }
//                )
//            }
            ListItem(
                headlineContent = {Text(device.displayName)},
                leadingContent = {
                    Icon(Icons.Filled.Menu, contentDescription = "Bulbicon")
                },
                trailingContent = {
                    var checked by remember { mutableStateOf(true) }
                    Switch(
                        modifier = Modifier.scale(0.8f),
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                        }
                    )
                }
            )
            Row(){
                var value by remember { mutableStateOf(0f) }
                Slider(
                    value = value ,
                    onValueChange = {value = it},
                    modifier = Modifier
                        .width(150.dp)
                        .height(25.dp)
                )
                Icon(Icons.Outlined.Settings, "Open device detail page")
            }
        }

    }
}

@Composable
fun imageBtn(
    navController: NavController
) {
        Image(
            painter = painterResource(id = R.drawable.moodboosticon),
            contentDescription = "moodBoostIcon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(150.dp)
                .clickable { navController.navigate(Screen.MoodBoost.route) })
}

@Composable
fun VerticalSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    /*@IntRange(from = 0)*/
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors()
){
    Slider(
        colors = colors,
        interactionSource = interactionSource,
        onValueChangeFinished = onValueChangeFinished,
        steps = steps,
        valueRange = valueRange,
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .graphicsLayer {
                rotationZ = 270f
                transformOrigin = TransformOrigin(0f, 0f)
            }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(
                    Constraints(
                        minWidth = constraints.minHeight,
                        maxWidth = constraints.maxHeight,
                        minHeight = constraints.minWidth,
                        maxHeight = constraints.maxHeight,
                    )
                )
                layout(placeable.height, placeable.width) {
                    placeable.place(-placeable.width, 0)
                }
            }
            .then(modifier)
    )
}