package com.example.moviedb.repository

import com.example.moviedb.model.MovieList
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoMovies {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apikey: String,
                                 @Query("language") lenguaje: String,
                                 @Query("page") page: Int): MovieList


}