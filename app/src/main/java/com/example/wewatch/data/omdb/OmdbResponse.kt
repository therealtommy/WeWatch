package com.example.wewatch.data.omdb

import com.google.gson.annotations.SerializedName

data class OmdbSearchResponse(
    @SerializedName("Search")
    val search: List<OmdbMovie>?,
    @SerializedName("totalResults")
    val totalResults: String?,
    @SerializedName("Response")
    val response: String,
    @SerializedName("Error")
    val error: String?
)

data class OmdbMovie(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("Poster")
    val posterUrl: String,
    @SerializedName("Type")
    val type: String? = null
)