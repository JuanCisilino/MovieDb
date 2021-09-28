package com.example.moviedb.network

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.example.moviedb.model.Movie
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest: TestCase(){

    private lateinit var db: AppDatabase
    private lateinit var dao: MovieDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.getMovieDao()
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun writeAndReadMovies()= runBlocking{
        val movie = Movie(1, "la historia sin fin")
        val movieList = ArrayList<Movie>()
        movieList.add(movie)
        dao.insertAll(movieList)
        val movieResult = dao.getMovieListFromDb()
        assertEquals(movie.id, movieResult[0].id)
    }
}