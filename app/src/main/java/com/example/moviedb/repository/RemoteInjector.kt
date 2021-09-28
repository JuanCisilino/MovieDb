package com.example.moviedb.repository

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteInjector {

    const val API_ENDPOINT = "https://api.themoviedb.org/3/"

    fun injectDoggoApiService(retrofit: Retrofit = getRetrofit()): RepoMovies {
        return retrofit.create(RepoMovies::class.java)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

}