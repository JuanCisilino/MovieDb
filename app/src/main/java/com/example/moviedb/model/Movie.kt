package com.example.moviedb.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Movie(

    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    @SerializedName("poster_path")
    var poster: String ="",
    @SerializedName("release_date")
    var release: String = "",
    @SerializedName("overview")
    var sinopsis: String = ""
):Parcelable

data class MovieList(
    @SerializedName("results")
    val movieList: List<Movie>)