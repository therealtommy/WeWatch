package com.example.wewatch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.wewatch.entity.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM watchlist")
    suspend fun getAll(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Query("DELETE FROM watchlist WHERE imdbID IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)
}