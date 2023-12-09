/*
 FILE: DeviceConfig.kt
 AUTHOR: Nikolas Nosál <xnosal01>
 PARTICIPATION: fun colorConvert() -> Daniel Blaško <xblask05>, Nikolas Nosál <xnosal01>

 DESCRIPTION: Application device configuration screen, used for settings the device's name,
              color, brightness, and temperature, as well as deleting the device
*/
@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("DEPRECATION")

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.zenglow.Screen
import com.example.zenglow.data.entities.Device
import com.example.zenglow.data.entities.Group
import com.example.zenglow.events.DeviceEvent
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.states.DeviceState
import com.example.zenglow.states.GroupState
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

    var renameDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { DeviceConfigTopBar(navController = navController) }
    ) { innerPadding ->
        Column( // Page content
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.inverseSurface),
        ) {
            DeviceConfigName(
                device = deviceById,
                onRenameButtonClick = { renameDialog = true },
                onDeleteButtonClick = { deleteDialog = true }
            )
            DeviceConfigEditPicker(
                device = deviceById,
                onDeviceEvent = onEvent
            )
            DeviceConfigDoneButton(navController = navController)
        }
        if (renameDialog) {
            DeviceConfigRename(
                onDismissRequest = { renameDialog = false },
                device = deviceById,
                onEvent = onEvent,
                state = state
            )
        }
        if(deleteDialog) {
            DeviceConfigDelete(
                onDismissRequest = { deleteDialog = false },
                device = deviceById,
                onEvent = onEvent,
                state = state,
                navController = navController
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
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    modifier = Modifier.size(36.dp),
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
            modifier = Modifier
                .size(width = 320.dp, height = 220.dp)
        ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface)
                ) {


                    // Title
                    Text(
                        text = "Rename the device",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                    )

                    /* Text field for entering a new name */
                    var newName by remember { mutableStateOf("") }
                    TextField(
                        value = newName,
                        onValueChange = { newName = it },
                        placeholder = { Text(text = "Device name") },
                        shape = RoundedCornerShape(25.dp),
                        singleLine = false,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .padding(bottom = 40.dp, start = 20.dp, end = 20.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    )

                    /* Cancel/Confirm buttons */
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End)
                            .padding(bottom = 10.dp),
                    ) {
                        TextButton(onClick = { onDismissRequest() }
                        ){
                            Text(
                                text="Cancel",
                                modifier.padding(end = 10.dp),
                            )
                        }

                        TextButton(onClick = {
                            val updatedDevice = device.copy(displayName = newName)
                            onEvent(DeviceEvent.UpdateDevice(updatedDevice))
                            onDismissRequest()
                        }) {
                            Text(
                                text="Confirm",
                                modifier.padding(end = 10.dp),
                            )
                        }
                    }

                }
        }
    }
}


/*
    DESCRIPTION:    DeviceConfigScreen -> DeviceConfigDelete
                    Modal for confirming the deletion of a device
*/
@Composable
fun DeviceConfigDelete(
    onDismissRequest: () -> Unit,
    state: DeviceState,
    onEvent: (DeviceEvent) -> Unit,
    modifier: Modifier = Modifier,
    device: Device,
    navController: NavController
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 320.dp, height = 180.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
            ) {


                // Title
                Text(
                    text = "Do you want to delete this device?",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 30.dp)
                )

                /* Cancel/Confirm buttons */
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End),
                ) {
                    TextButton(onClick = { onDismissRequest() }
                    ){
                        Text(
                            text="Cancel",
                            modifier.padding(end = 10.dp),
                        )
                    }

                    TextButton(onClick = {
                        onEvent(DeviceEvent.DeleteDevice(device))
                        onDismissRequest()
                        navController.navigate(Screen.Home.route)
                    }) {
                        Text(
                            text="Confirm",
                            modifier.padding(end = 10.dp),
                        )
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
    onRenameButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit
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
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 26.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp
            ),
            modifier = Modifier
                .padding(end = 30.dp)
        )
        IconButton(
            onClick = { onRenameButtonClick() }
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit device name",
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = 5.dp)
            )
        }
        IconButton(
            onClick = { onDeleteButtonClick() }
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete device",
                modifier = Modifier.size(30.dp)
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
        Button(
            onClick = { navController.navigate(Screen.Home.route) },
        ) {
            Text(
                text = "Done",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .padding(start = 40.dp, end = 40.dp),
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


        /*
        Helpful buttons (reset color), (reset filters)
        - buttons for resetting the color and filters
        */
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { pickerController.selectCenter(fromUser = false) },
                modifier = Modifier.padding(end = 20.dp)
            ) {
                Text(
                    text = "Reset color",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            OutlinedButton(
                onClick = {
                    with(device.copy(temperature = 0.0f, brightness = 1.0f)) {
                        onDeviceEvent(DeviceEvent.UpdateDevice(this))
                    } },
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Text(
                    text = "Reset filters",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
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

    NOTE:           This function is also used in partly used in HomeScreen.kt
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