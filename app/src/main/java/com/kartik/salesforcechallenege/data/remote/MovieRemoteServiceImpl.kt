/*
 * Created by Kartik Kumar Gujarati on 7/18/19 7:45 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRemoteServiceImpl {

    fun getRemoteService(): MovieRemoteService {
        return Retrofit.Builder()
            .baseUrl(OMDB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MovieRemoteService::class.java)
    }

    companion object {
        const val OMDB_URL = "https://omdbapi.com"
    }
}