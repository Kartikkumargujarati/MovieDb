/*
 * Created by Kartik Kumar Gujarati on 7/18/19 7:36 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.data.remote

import com.kartik.salesforcechallenege.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieRemoteService {

    @GET("?")
    suspend fun searchMovies(@Query("s") name: String, @Query("page") start: String, @Query("apikey") apiKey: String): Response<Movies.MovieList>

    @GET("?plot=full")
    suspend fun getMovieDetails(@Query("i") name: String, @Query("apikey") apiKey: String): Response<Movies.MovieDetails>

}