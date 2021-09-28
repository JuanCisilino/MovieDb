package com.example.moviedb.ui.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class PagingAdapter(private val itemClickListener: OnMovieClickListener, val context: Context):
    PagingDataAdapter<Movie, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    interface OnMovieClickListener{
        fun onMovieClick(movie: Movie)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.equals(newItem)

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.equals(newItem)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PopularMoviesViewHolder)?.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PopularMoviesViewHolder(parent.rootView).getInstance(parent)
    }
    /**
     * view holder class
     */
    inner class PopularMoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun getInstance(parent: ViewGroup): PopularMoviesViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_movie, parent, false)
            return PopularMoviesViewHolder(view)
        }

        private val baseUrl = "https://image.tmdb.org/t/p/original"

        fun bind(item: Movie?) {
            item?.let { movie ->
                val poster = "$baseUrl${movie.poster}"
                itemView.titleTextView.text = movie.title
                itemView.releaseTextView.text = movie.release
                Glide.with(context).load(poster).into(itemView.imageMovie)
                itemView.setOnClickListener { itemClickListener.onMovieClick(movie) }
            }
        }

    }
}