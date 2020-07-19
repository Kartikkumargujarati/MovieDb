/*
 * Created by Kartik Kumar Gujarati on 7/19/19 12:58 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kartik.openmoviedb.R
import com.kartik.openmoviedb.model.Movies

import kotlinx.android.synthetic.main.activity_movie_details.*

/**
 * An activity representing a single Movie detail screen.
 */
class MovieDetailsActivity : AppCompatActivity() {
    private var movie: Movies.Movie? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        setSupportActionBar(detail_toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        movie = intent.getParcelableExtra(MovieDetailsFragment.ARG_MOVIE)
        val options = RequestOptions().centerCrop()
        Glide.with(this).load(movie?.poster).apply(options).into(toolbar_image)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            val fragment = MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        MovieDetailsFragment.ARG_MOVIE,
                        movie
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.movie_detail_container, fragment)
                .commit()
        }

    }

}
