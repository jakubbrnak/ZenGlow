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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import com.example.zenglow.DeleteGroupDialog
import com.example.zenglow.R
import com.example.zenglow.RenameGroupDialog
import com.example.zenglow.Screen
import com.example.zenglow.data.entities.Device
import com.example.zenglow.events.AppStateEvent
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.AppStateState
import com.example.zenglow.states.DeviceState
import com.example.zenglow.states.GroupState
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
    groupState: GroupState,
    deviceState: DeviceState,
    appStateState: AppStateState,
    onGroupEvent: (GroupEvent) -> Unit,
    onDeviceEvent: (DeviceEvent) -> Unit,
    onAppStateEvent: (AppStateEvent) -> Unit
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
        MainScrollContent(innerPadding, navController, groupState, deviceState, appStateState, onGroupEvent, onDeviceEvent, onAppStateEvent)
    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScrollContent(
    innerPadding: PaddingValues,
    navController: NavController,
    groupState: GroupState,
    deviceState: DeviceState,
    appStateState: AppStateState,
    onGroupEvent: (GroupEvent) -> Unit,
    onDeviceEvent: (DeviceEvent) -> Unit,
    onAppStateEvent: (AppStateEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xEC, 0xEC, 0xEC)),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        //Mood Boost button and general control sliders
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            moodBoostBtn(navController = navController)
            Row (
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VerticalBrightnessSlider(
                    appStateState = appStateState,
                    onAppStateEvent = onAppStateEvent,
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                )
                VerticalTemperatureSlider(
                    appStateState = appStateState,
                    onAppStateEvent = onAppStateEvent,
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                )
            }
        }
        //Pager for groups
        val pagerState = rememberPagerState(pageCount = {
            groupState.groups.size + 2 //For ungrouped devices and group creating card
        })

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(start = 46.dp, end = 24.dp)
        ) {page->
            if (page < groupState.groups.size ) {
                var toggleNotification by remember { mutableStateOf(value = false) }
                var typeNotification by remember { mutableStateOf(value = 0) }                 // TODO (should be read from database)
                val optionsNotification = listOf("Manual", "Mood")
                // Render regular pages based on state.groups
                Card(
                    Modifier
                        .width(300.dp)
                        .height(450.dp)
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
                            text = "${groupState.groups[page].group.name}",
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W900
                        )
                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .padding(
                                    start = 42.dp,
                                    top = 8.dp,
                                    end = 42.dp,
                                    bottom = 8.dp),
                        ) {
                            optionsNotification.forEachIndexed { index, label ->
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = optionsNotification.size),
                                    onClick = { typeNotification = index },
                                    selected = index == typeNotification               // TODO (should update/linked to database)
                                ) {
                                    Text(label)
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            IconButton(onClick = {
                                navController.navigate("${Screen.NewDevice.route}/${groupState.groups[page].group.groupId}")}
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Add New Device")
                            }
                            IconButton(onClick = {
                                onGroupEvent(GroupEvent.ShowDeleteDialog(page))
                            }) {
                                Icon(Icons.Filled.Delete, contentDescription = "delete group")
                            }

                            IconButton(onClick = {
                                onGroupEvent(GroupEvent.ShowRenameDialog(page))
                            }) {
                                Icon(Icons.Filled.Create, contentDescription = "rename group")
                            }

                            if(groupState.isRenaming == page) {
                                RenameGroupDialog(state = groupState, onEvent = onGroupEvent, group = groupState.groups[page].group)
                            }

                            if(groupState.isDeleting == page) {
                                DeleteGroupDialog(state = groupState, onEvent = onGroupEvent, group = groupState.groups[page].group)
                            }
                        }
                        //Contain the lazyColumn into a box so that it doesn't push other components away
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .weight(1f)
                        ) {
                            LazyColumn(contentPadding = PaddingValues(12.dp)) {
                                items(groupState.groups[page].devices.size) { device ->
                                    GroupDeviceItem(
                                        modifier = Modifier,
                                        device = groupState.groups[page].devices[device],
                                        navController = navController,
                                        onDeviceEvent = onDeviceEvent,
                                    )
                                }
                            }
                        }
                    }
                }
            } else if (page == groupState.groups.size ) {
                // Render the extra page (new content for the additional page)
                Card(
                    Modifier
                        .width(300.dp)
                        .height(450.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.tertiaryContainer),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Unassigned devices",
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W900
                        )
                        Row{
                            IconButton(onClick = {
                                onDeviceEvent(DeviceEvent.ShowDialog)
                            }
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Add New Device")
                            }
                            if (deviceState.isAddingDevice) {
                                AddDeviceDialog(state = deviceState, onEvent = onDeviceEvent)
                            }
                        }
                        //Contain the lazyColumn into a box so that it doesn't push other components away
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .weight(1f)
                        ) {
                            LazyColumn(contentPadding = PaddingValues(12.dp)) {
                                items(deviceState.freeDevices.size) { device ->
                                    GroupDeviceItem(
                                        modifier = Modifier,
                                        device = deviceState.freeDevices[device],
                                        navController = navController,
                                        onDeviceEvent = onDeviceEvent,
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
             // Render the extra page (new content for the additional page)
                Card(
                    Modifier
                        .size(450.dp)
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
                                onGroupEvent(GroupEvent.ShowCreateDialog)
                            },
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically // Align icon and text vertically
                            ) {
                                Icon(Icons.Filled.Add, "Add New Group")
                                Text(text = "Add new group")
                            }
                        }
                        if(groupState.isAddingGroup) {
                            AddGroupDialog(state = groupState, onEvent = onGroupEvent)
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
/*
DESCRIPTION: Composable for basic control over a device. It contains a switch to turn it on or off,
             A brightness slider and a button to navigate to the device's detailed control page.
             The background of the light bulb icon changes depending on the device's chosen colour.
 */
@Composable
fun GroupDeviceItem(
    modifier: Modifier,
    device: Device,
    navController: NavController,
    onDeviceEvent: (DeviceEvent) -> Unit
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
                            .background(color = Color(device.color))
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
                    var value by remember { mutableStateOf(device.brightness) }
                    Slider(
                        value = value ,
                        valueRange = 0f..1f,
                        onValueChange = {
                            value = it
                            val updatedDevice = device.copy(brightness = it)
                            onDeviceEvent(DeviceEvent.UpdateDevice(updatedDevice))
                        },
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
fun moodBoostBtn(
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
    appStateState: AppStateState,
    onAppStateEvent: (AppStateEvent) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors()
){
    var brightnessValue = appStateState.brightness
    val iconColor = calculateBackgroundColor(brightnessValue, Color.Black, Color(0xFFFCBA03))

    Slider(
        colors = colors,
        interactionSource = interactionSource,
        steps = steps,
        value = brightnessValue,
        valueRange = valueRange,
        enabled = enabled,
        thumb = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color.Gray)
                    .size(36.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.brightness_high),
                    contentDescription = "brightness Icon",
                    modifier = Modifier
                        .size(30.dp),
                    tint = Color(iconColor)
                )
            }
        },
        onValueChange = {
            brightnessValue = it
            val updatedAppState = appStateState.copy(brightness = brightnessValue)
            onAppStateEvent(AppStateEvent.UpdateAppState(updatedAppState))
        },
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

/*
Composable for a vertical slider controlling the overall temperature of all lights
The color of the slider thumb changes dynamically depending on the slider position
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalTemperatureSlider(
    appStateState: AppStateState,
    onAppStateEvent: (AppStateEvent) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors()
){
    var temperatureValue = appStateState.temperature
    val backgroundColor = calculateBackgroundColor(temperatureValue, Color(0xFF03BAFC), Color(0xFFFCBA03))

    Slider(
        colors = colors,
        interactionSource = interactionSource,
        steps = steps,
        valueRange = valueRange,
        enabled = enabled,
        value = temperatureValue,
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
                    contentDescription = "temperature Icon",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        },
        onValueChange = {
            temperatureValue = it
            val updatedAppState = appStateState.copy(temperature = temperatureValue)
            onAppStateEvent(AppStateEvent.UpdateAppState(updatedAppState))
        },
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
private fun calculateBackgroundColor(value: Float, color1: Color, color2: Color): Int {
    val color1Int = argb(
        (color1.alpha * 255).toInt(),
        (color1.red * 255).toInt(),
        (color1.green * 255).toInt(),
        (color1.blue * 255).toInt()
    )
    val color2Int = argb(
        (color2.alpha * 255).toInt(),
        (color2.red * 255).toInt(),
        (color2.green * 255).toInt(),
        (color2.blue * 255).toInt()
    )

    val ratio = value.coerceIn(0f, 1f)

    return ColorUtils.blendARGB(color1Int, color2Int, ratio)
}