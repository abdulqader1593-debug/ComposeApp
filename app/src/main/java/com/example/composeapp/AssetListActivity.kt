package com.example.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeapp.ui.assetList.AssetListItem
import com.example.composeapp.ui.assetList.AssetViewModel
import com.example.composeapp.ui.theme.ComposeAppTheme

class AssetListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SimpleAssetListScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleAssetListScreen(
    viewModel: AssetViewModel = viewModel()
) {
    // Directly collect assetList state without search bar filtering UI
    val assetList by viewModel.filteredAssets.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(
            items = assetList,
            key = { asset -> asset.id }
        ) { asset ->
            AssetListItem(asset = asset)
        }
    }
}
