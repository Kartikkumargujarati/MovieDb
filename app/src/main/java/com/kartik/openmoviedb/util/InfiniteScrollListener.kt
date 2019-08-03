/*
 * Created by Kartik Kumar Gujarati on 7/19/19 9:58 AM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/*
* A util class to implement an infinite scrolling list
*/
abstract class InfiniteScrollListener(private val layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {

    private var isLoadingData: Boolean = false
    private val itemsPerPage = 10 //size per page

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy <= 0) {
            return  // if user scrolling up, do not perform any processing.
        }
        if (!isLoadingData && canLoadData()) {
            doLoadMoreData(layoutManager.findFirstVisibleItemPosition())
        }
    }

    fun setDataLoading(isLoad: Boolean) {
        isLoadingData = isLoad
    }

    private fun canLoadData(): Boolean {
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val pastVisibleItemCount = layoutManager.findFirstVisibleItemPosition()
        val lastItemShown = visibleItemCount + pastVisibleItemCount >= totalItemCount
        return lastItemShown && totalItemCount >= itemsPerPage
    }

    abstract fun doLoadMoreData(firstVisibleItemPosition: Int)
}