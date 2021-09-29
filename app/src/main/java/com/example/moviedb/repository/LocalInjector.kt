package com.example.moviedb.repository

import com.example.moviedb.network.AppDatabase

object LocalInjector {

    var appDatabase: AppDatabase? = null

    fun injectDb(): AppDatabase? {
        return appDatabase
    }
}