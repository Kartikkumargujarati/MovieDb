/*
 * Created by Kartik Kumar Gujarati on 7/18/19 7:33 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kartik.salesforcechallenege.model.Movies

@Database(entities = [Movies.Movie::class], version = 1)
abstract class MovieRoomDb : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieRoomDb? = null

        fun getDatabase(context: Context): MovieRoomDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDb::class.java,
                    "Movie_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}