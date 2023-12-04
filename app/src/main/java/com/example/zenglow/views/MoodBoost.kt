@file:OptIn(ExperimentalMaterial3Api::class)

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenglow.R
import com.example.zenglow.RenameDialog
import com.example.zenglow.Screen
import com.example.zenglow.events.GroupEvent
import com.example.zenglow.views.GroupDeviceItem
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MoodBoostScreen(navController: NavController) {

    Scaffold(
        topBar = { MoodBoostTopBar { navController.navigateUp() } },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Add mood click action */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Mood Status")
            }
        }
    ) {
        innerPadding -> MainScrollContent(innerPadding)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScrollContent(innerPadding: PaddingValues){
    val pagerState = rememberPagerState(pageCount = {
        4
    })

    val imageList = listOf(R.drawable.mood_working3, R.drawable.mood_sleep, R.drawable.mood_working3, R.drawable.mood_sleep)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xEC, 0xEC, 0xEC)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(start = 46.dp, end = 24.dp, top = 50.dp)
        ) {page->
            Card(
                Modifier
                    .width(300.dp)
                    .height(200.dp)
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
                Image(
                    painter = painterResource(id = imageList[page]),
                    contentDescription = "Description for accessibility",
                    contentScale = ContentScale.Crop // Adjust scaling as needed
                )

            }

        }

    }
}

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




@Composable
fun MoodFactors() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MoodFactor(icon = Icons.Filled.Favorite, label = "Heart Rate", value = "72 bpm")
        MoodFactor(icon = Icons.Outlined.Build, label = "Time", value = "2h 30m")
        MoodFactor(icon = Icons.Outlined.CheckCircle, label = "Temperature", value = "21°C")
    }
}

@Composable
fun MoodFactor(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label)
        Text(value)
    }
}


