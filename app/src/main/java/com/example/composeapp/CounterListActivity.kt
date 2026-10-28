package com.example.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapp.ui.theme.ComposeAppTheme
import kotlinx.coroutines.delay

class CounterListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CounterListScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CounterListScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = Int.MAX_VALUE,
            key = { index -> index },       // 1. Stable key prevents flickering
            contentType = { "CounterRow" } // 2. ContentType helps LazyColumn reuse item compositions efficiently
        ) { index ->
            CounterListItem(itemIndex = index)
        }
    }
}

@Composable
fun CounterListItem(
    itemIndex: Int,
    modifier: Modifier = Modifier
) {
    // 1. Store initial start timestamp and accumulated elapsed milliseconds
    var startTimeMillis by rememberSaveable { mutableLongStateOf(0L) }
    var elapsedTimeMs by rememberSaveable { mutableLongStateOf(0L) }
    var isRunning by rememberSaveable { mutableStateOf(false) }

    // 2. State for triggering 1-second recomposition ticks
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Auto-start timer when this row appears on screen for the first time
    LaunchedEffect(Unit) {
        if (startTimeMillis == 0L && !isRunning) {
            startTimeMillis = System.currentTimeMillis()
            isRunning = true
        }
    }

    // Ticker coroutine that updates `now` every 1 second while running
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            now = System.currentTimeMillis()
        }
    }

    // Calculate current display seconds based on System.currentTimeMillis()
    val displaySeconds = if (isRunning && startTimeMillis > 0L) {
        ((now - startTimeMillis) / 1000).coerceAtLeast(0L)
    } else {
        (elapsedTimeMs / 1000).coerceAtLeast(0L)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Item #$itemIndex",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Time: ${displaySeconds}s",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Start / Stop (Pause) Button
            if (isRunning) {
                OutlinedButton(
                    onClick = {
                        // Stop/Pause: Save current elapsed time and set isRunning = false
                        elapsedTimeMs = System.currentTimeMillis() - startTimeMillis
                        isRunning = false
                    }
                ) {
                    Text(text = "Stop")
                }
            } else {
                Button(
                    onClick = {
                        // Start/Resume: Adjust startTimeMillis relative to saved elapsedTimeMs
                        startTimeMillis = System.currentTimeMillis() - elapsedTimeMs
                        now = System.currentTimeMillis()
                        isRunning = true
                    }
                ) {
                    Text(text = "Start")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterListScreenPreview() {
    ComposeAppTheme {
        CounterListScreen()
    }
}
