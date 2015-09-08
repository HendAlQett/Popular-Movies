package com.hendalqett.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hendalqett.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hend on 9/3/2015.
 */
public class DetailsActivity extends AppCompatActivity {
    @Bind(R.id.tvMovieTitle)
    TextView tvMovieTitle;
    @Bind(R.id.ivMovie)
    ImageView ivMovie;
    @Bind(R.id.tvMovieDescription)
    TextView tvMovieDescription;
    @Bind(R.id.tvReleaseDate)
    TextView tvReleaseDate;
    @Bind(R.id.tvRating)
    TextView tvRating;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if (savedInstanceState == null || !savedInstanceState.containsKey(MoviesFragment.KEY_MOVIE_ITEM)) {
            movie = getIntent().getParcelableExtra(MoviesFragment.KEY_MOVIE_ITEM);
        } else {
            movie = savedInstanceState.getParcelable(MoviesFragment.KEY_MOVIE_ITEM);

        }
        tvMovieTitle.setText(movie.getOriginalTitle());
        if (!TextUtils.isEmpty(movie.getOverview()) && !TextUtils.equals(movie.getOverview(), "null")) {
            tvMovieDescription.setText(movie.getOverview());
        }
        if (!TextUtils.isEmpty(movie.getReleaseDate()) && !TextUtils.equals(movie.getReleaseDate(), "null")) {
            tvReleaseDate.setText(movie.getReleaseDate());
        }

        tvRating.setText(Double.toString(movie.getVoteAverage()) + "/10.0");

        Picasso.with(DetailsActivity.this).load(getString(R.string.poster_medium_base_url) + movie.getPosterPath()).placeholder(R.mipmap.thumbnail).into(ivMovie);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MoviesFragment.KEY_MOVIE_ITEM, movie);
    }
}
