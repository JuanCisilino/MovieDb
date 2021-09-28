package com.example.moviedb.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviedb.R
import com.example.moviedb.model.Movie
import com.example.moviedb.ui.viewmodel.LoaderStateAdapter
import com.example.moviedb.ui.viewmodel.MainViewModel
import com.example.moviedb.ui.viewmodel.PagingAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MainFragment : Fragment(R.layout.fragment_main), PagingAdapter.OnMovieClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter : PagingAdapter
    lateinit var loaderStateAdapter: LoaderStateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setUpViews()
        fetchMoviesFromDb()
    }

    private fun initMembers() {
        viewModel = defaultViewModelProviderFactory.create(MainViewModel::class.java)
        adapter = PagingAdapter(this, requireContext())
        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
    }

    private fun setUpViews() {
        movieContainer.layoutManager = LinearLayoutManager(context)
        movieContainer.adapter = adapter.withLoadStateFooter(loaderStateAdapter)
    }

    private fun fetchMoviesFromDb() {
        lifecycleScope.launch {
            viewModel.fetchMovieList().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onMovieClick(movie: Movie) {
        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        findNavController().navigate(R.id.detailMovie, bundle)
    }

}