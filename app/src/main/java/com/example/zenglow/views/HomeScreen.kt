package com.example.zenglow.views

import android.graphics.Color.argb
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavController
import com.example.zenglow.AddDeviceDialog
import com.example.zenglow.AddGroupDialog
import com.example.zenglow.R
import com.example.zenglow.RenameDialog
import com.example.zenglow.Screen
import com.example.zenglow.data.entities.Device
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.GroupState
import kotlin.math.absoluteValue
/*
 FILE: HomeScreen.kt
 AUTHOR: Daniel Blaško <xblask05>
 DESCRIPTION: Main Page of the app containing a link to the MoodBoost window,
              light intensity and temperature sliders and a pager with light groups.
              Groups contain devices, which can be controlled manually, the user can also access
              the detail page of the device
 */

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
                VerticalBrightnessSlider(
                    value = value1,
                    onValueChange = {value1 = it},
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                )
                VerticalTemperatureSlider(
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
                        if(state.isAddingGroup) {
                            AddGroupDialog(state = state, onEvent = onEvent)
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
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            ListItem(
                modifier = Modifier.height(48.dp),
                headlineContent = {Text(device.displayName)},
                leadingContent = {
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Black, CircleShape)
                            .clip(CircleShape)
                            .background(color = Color(0xfffcba03))
                            .size(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.emoji_objects),
                            contentDescription = "bulbIcon",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                },
                trailingContent = {
                    var checked by remember { mutableStateOf(true) }
                    Switch(
                        modifier = Modifier
                            .scale(0.8f)
                            .padding(end = 8.dp),
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                        }
                    )
                }
            )
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
            //Brightness slider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f) // Expandable inner Row
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.brightness_empty),
                        contentDescription = "low brightness",
                        modifier = Modifier
                            .size(20.dp)
                    )
                    var value by remember { mutableStateOf(0f) }
                    Slider(
                        value = value ,
                        onValueChange = {value = it},
                        modifier = Modifier
                            .width(130.dp)
                            .height(24.dp)
                            .padding(8.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.brightness_high),
                        contentDescription = "high brightness",
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
                //Button to device detail page
                Image(
                    painter = painterResource(id = R.drawable.tune),
                    contentDescription = "open device detail page",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.navigate("${Screen.DeviceConfig.route}/${device.deviceId}")
                        }
                )
            }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Black
        )
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


/*
Composable for a vertical slider controlling the overall brightness of all lights
The color of the icon in the slider thumb changes dynamically depending on the slider position
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalBrightnessSlider(
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
    val backgroundColor = calculateBackgroundColor(value)

    Slider(
        colors = colors,
        interactionSource = interactionSource,
        onValueChangeFinished = onValueChangeFinished,
        steps = steps,
        valueRange = valueRange,
        enabled = enabled,
        value = value,
        thumb = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color(backgroundColor))
                    .size(36.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.device_thermostat_rotated),
                    contentDescription = "bulbIcon",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalTemperatureSlider(
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
    val backgroundColor = calculateBackgroundColor(value)

    Slider(
        colors = colors,
        interactionSource = interactionSource,
        onValueChangeFinished = onValueChangeFinished,
        steps = steps,
        valueRange = valueRange,
        enabled = enabled,
        value = value,
        thumb = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color(backgroundColor))
                    .size(36.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.device_thermostat_rotated),
                    contentDescription = "bulbIcon",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        },
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

@Composable
private fun calculateBackgroundColor(value: Float): Int {
    val blue = argb(255, 3, 186, 252)
    val yellow = argb(255, 252, 186, 3)

    val ratio = value.coerceIn(0f, 1f)

    return ColorUtils.blendARGB(blue, yellow, ratio)
}