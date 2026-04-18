package com.example.wewatch.data

import androidx.room.*
import com.example.wewatch.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM watchlist ORDER BY title ASC")
    fun getAll(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Query("DELETE FROM watchlist WHERE imdbID IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)
}