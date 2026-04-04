package com.example.wewatch.data.omdb

// network/OmdbApi.kt
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("y") year: String? = null,
        @Query("apikey") apiKey: String
    ): OmdbSearchResponse

    @GET("/")
    suspend fun getById(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String
    ): OmdbMovie // можно расширить поля
}