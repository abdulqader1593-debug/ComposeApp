package com.example.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapp.ui.theme.ComposeAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AutoScrollPagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AutoScrollPagerScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AutoScrollPagerScreen(modifier: Modifier = Modifier) {
    val pageCount = 3

    val pagerState = rememberPagerState(pageCount = { pageCount })


    val coroutineScope = rememberCoroutineScope()


    var isAutoScrollEnabled by remember { mutableStateOf(false) }


    LaunchedEffect(isAutoScrollEnabled) {
        while (isAutoScrollEnabled) {
            delay(2000L)
            val nextPage = (pagerState.currentPage + 1) % pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Auto Scroll",
                style = MaterialTheme.typography.titleMedium
            )
            Switch(
                checked = isAutoScrollEnabled,
                onCheckedChange = { isAutoScrollEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { pageIndex ->
            PagerItemCard(pageIndex = pageIndex)
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until pageCount) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(i)
                        }
                    }
                ) {
                    Text(text = "Page ${i + 1}")
                }
            }
        }
    }
}

@Composable
fun PagerItemCard(pageIndex: Int) {
    val colors = listOf(
        Color(0xFFE57373),
        Color(0xFF81C784),
        Color(0xFF64B5F6)
    )

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors[pageIndex % colors.size]
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Card Page #${pageIndex + 1}",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AutoScrollPagerScreenPreview() {
    ComposeAppTheme {
        AutoScrollPagerScreen()
    }
}
