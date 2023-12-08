@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.zenglow.views

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Brightness1
import androidx.compose.material.icons.outlined.BrightnessHigh
import androidx.compose.material.icons.outlined.BrightnessLow
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.zenglow.Screen
import com.example.zenglow.data.entities.Device
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.states.DeviceState
import com.github.skydoves.colorpicker.compose.*

/*
    DESCRIPTION:    DeviceConfigScreen
                    Screen for configuring a device, including the device's name, color, and brightness
*/
@Composable
fun DeviceConfigScreen(
    navController: NavController,
    state: DeviceState,
    onEvent: (DeviceEvent) -> Unit,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var deviceId = -1
    navBackStackEntry?.arguments?.getInt("deviceId")?.let {selectedDeviceId ->
        deviceId = selectedDeviceId
    } ?: run {
        deviceId = -1
    }
    val deviceById: Device = state.devices.find { it.deviceId == deviceId }
        ?:  Device(deviceId = -1, groupId = -1, color = 0xFFFFFF, temperature = 0.0f, brightness = 0.0f, displayName = "")

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { DeviceConfigTopBar(navController = navController) }
    ) { innerPadding ->
        Column( // Page content
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            DeviceConfigName(
                device = deviceById,
                onRenameButtonClick = { showDialog = true }
            )
            DeviceConfigEditPicker(
                device = deviceById,
                onDeviceEvent = onEvent
            )
            DeviceConfigDoneButton(navController = navController)
        }
        if (showDialog) {
            DeviceConfigRename(
                onDismissRequest = { showDialog = false },
                device = deviceById,
                onEvent = onEvent,
                state = state
            )
        }
    }
}


/*
    DESCRIPTION:    DeviceConfigScreen -> DeviceConfigTopBar
                    Top bar for the DeviceConfig screen, with a back button to return to the home screen
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceConfigTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Device Configuration",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    modifier = Modifier.size(32.dp),
                    contentDescription = "Return back to home-page"
                )
            }
        }
    )
}


/*
    DESCRIPTION:    DeviceConfigScreen -> DeviceConfigRename
                    Modal for renaming a device
*/
@Composable
fun DeviceConfigRename(
    onDismissRequest: () -> Unit,
    state: DeviceState,
    onEvent: (DeviceEvent) -> Unit,
    modifier: Modifier = Modifier,
    device: Device
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
//            modifier = Modifier
//                .size(width = 300.dp, height = 320.dp)
        ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var newName by remember { mutableStateOf("") }
                    TextField(
                        value = newName,
                        onValueChange = {
                            newName = it
                        },
                        placeholder = {
                            Text(text = "Device name")
                        }
                    )
                    Row {
                        Button(onClick = {
                            onDismissRequest()
                        }) {
                            Text(text="Cancel")
                        }
                        Button(onClick = {
                            val updatedDevice = device.copy(displayName = newName)
                            onEvent(DeviceEvent.UpdateDevice(updatedDevice))
                            onDismissRequest()
                        }) {
                            Text(text="Confirm")
                        }
                    }

                }
        }
    }
}


/*
    DESCRIPTION:    DeviceConfigScreen -> DeviceConfigDisplayName
                    Component for displaying the device's name with an edit button which opens a modal
*/
@Composable
fun DeviceConfigName(
    device: Device,
    onRenameButtonClick: () -> Unit
) {
    val deviceId = device.deviceId
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 0.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text =  device.displayName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.background,
            style = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 26.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp
            ),
            modifier = Modifier
                .padding(end = 10.dp)
        )
        IconButton(
            onClick = { onRenameButtonClick() }
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit device name",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}


/*
    DESCRIPTION:    DeviceConfigScreen -> DeviceConfigDoneButton
                    Component for displaying the done button
*/
@Composable
fun DeviceConfigDoneButton(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        FilledTonalButton(
            onClick = { navController.navigate(Screen.Home.route) },
        ) {
            Text(
                text = "Done",
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp, top = 0.dp, bottom = 5.dp),
            )
        }
    }
}



/*
    DESCRIPTION:    DeviceConfigScreen -> DeviceConfigEditPicker
                    Component for displaying the color picker and brightness/temperature sliders
*/
@Composable
fun DeviceConfigEditPicker(
    device: Device,
    onDeviceEvent: (DeviceEvent) -> Unit
) {
    val controller = rememberColorPickerController()

    // Database variables
    var color = device.color
    var brightness = device.brightness
    var temperature = device.temperature

    // Internal variables
    val pickerController = rememberColorPickerController()

    val interactionSource = remember { MutableInteractionSource() }
    val displayColor = colorConvert(pickerController.selectedColor.value, brightness, temperature)
    val updatedDevice = device.copy(color = colorHexInt(pickerController.selectedColor.value))
    onDeviceEvent(DeviceEvent.UpdateDevice(updatedDevice))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 30.dp,
                top = 16.dp,
                end = 30.dp,
            )
            .clip(shape = RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        /*
            Color picker
            - color picker for choosing a color
        */
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 15.dp,
                    bottom = 10.dp
                ),
            controller = pickerController,
            initialColor = Color(color),
            onColorChanged = {
                Log.d("Color", it.hexCode)

            }
        )

        /*
            Color picker display
            - displays the chosen color with hex code
        */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 30.dp)
            ) {
                Canvas(modifier = Modifier.size(60.dp)) {
                    scale(scaleX = 1f, scaleY = 1f) {
                        drawCircle(displayColor, radius = 30.dp.toPx())
                        drawCircle(Color.Black, radius = 30.dp.toPx(), style = Stroke(width = 1.dp.toPx())
                        )
                    }
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                    .width(120.dp)
                    .height(42.dp)
            ) {
                Text(
                    text = colorHex(displayColor),
                    modifier = Modifier.padding(10.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.W400,
                        fontSize = 20.sp,
                        lineHeight = 40.sp,
                        letterSpacing = 0.sp
                    )
                )
            }
        }

        /*
            Color picker buttons
            - presets for the color picker (white, gray, red, green, blue)
        */
        Text(
            text = "Color presets",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(
                    bottom = 20.dp,
                    start = 15.dp,
                    end = 15.dp,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(60.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(60.dp)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            pickerController.selectCenter(fromUser = false)
                            brightness = 1.0f
                            temperature = 0.0f
                        }
                ) {
                    scale(scaleX = 1f, scaleY = 1f) {
                        drawCircle(Color.White, radius = 25.dp.toPx())
                        drawCircle(Color.Black, radius = 25.dp.toPx(), style = Stroke(width = 1.dp.toPx()))
                    }
                }
            }
            Box(
                Modifier.size(60.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(60.dp)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            pickerController.selectCenter(fromUser = false)
                            brightness = 0.5f
                            temperature = 0.0f
                        }
                ) {
                    scale(scaleX = 1f, scaleY = 1f) {
                        drawCircle(Color.Gray, radius = 25.dp.toPx())
                        drawCircle(Color.Black, radius = 25.dp.toPx(), style = Stroke(width = 1.dp.toPx())
                        )
                    }
                }
            }
            Box(
                Modifier.size(60.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(60.dp)
                        .clickable(interactionSource = interactionSource, indication = null)
                        { controller.selectByCoordinate(x = 50f, y = 50f, fromUser = false) }
                ) {
                    scale(scaleX = 1f, scaleY = 1f) {
                        drawCircle(Color.Red, radius = 25.dp.toPx())
                        drawCircle(Color.Black, radius = 25.dp.toPx(), style = Stroke(width = 1.dp.toPx())
                        )
                    }
                }
            }
            Box(
                Modifier.size(60.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(60.dp)
                        .clickable(interactionSource = interactionSource, indication = null)
                        { controller.selectByCoordinate(x = 50f, y = 50f, fromUser = false) }
                ) {
                    scale(scaleX = 1f, scaleY = 1f) {
                        drawCircle(Color.Green, radius = 25.dp.toPx(),)
                        drawCircle(Color.Black, radius = 25.dp.toPx(), style = Stroke(width = 1.dp.toPx())
                        )
                    }
                }
            }
            Box(
                Modifier.size(60.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(60.dp)
                        .clickable(interactionSource = interactionSource, indication = null)
                        { controller.selectByCoordinate(x = 50f, y = 50f, fromUser = false) }
                ) {
                    scale(scaleX = 1f, scaleY = 1f) {
                        drawCircle(Color.Blue, radius = 25.dp.toPx())
                        drawCircle(Color.Black, radius = 25.dp.toPx(), style = Stroke(width = 1.dp.toPx())
                        )
                    }
                }
            }
        }

        /*
            Brightness slider
            - slider for choosing the brightness of the color
        */
        Text(
            text = "Brightness",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 10.dp
                ),
        ) {
            Icon(
                imageVector = Icons.Outlined.BrightnessLow,
                contentDescription = "Low brightness",
            )
            Slider(
                value = brightness,
                onValueChange = {
                    brightness = it
                    val updatedDevice = device.copy(brightness = it)
                    onDeviceEvent(DeviceEvent.UpdateDevice(updatedDevice))
                },
                modifier = Modifier
                    .width(250.dp)
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                    )
            )
            Icon(
                imageVector = Icons.Outlined.BrightnessHigh,
                contentDescription = "High brightness"
            )
        }

        /*
            Temperature slider
            - slider for choosing the temperature of the color
        */
        Text(
            text = "Temperature filter",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 10.dp,
                ),
        ) {
            Icon(
                imageVector = Icons.Outlined.Brightness1,
                contentDescription = "Return back to home-page",
            )
            Slider(
                value = temperature,
                onValueChange = {
                    temperature = it
                    val updatedDevice = device.copy(temperature = it)
                    onDeviceEvent(DeviceEvent.UpdateDevice(updatedDevice))
                },
                modifier = Modifier
                    .width(250.dp)
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                    )
            )
            Icon(
                imageVector = Icons.Outlined.WbSunny,
                contentDescription = "Return back to home-page",
            )
        }
    }
}


/*
    DESCRIPTION:    [colorHex]
                    Function takes in a Color and returns a hex of the color
*/
fun colorHex(color: Color): String {
    val red = (color.red * 255).toInt()
    val green = (color.green * 255).toInt()
    val blue = (color.blue * 255).toInt()

    return String.format("#%02X%02X%02X", red, green, blue)
}

fun colorHexInt(color: Color): Int {
    val red = (color.red * 255).toInt()
    val green = (color.green * 255).toInt()
    val blue = (color.blue * 255).toInt()

    return (red shl 16) or (green shl 8) or blue
}


/*
    DESCRIPTION:    colorBrightness
                    Function takes in a Color and returns the brightness of the color
*/
fun colorBrightness(color: Color): Double {
    val red = color.red / 255.0
    val green = color.green / 255.0
    val blue = color.blue / 255.0
    val brightness = maxOf(red, green, blue) * 100.0

    return (brightness / 100.0) * 255.0
}


/*
    DESCRIPTION:    colorConvert
                    Function takes in a Hue, Brightness, and Temperature and returns a Color
*/
@Composable
fun colorConvert(hue: Color, brightness: Float, temperature: Float): Color {

    // Scale the colors based on the brightness
    val scaledRed = hue.red * brightness
    val scaledGreen = hue.green * brightness
    val scaledBlue = hue.blue * brightness

    // Set the target color based on the temperature
    val warmColor = Color(255, 197, 143) // Warm color

    // Scale the colors based on the temperature
    val red = interpolateColor(scaledRed, warmColor.red, temperature)
    val green = interpolateColor(scaledGreen, warmColor.green, temperature)
    val blue = interpolateColor(scaledBlue, warmColor.blue, temperature)

    // Create a new Color object with the scaled components
    return Color(red = red, green = green, blue = blue)
}


/*
    DESCRIPTION:    interpolateColor
                    Function takes in a start, end, and t value and returns a float (interpolated color), function is used in colorConvert
*/
fun interpolateColor(start: Float, end: Float, t: Float): Float {
    val adjustedT = t * 0.7f
    val smoothedT = adjustedT * adjustedT * (3f - 2f * adjustedT)

    return start + (end - start) * smoothedT
}