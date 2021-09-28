package com.example.moviedb.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviedb.data.PagingMediatorUC
import com.example.moviedb.model.Movie
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class MainViewModel(): ViewModel() {

    private val pagingMediatorUC = PagingMediatorUC().getInstance()

    fun fetchMovieList(): Flow<PagingData<Movie>> {
        return pagingMediatorUC.getMoviesFlowDb().cachedIn(viewModelScope)
    }

}