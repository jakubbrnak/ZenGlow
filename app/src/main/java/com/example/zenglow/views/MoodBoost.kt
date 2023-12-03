package com.example.zenglow.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zenglow.Screen
import com.example.zenglow.Screen.Home.route
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodBoostScreen(
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarText = "ZenGlow 0.1"
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {snackbarData: SnackbarData ->
                    ElevatedCard(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable{}
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Filled.Info, contentDescription = "")
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = MaterialTheme.typography.bodyLarge.fontSize)) {
                                        append("ZenGlow\n")
                                    }
                                },
                                textAlign = TextAlign.Center
                            )
                            Text(text = "ZenGlow Alpha 0.2", textAlign = TextAlign.Center
                            )
                        }
                    }

                }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Mood Boost")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(snackbarText)
                        }
                    }) {
                        Icon (
                            imageVector = Icons.Filled.Info,
                            contentDescription = "App info"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        MoodBoostScrollContent(innerPadding)
    }
}

@Composable
fun MoodBoostScrollContent(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xEC, 0xEC, 0xEC)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Settings",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = MaterialTheme.typography.displayLarge.fontSize
        )
    }
}