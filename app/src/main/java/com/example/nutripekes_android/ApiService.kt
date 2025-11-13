package com.example.nutripekes_android

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://nutripekes-api.vercel.app/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("info")
    suspend fun getInfo(): InfoResponse

    @GET("recetas")
    suspend fun getRecipes(): List<RecipeApiResponseItem>
}

object ApiClient {
    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
