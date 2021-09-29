package com.example.moviedb.network

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviedb.model.Movie
import com.example.moviedb.model.RemoteKeys

@Database(version = 2, entities = [Movie::class, RemoteKeys::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMovieDao() : MovieDao
    abstract fun getRepoDao(): RemoteKeysDao

    companion object {

        val MOVIE_DB = "movie.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, MOVIE_DB)
                .fallbackToDestructiveMigration().build()
    }

}