package com.example.wewatch.data

import com.example.wewatch.entity.MovieEntity

class MovieRepository(private val dao: MovieDao) {
    suspend fun getAllWatchlist() = dao.getAll()
    suspend fun addToWatchlist(movie: MovieEntity) = dao.insert(movie)
    suspend fun removeFromWatchlist(movie: MovieEntity) = dao.delete(movie)
    suspend fun removeMultiple(ids: List<String>) = dao.deleteByIds(ids)
}