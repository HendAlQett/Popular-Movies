package com.hendalqett.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hend on 11/7/15.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.hendalqett.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MOVIE = "movie";



    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID="movie_id";

        public static final String COLUMN_TITLE = "original_title";

        public static final String COLUMN_OVERVIEW = "overview";


        public static final String COLUMN_RELEASE_DATE = "release_date";


        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";



        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

//        public static Uri buildMovieUri(int movieId) {
//            return ContentUris.withAppendedId(CONTENT_URI, movieId);
//        }

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static int getMovieIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

        public static Uri buildMovieUri() {
            return CONTENT_URI.buildUpon().build();
        }


    }




//    public static final class FavoriteEntry implements BaseColumns {
//
//        public static final String TABLE_NAME = "weather";
//
//        // Column with the foreign key into the location table.
//        public static final String COLUMN_LOC_KEY = "location_id";
//        // Date, stored as long in milliseconds since the epoch
//        public static final String COLUMN_DATE = "date";
//        // Weather id as returned by API, to identify the icon to be used
//        public static final String COLUMN_WEATHER_ID = "weather_id";
//
//        // Short description and long description of the weather, as provided by API.
//        // e.g "clear" vs "sky is clear".
//        public static final String COLUMN_SHORT_DESC = "short_desc";
//
//        // Min and max temperatures for the day (stored as floats)
//        public static final String COLUMN_MIN_TEMP = "min";
//        public static final String COLUMN_MAX_TEMP = "max";
//
//        // Humidity is stored as a float representing percentage
//        public static final String COLUMN_HUMIDITY = "humidity";
//
//        // Humidity is stored as a float representing percentage
//        public static final String COLUMN_PRESSURE = "pressure";
//
//        // Windspeed is stored as a float representing windspeed  mph
//        public static final String COLUMN_WIND_SPEED = "wind";
//
//        // Degrees are meteorological degrees (e.g, 0 is north, 180 is south).  Stored as floats.
//        public static final String COLUMN_DEGREES = "degrees";
//
////
////        public static final Uri CONTENT_URI =
////                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();
////
////        public static final String CONTENT_TYPE =
////                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
////        public static final String CONTENT_ITEM_TYPE =
////                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
////
////
////        public static Uri buildWeatherUri(long id) {
////            return ContentUris.withAppendedId(CONTENT_URI, id);
////        }
//
//    }

}
