@file:OptIn(ExperimentalMaterial3Api::class)

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenglow.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Types.NULL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MoodBoostScreen(navController: NavController) {

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
        Text(
            "Current Mood",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.Start) // Aligns the text to the start within the column
                .padding(start = 16.dp, top = 20.dp)  // Adds padding to the start
        )
            ImagePager()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Suggested Mood",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.Start) // Aligns the text to the start within the column
                .padding(start = 16.dp, top = 10.dp)  // Adds padding to the start
        )
        OverallScore()

        SuggestedMood()


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagePager(){
    val pagerState = rememberPagerState(pageCount = {
        4
    })
    val imageList = listOf(R.drawable.mood_working3, R.drawable.mood_sleep, R.drawable.mood_working3, R.drawable.mood_sleep)
    val moodList = listOf("Working", "Sleep", "Sport", "Chill")
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(start = 46.dp, end = 24.dp, top = 20.dp)
    ) {page->
        Card(
            Modifier
                .width(330.dp)
                .height(220.dp)
                .graphicsLayer {
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue
                }
        ) {
            Box {
                Image(
                    painter = painterResource(id = imageList[page]),
                    contentDescription = "Description for accessibility",
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = moodList[page],
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 30.dp, bottom = 16.dp)// Position the text
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
fun OverallScore(){
    Column(
        modifier = Modifier
            .padding(top = 10.dp)
            .background(Color(0xEC, 0xEC, 0xEC)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            Modifier
                .width(340.dp)
                .height(250.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = "Overall score",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.W300,
                    fontSize = 25.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp
                )
                Text(
                    text = "54",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.W300,
                    fontSize = 90.sp
                )

                val databaseValue by remember { mutableStateOf(0.75f) }
                DatabaseProgressIndicator(databaseProgress = databaseValue)
                Row(modifier = Modifier
                    .padding(top = 30.dp)){
                    TimeDisplay()
                    EditButtonExample(onClick = {})
                }



            }
        }
    }
}



@Composable
fun EditButtonExample(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.CenterEnd, // Aligns children to the center-end
        modifier = Modifier
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .padding(end = 10.dp, start = 10.dp)
                .width(170.dp)
                .height(40.dp)
        ) {
            Text("Edit Mood Factors",
                fontSize = 14.sp)
        }
    }
}


@Preview
@Composable
fun TimeDisplay() {
    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    // Update the time every second
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getCurrentTime()
            delay(1000) // Wait for a second
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.AccessTime, // Material design time icon
            contentDescription = "Time Icon",
            modifier = Modifier.size(24.dp) // Icon size
        )
        Spacer(modifier = Modifier.width(6.dp)) // Space between icon and text
        Text(text = "$currentTime",
                fontSize = 26.sp)
        Spacer(modifier = Modifier.width(3.dp))
    }
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date())
}
@Preview
@Composable
fun SuggestedMood() {
    Column(
        modifier = Modifier
            .padding(top = 10.dp)
            .background(Color(0xEC, 0xEC, 0xEC)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .width(340.dp)
                .height(80.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically, // Center the content vertically in the row
                horizontalArrangement = Arrangement.SpaceBetween // Space between the text and button
            ) {

                Spacer(modifier = Modifier.width(8.dp))
                Text("Sleeping", style = MaterialTheme.typography.titleMedium,)


                FilledButtonExample(onClick = {}) // Button to the right
            }
        }
    }
}


@Composable
fun FilledButtonExample(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.CenterEnd, // Aligns children to the center-end
        modifier = Modifier
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .padding(end = 10.dp)
                .width(110.dp)
                .height(40.dp)
        ) {
            Text("Apply",
                fontSize = 15.sp)
        }
    }
}


@Composable
fun DatabaseProgressIndicator(databaseProgress: Float) {
    LinearProgressIndicator(
        progress = {
            databaseProgress // Use the database value directly
        },
        modifier = Modifier.fillMaxWidth(),
    )
}
