/*
 * Created by Kartik Kumar Gujarati on 7/18/19 7:10 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.model

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
        var isFavorite: Boolean
    ) : Parcelable
}