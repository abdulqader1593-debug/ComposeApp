package com.example.composeapp.data.remote

import com.example.composeapp.data.model.Asset
import retrofit2.http.GET

interface StockApi {
    @GET("api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1")
    suspend fun fetchAssets(): List<Asset>
}