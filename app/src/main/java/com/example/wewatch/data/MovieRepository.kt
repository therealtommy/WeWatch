package com.example.wewatch.data

import com.example.wewatch.data.omdb.OmdbMovie
import com.example.wewatch.data.omdb.RetrofitClient
import com.example.wewatch.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: MovieDao) {

    fun getWatchlist(): Flow<List<MovieEntity>> = dao.getAll()

    suspend fun addMovie(movie: MovieEntity) = dao.insert(movie)

    suspend fun deleteMovies(ids: List<String>) = dao.deleteByIds(ids)

    suspend fun searchMovies(query: String, year: String?): List<OmdbMovie> {
        val apiKey = "174065d5"
        val response = RetrofitClient.api.searchMovies(apiKey, query, year)
        return if (response.response == "True") response.search ?: emptyList()
        else emptyList()
    }
}