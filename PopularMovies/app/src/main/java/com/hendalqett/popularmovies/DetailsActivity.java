package com.hendalqett.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hendalqett.popularmovies.models.Movie;

import butterknife.ButterKnife;

/**
 * Created by Hend on 9/3/2015.
 */
public class DetailsActivity extends AppCompatActivity {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getSupportActionBar().setElevation(0f);

//        if (savedInstanceState == null || !savedInstanceState.containsKey(MoviesFragment.KEY_MOVIE_ITEM)) {
//            movie = getIntent().getParcelableExtra(MoviesFragment.KEY_MOVIE_ITEM);
//        } else {
//            movie = savedInstanceState.getParcelable(MoviesFragment.KEY_MOVIE_ITEM);
//
//        }
//        tvMovieTitle.setText(movie.getOriginalTitle());
//        if (!TextUtils.isEmpty(movie.getOverview()) && !TextUtils.equals(movie.getOverview(), "null")) {
//            tvMovieDescription.setText(movie.getOverview());
//        }
//        if (!TextUtils.isEmpty(movie.getReleaseDate()) && !TextUtils.equals(movie.getReleaseDate(), "null")) {
//            tvReleaseDate.setText(movie.getReleaseDate());
//        }
//
//        tvRating.setText(Double.toString(movie.getVoteAverage()) + "/10.0");
//
//        Picasso.with(DetailsActivity.this).load(getString(R.string.poster_medium_base_url) + movie.getPosterPath()).placeholder(R.mipmap.thumbnail).into(ivMovie);
//

        if (savedInstanceState == null) {


            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailsFragment.DETAIL_MOVIE, getIntent().getExtras().getParcelable(DetailsFragment.DETAIL_MOVIE));

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movies_detail_container, fragment)
                    .commit();

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MoviesFragment.KEY_MOVIE_ITEM, movie);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
