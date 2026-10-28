package com.example.composeapp.ui.assetList

import android.widget.GridLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AssetListScreen(viewModel: AssetViewModel){
    val searchQuery by viewModel.searchQuery.collectAsState()
    val assetList by viewModel.filteredAssets.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)){
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {viewModel.onSearchQueryChanged(it)},
            label = { Text("Search by name or type...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = assetList, key = {it.id}){ asset ->
                AssetListItem(asset = asset)
            }
        }
    }
}
