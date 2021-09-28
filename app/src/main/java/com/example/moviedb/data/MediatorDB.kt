package com.example.moviedb.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.moviedb.model.Movie
import com.example.moviedb.network.RemoteKeys
import com.example.moviedb.network.AppDatabase
import com.example.moviedb.repository.RepoMovies
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class MediatorDB (private val webservice: RepoMovies, private val appDatabase: AppDatabase) :
    RemoteMediator<Int, Movie>() {

    private val apiKey = "1d3a01b7a462bea05038866f2fab52a1"
    private val startingPageIndex = 1
    private var page = 1

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Movie>
    ): MediatorResult {

        page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData?.let { it as Int } ?:run{ page++ }
            }
        }

        try {
            val response = webservice.getPopularMovies(apiKey, "es", page)
            val isEndOfList = response.movieList.isEmpty()
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getRepoDao().clearRemoteKeys()
                    appDatabase.getMovieDao().clearAllMovies()
                }
                val prevKey = if (page == startingPageIndex) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.movieList.map {
                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.getRepoDao().insertAll(keys)
                appDatabase.getMovieDao().insertAll(response.movieList)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Movie>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: startingPageIndex
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: RemoteKeys(page++, page, page++)
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: RemoteKeys(page, page, page)
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }


    private suspend fun getLastRemoteKey(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movie -> appDatabase.getRepoDao().remoteKeysMovieId(movie.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie -> appDatabase.getRepoDao().remoteKeysMovieId(movie.id) }
    }


    private suspend fun getClosestRemoteKey(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.getRepoDao().remoteKeysMovieId(repoId)
            }
        }
    }
}