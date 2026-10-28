package com.example.composeapp.ui.assetList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.data.model.Asset
import com.example.composeapp.data.model.AssetType
import com.example.composeapp.data.remote.StockApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

class AssetViewModel : ViewModel(){
    private val stockApi : StockApi = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(StockApi::class.java)

    private val _assets = MutableStateFlow<List<Asset>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredAssets = combine(_assets, _searchQuery) { assets, query ->
        if (query.isBlank()) {
            assets
        } else {
            assets.filter { asset ->
                asset.name.contains(query, ignoreCase = true) ||
                        (asset.type?.displayName?.contains(query, ignoreCase = true) == true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),emptyList())

    fun onSearchQueryChanged(newQuery: String){
        _searchQuery.value = newQuery
    }

    init {
        fetchLiveAssetsFromServer()
    }

    private fun fetchLiveAssetsFromServer(){
        viewModelScope.launch {
            try {
                val responseList = stockApi.fetchAssets()
                _assets.value = responseList
            }catch (e:Exception){
               _assets.value = listOf(
                   Asset(
                       "err",
                       "Failed to load server data",
                       AssetType.STOCK,
                       "$0.00"
                   )
               )
            }
        }
    }
}