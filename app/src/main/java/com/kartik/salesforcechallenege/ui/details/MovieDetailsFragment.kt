/*
 * Created by Kartik Kumar Gujarati on 7/19/19 12:58 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.ui.details

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kartik.salesforcechallenege.R
import com.kartik.salesforcechallenege.model.Movies

/**
 * A placeholder fragment containing a simple view.
 */
class MovieDetailsFragment : Fragment() {

    private var movie: Movies.Movie? = null
    private var rootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_MOVIE)) {
                movie = it.getParcelable(ARG_MOVIE)
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie_details, container, false)
        rootView = root
        return rootView
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.finish()
            return true
        }
        return false
    }

    companion object {
        /**
         * The fragment argument representing the movie ID that this fragment
         * represents.
         */
        const val ARG_MOVIE = "omdb_movie"
    }
}
