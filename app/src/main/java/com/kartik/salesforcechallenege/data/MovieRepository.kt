/*
 * Created by Kartik Kumar Gujarati on 7/18/19 7:48 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.data

import com.kartik.salesforcechallenege.data.local.MovieDao
import com.kartik.salesforcechallenege.data.remote.MovieRemoteServiceImpl

class MovieRepository(private val movieDao: MovieDao, private val remoteService: MovieRemoteServiceImpl) {

    fun getMoviesFromSearch(searchKey: String) {

    }

    fun getFavoritedMovies() {

    }

}