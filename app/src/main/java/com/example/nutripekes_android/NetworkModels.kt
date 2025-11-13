package com.example.nutripekes_android

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InfoResponse(
    @Json(name = "info") val info: List<InfoItem>
)

@JsonClass(generateAdapter = true)
data class InfoItem(
    @Json(name = "pk") val pk: String,
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: List<List<String>>,
    @Json(name = "color") val color: String
)

@JsonClass(generateAdapter = true)
data class RecipeApiResponseItem(
    @Json(name = "pk") val pk: String,
    @Json(name = "name") val name: String,
    @Json(name = "ingredients") val ingredients: List<String>,
    @Json(name = "instructions") val instructions: String,
    @Json(name = "img_url") val image: String
)