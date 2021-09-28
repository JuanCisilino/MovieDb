package com.example.moviedb.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.model.Movie
import kotlinx.android.synthetic.main.fragment_detail_movie.*

class DetailFragment : Fragment() {

    private lateinit var movie: Movie
    private val baseUrl = "https://image.tmdb.org/t/p/original"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let { movie = it.getParcelable("movie")!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val poster = "$baseUrl${movie.poster}"
        Glide.with(requireContext()).load(poster).into(movieImageMovie)
        movieTitleTextView.text = movie.title
        movieReleaseTextView.text = movie.release
        movieSinopsisTextView.text = movie.sinopsis
    }

}