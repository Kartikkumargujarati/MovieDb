/*
 * Created by Kartik Kumar Gujarati on 7/18/19 7:29 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.data.local

import androidx.room.*
import com.kartik.openmoviedb.model.Movies

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovieToFavorites(movie: Movies.Movie)

    @Query("SELECT * from movie_table")
    fun getAllFavoriteMovies(): List<Movies.Movie>

    @Delete
    fun removeMovieFromFavorites(movie: Movies.Movie)
}