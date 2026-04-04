package com.example.wewatch.data.omdb

data class OmdbSearchResponse(
    val Search: List<OmdbMovie>?,
    val totalResults: String,
    val Response: String,
    val Error: String?
)

data class OmdbMovie(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String,
    val Genre: String? = null
)