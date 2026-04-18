package com.example.wewatch.data.omdb

import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("y") year: String? = null
    ): OmdbSearchResponse
}