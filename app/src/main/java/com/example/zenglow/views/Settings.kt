@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.zenglow.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.zenglow.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    DESCRIPTION: TODO add description
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
                    imageVector = Icons.Filled.KeyboardArrowLeft,
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
    DESCRIPTION: TODO add description
*/
@Composable
fun MinimalDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 240.dp, height = 200.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Elevated",
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Card",
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "This is a card with elevation",
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = { onDismissRequest() }, // Close the dialog
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}


/*
    DESCRIPTION: TODO add description
*/
@Composable
fun SettingsScrollContent(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xEC, 0xEC, 0xEC)),
        contentAlignment = Alignment.Center
    ) {
        NotificationListItem()
        ConnectionListItem()
    }
}


/*
 DESCRIPTION:   Settings -> Notification
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
            .padding(16.dp)
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
 DESCRIPTION:   Settings -> Connection
                List item with a drop-down menu to select a connection type

 TODO:          - (ListItem->supportingContent), the text should be read from database.
                - SegmentedButton->selected should update/linked the database from database.
                - (var typeConnection) should be read/linked to database.
 */
@Composable
fun ConnectionListItem() {
    var toggleConnection by remember { mutableStateOf(value = false) }
    var typeConnection by remember { mutableStateOf(0) }            // TODO (should be read from database)
    val optionsConnection = listOf("Wifi", "Bluethooth")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.background),

        ) {

        ListItem(
            headlineContent = { Text(text = "Notifications") },
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
                    Icons.Filled.Notifications,
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