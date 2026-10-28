package com.example.composeapp.data.model

import com.google.gson.annotations.SerializedName

data class Asset(
    @SerializedName("id")
    val id:String,
    @SerializedName("name")
    val name:String,
    @SerializedName("symbol")
    val type:AssetType?,
    @SerializedName("current_price")
    val price:String
)