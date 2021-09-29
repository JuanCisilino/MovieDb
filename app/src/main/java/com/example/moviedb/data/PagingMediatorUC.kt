package com.example.moviedb.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviedb.model.Movie
import com.example.moviedb.network.AppDatabase
import com.example.moviedb.repository.LocalInjector
import com.example.moviedb.repository.RemoteInjector
import com.example.moviedb.repository.RepoMovies
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class PagingMediatorUC(private val webService: RepoMovies = RemoteInjector.injectRetrofitApiService(),
                       private val appDatabase: AppDatabase? = LocalInjector.injectDb()){

    fun getInstance() = PagingMediatorUC(RemoteInjector.injectRetrofitApiService())

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 20,
            prefetchDistance = 10,
            enablePlaceholders = true)
    }

    fun getMoviesFlowDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<Movie>> {
        appDatabase?:run{ throw IllegalStateException("Database is not initialized")}

        val pagingSourceFactory = { appDatabase.getMovieDao().getMoviesFromDb() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = MediatorDB(webService, appDatabase) 
        ).flow
    }

}