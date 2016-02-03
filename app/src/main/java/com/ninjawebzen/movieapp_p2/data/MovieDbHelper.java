package com.ninjawebzen.movieapp_p2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by VKurochkin on 2/3/2016.
 */

 public class MovieDbHelper extends SQLiteOpenHelper {
        public static final String LOG_TAG = MovieDbHelper.class.getSimpleName();
        private static final int DATABASE_VERSION = 1;

        static final String DATABASE_NAME = "movie.db";

        public MovieDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_MOVIE + " (" +
                    MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_IMAGE + " TEXT, " +
                    MovieContract.MovieEntry.COLUMN_IMAGE2 + " TEXT, " +
                    MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                    MovieContract.MovieEntry.COLUMN_RATING + " INTEGER, " +
                    MovieContract.MovieEntry.COLUMN_DATE + " TEXT);";

            sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

            Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                    newVersion + ". OLD DATA WILL BE DESTROYED");


            //Drop the table
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_MOVIE);
            sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                    MovieContract.MovieEntry.TABLE_MOVIE + "'");

            // re-create database
            onCreate(sqLiteDatabase);
        }
    }
