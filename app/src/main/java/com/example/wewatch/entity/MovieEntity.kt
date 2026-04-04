package com.example.wewatch.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class MovieEntity(
    @PrimaryKey
    val imdbID: String,
    val title: String,
    val year: String,
    val posterUrl: String?,
    val genre: String? // для SearchScreen не используется, но можно сохранить
)