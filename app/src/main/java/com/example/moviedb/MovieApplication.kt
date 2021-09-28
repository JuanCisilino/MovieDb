package com.example.moviedb

import android.app.Application
import com.example.moviedb.network.AppDatabase
import com.example.moviedb.network.LocalInjector

class MovieApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        LocalInjector.appDatabase = AppDatabase.getInstance(this@MovieApplication)
    }
}