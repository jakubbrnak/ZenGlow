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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditMoodScreen(navController: NavController) {

    Scaffold(
        topBar = { MoodBoostTopBar { navController.navigateUp() } },

        ) {
            innerPadding -> MainScrollContent(innerPadding)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScrollContent(innerPadding: PaddingValues){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xEC, 0xEC, 0xEC)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SliderBox("Stress Index")
        SliderBox("Physical energy")
        RadioButtonGroup()
    }
}

@Composable
fun RadioButtonGroup() {
    // State to hold the index of the currently selected radio button
    var selectedOption by remember { mutableStateOf(0) }

    Card(
        Modifier
            .width(340.dp)
            .height(120.dp)
            .padding(top = 40.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){


        Row(Modifier
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

        Row(Modifier
            .selectableGroup()
            .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom

            )
            {
            // Create 5 radio buttons
            for (index in 0 until 5) {
                RadioButton(
                    selected = selectedOption == index,
                    onClick = { selectedOption = index },
                    modifier = Modifier.semantics { contentDescription = "Option ${index + 1}" }
                )
            }
        }
    }
}


@Composable
fun SliderBox(
    sliderText: String
){
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
                text = sliderText,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
            Row(
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
                Text(text = "LOW")
                SliderMinimalExample()
                Text(text = "HIGH")
            }
        }
    }
}



@Composable
fun SliderMinimalExample() {
    var sliderPosition by remember { mutableFloatStateOf(0f)}
    Column {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            modifier = Modifier
                .width(240.dp)
        )
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





