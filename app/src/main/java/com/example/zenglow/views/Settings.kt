@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.zenglow.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.zenglow.R
import com.example.zenglow.Screen


/*
    DESCRIPTION: SettingsScreen
                 Screen for the settings options of the app.
*/
@Composable
fun SettingsScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(value = false) }

    Scaffold(
        topBar = {
            SettingsTopBar(
                navController = navController,
                onInfoButtonClick = { showDialog = true }
            )
        }
    ) { innerPadding ->
        SettingsScrollContent(innerPadding)

        if (showDialog) {
            MinimalDialog(onDismissRequest = { showDialog = false })
        }
    }
}


/*
    DESCRIPTION: SettingsScreen -> TopBar
                 Top bar for the settings screen.
*/
@Composable
fun SettingsTopBar(navController: NavController, onInfoButtonClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Settings",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
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
        },
        actions = {
            IconButton(onClick = { onInfoButtonClick() }) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    modifier = Modifier.size(28.dp),
                    contentDescription = "ZenGlow app information"
                )
            }
        }
    )
}


/*
    DESCRIPTION:    SettingsScreen -> Dialog
                    Dialog for the settings screen, which is trigger by SettingsTopBar,
                    when the info button is clicked.

    TODO:           Fix Text() text, values if needed.
*/
@Composable
fun MinimalDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 300.dp, height = 320.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(   // Headline
                    text = "ZenGlow",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
                Text(   // SubHeadline
                    text = "Version 0.3.0-Pre-Alpha",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                Text(   // MainBody
                    text ="\nApp for controlling smartLED devices, based on your mood. Implemented for ITU-Project (BUT-FIT).",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
                Text(   // OtherBody Headline
                    text = "\nCreated by:",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(   // OtherBody
                    text = " - Nikolas Nosál (xnosal01)\n" +
                            " - Jakub Brnák (xbrnak??)\n" +
                            " - Daniel Blaško (xblask??)\n",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Left,
                )
                TextButton(
                    onClick = { onDismissRequest() }, // Close the dialog
                    modifier = Modifier
                        .align(Alignment.End),
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}


/*
    DESCRIPTION:    SettingsScreen -> ScrollContent
                    Scrollable content for the settings screen.
*/
@Composable
fun SettingsScrollContent(innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(innerPadding)
    ) {
        item {
            NotificationListItem()
        }
        item {
            ConnectionListItem()
        }
    }
}


/*
 DESCRIPTION:   SettingsScreen -> ScrollContent -> Notification
                List item with a drop-down menu to turn on/off notifications

 TODO:          - (ListItem->supportingContent), the text should be read from database.
                - SegmentedButton->selected should update/linked the database from database.
                - (var typeConnection) should be read/linked to database.
 */
@Composable
fun NotificationListItem() {
    var toggleNotification by remember { mutableStateOf(value = false) }
    var typeNotification by remember { mutableStateOf(value = 0) }                 // TODO (should be read from database)
    val optionsNotification = listOf("ON", "OFF")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                top = 16.dp,
                end = 24.dp,
            )
            .clip(shape = RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.background),

        ) {

        ListItem(
            headlineContent = { Text(text = "Notifications") },
            supportingContent = { Text(text = "(not implemented)") },            // TODO (should be read from database)
            trailingContent = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "DropDownNotification",
                    modifier = Modifier
                        .rotate(degrees = if (toggleNotification) 0f else -90f)
                        .clickable { toggleNotification = !toggleNotification }
                )
            },
            leadingContent = {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = "SettingsNotification",
                )
            }
        )

        AnimatedVisibility(
            toggleNotification,
            enter = expandVertically(expandFrom = Alignment.Top) { 30 },
            exit = shrinkVertically(animationSpec = tween()) { fullHeight ->
                fullHeight / 2
            },
        ) {
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
        }
    }
}


/*
 DESCRIPTION:   SettingsScreen -> ScrollContent -> Connection
                List item with a drop-down menu to select a connection type

 TODO:          - (ListItem->supportingContent), the text should be read from database.
                - SegmentedButton->selected should update/linked the database from database.
                - (var typeConnection) should be read/linked to database.
 */
@Composable
fun ConnectionListItem() {
    var toggleConnection by remember { mutableStateOf(value = false) }
    var typeConnection by remember { mutableStateOf(0) }            // TODO (should be read from database)
    val optionsConnection = listOf("Wifi", "Bluetooth")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                top = 16.dp,
                end = 24.dp,
            )
            .clip(shape = RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.background),

        ) {

        ListItem(
            headlineContent = { Text(text = "Connection") },
            supportingContent = { Text(text = "(not implemented)") },       // TODO (should be read from database)
            trailingContent = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "DropDownConnection",
                    modifier = Modifier
                        .rotate(degrees = if (toggleConnection) 0f else -90f)
                        .clickable { toggleConnection = !toggleConnection }
                )
            },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.outline_wifi_tethering_24),
                    contentDescription = "TestSetting",
                )
            }
        )

        AnimatedVisibility(
            toggleConnection,
            enter = expandVertically(expandFrom = Alignment.Top) { 30 },
            exit = shrinkVertically(animationSpec = tween()) { fullHeight ->
                fullHeight / 2
            },
        ) {
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
                optionsConnection.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = optionsConnection.size),
                        onClick = { typeConnection = index },
                        selected = index == typeConnection              // TODO (should update/linked to database)
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}