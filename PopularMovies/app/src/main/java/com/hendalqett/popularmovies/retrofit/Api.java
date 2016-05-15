package com.hendalqett.popularmovies.retrofit;

import com.hendalqett.popularmovies.models.MovieResult;
import com.hendalqett.popularmovies.models.ReviewResult;
import com.hendalqett.popularmovies.models.TrailerResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by hend on 11/8/15.
 */
public interface Api {


    @GET("/discover/movie")
    void requestMovies(@Query("sort_by") String sortBy, @Query("api_key") String apiKey, Callback<MovieResult> callback);


    @GET("/movie/{id}/videos")
    void requestTrailer(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<TrailerResult> callback);


    @GET("/movie/{id}/reviews")
    void requestReviews( @Path("id") int movieId, @Query("api_key") String apiKey, Callback<ReviewResult> callback);


}
