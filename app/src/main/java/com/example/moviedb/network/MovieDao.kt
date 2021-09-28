package com.example.moviedb.network

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedb.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieList: List<Movie>)

    @Query("SELECT * FROM movie")
    fun getMoviesFromDb(): PagingSource<Int, Movie>

    @Query("SELECT * FROM movie")
    fun getMovieListFromDb(): List<Movie>

    @Query("DELETE FROM movie")
    suspend fun clearAllMovies()
}