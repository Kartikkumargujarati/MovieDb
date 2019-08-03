/*
 * Created by Kartik Kumar Gujarati on 7/18/19 7:10 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class Movies {
    @Parcelize
    data class MovieList(
        @SerializedName("Search")
        val movies: List<Movie>?,
        @SerializedName("totalResults")
        val totalResults: String?
    ) : Parcelable

    @Entity(tableName = "movie_table")
    @Parcelize
    data class Movie(
        @SerializedName("Poster")
        val poster: String?,
        @SerializedName("Title")
        val title: String?,
        @SerializedName("Type")
        val type: String?,
        @SerializedName("Year")
        val year: String?,
        @SerializedName("imdbID")
        @PrimaryKey
        val imdbID: String,
        var isFavorite: Boolean = false,
        var isFavoriteLoading: Boolean = false
    ) : Parcelable

    data class MovieDetails(
        @SerializedName("Actors")
        val actors: String?,
        @SerializedName("Awards")
        val awards: String?,
        @SerializedName("BoxOffice")
        val boxOffice: String?,
        @SerializedName("Country")
        val country: String?,
        @SerializedName("DVD")
        val dVD: String?,
        @SerializedName("Director")
        val director: String?,
        @SerializedName("Genre")
        val genre: String?,
        @SerializedName("Language")
        val language: String?,
        @SerializedName("Metascore")
        val metascore: String?,
        @SerializedName("Plot")
        val plot: String?,
        @SerializedName("Poster")
        val poster: String?,
        @SerializedName("Production")
        val production: String?,
        @SerializedName("Rated")
        val rated: String?,
        @SerializedName("Released")
        val released: String?,
        @SerializedName("Response")
        val response: String?,
        @SerializedName("Runtime")
        val runtime: String?,
        @SerializedName("Title")
        val title: String?,
        @SerializedName("Type")
        val type: String?,
        @SerializedName("Website")
        val website: String?,
        @SerializedName("Writer")
        val writer: String?,
        @SerializedName("Year")
        val year: String?,
        @SerializedName("imdbID")
        val imdbID: String?,
        @SerializedName("imdbRating")
        val imdbRating: String?,
        @SerializedName("imdbVotes")
        val imdbVotes: String?
    )
}