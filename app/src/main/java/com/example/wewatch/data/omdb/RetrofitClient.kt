package com.example.wewatch.data.omdb

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// network.RetrofitClient.kt
object RetrofitClient {
    private const val BASE_URL = "https://www.omdbapi.com/"
    private const val API_KEY = "174065d5"

    val api: OmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbApi::class.java)
    }
}