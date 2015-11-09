package com.hendalqett.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hendalqett.popularmovies.adapters.ReviewAdapter;
import com.hendalqett.popularmovies.adapters.TrailerAdapter;
import com.hendalqett.popularmovies.data.MovieContract;
import com.hendalqett.popularmovies.models.Movie;
import com.hendalqett.popularmovies.models.Review;
import com.hendalqett.popularmovies.models.ReviewResult;
import com.hendalqett.popularmovies.models.Trailer;
import com.hendalqett.popularmovies.models.TrailerResult;
import com.hendalqett.popularmovies.retrofit.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by hend on 11/8/15.
 */
public class DetailsFragment extends Fragment {


    static final String DETAIL_MOVIE = "movie";
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
    @Bind(R.id.tvTrailers)
    TextView tvTrailers;
    @Bind(R.id.tvReviews)
    TextView tvReviews;
    @Bind(R.id.cbMovieFavorite)
    CheckBox cbMovieFavorite;
    @Bind(R.id.lvTrailer)
    ListView lvTrailer;
    @Bind(R.id.lvReview)
    ListView lvReview;
    private Movie movie;

    final String LOG_TAG = DetailsFragment.class.getSimpleName();
    ShareActionProvider mShareActionProvider;
     ArrayList<Trailer> mTrailers;
    ArrayList<Review> mReviews;



    private static final String[] MOVIE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_OVERVIEW = 3;
    static final int COL_RELEASE_DATE = 4;
    static final int COL_POSTER_PATH = 5;
    static final int COL_VOTE_AVERAGE = 6;

    String REVIEWS_KEY="reviews_key", TRAILERS_KEY="trailers_key",MOVIE_KEY= "movie_key";


    public DetailsFragment() {
        //Important
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState==null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                movie = arguments.getParcelable(DetailsFragment.DETAIL_MOVIE);
                if (movie != null) {
                    Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, MOVIE_COLUMNS, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{Integer.toString(movie.getMovieId())}, null);
                    if (cursor.getCount() > 0) {
                        cbMovieFavorite.setChecked(true);
                    }
                    cbMovieFavorite.setVisibility(View.VISIBLE);
                }
            }


            cbMovieFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
                        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
                        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

                        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

                        Uri uri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
                        Log.d(LOG_TAG, "" + uri);
                    } else {
                        int count = getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{Integer.toString(movie.getMovieId())});

                        Log.d(LOG_TAG, "number of rows deleted " + count);
                    }
                }
            });


//        if (savedInstanceState == null || !savedInstanceState.containsKey(MoviesFragment.KEY_MOVIE_ITEM)) {
//            movie = getIntent().getParcelableExtra(MoviesFragment.KEY_MOVIE_ITEM);
//        } else {
//            movie = savedInstanceState.getParcelable(MoviesFragment.KEY_MOVIE_ITEM);
//
//        }
            if (movie != null) {
                tvMovieTitle.setText(movie.getOriginalTitle());
                if (!TextUtils.isEmpty(movie.getOverview()) && !TextUtils.equals(movie.getOverview(), "null")) {
                    tvMovieDescription.setText(movie.getOverview());
                } else {
                    tvMovieDescription.setText(getString(R.string.notAvailable));
                }
                if (!TextUtils.isEmpty(movie.getReleaseDate()) && !TextUtils.equals(movie.getReleaseDate(), "null")) {
                    tvReleaseDate.setText(movie.getReleaseDate());
                } else {
                    tvMovieDescription.setText(getString(R.string.notAvailable));
                }

                tvRating.setText(Double.toString(movie.getVoteAverage()) + "/10.0");

                Picasso.with(getActivity()).load(getString(R.string.poster_medium_base_url) + movie.getPosterPath()).placeholder(R.mipmap.thumbnail).into(ivMovie);

                if (Utils.isNetworkAvailable(getActivity())) {

                    updateTrailers(movie);
                    updateReviews(movie);
                }

            }
        }
        else
        {
            movie = savedInstanceState.getParcelable(MOVIE_KEY);
            mReviews = savedInstanceState.getParcelableArrayList(REVIEWS_KEY);
            mTrailers = savedInstanceState.getParcelable(TRAILERS_KEY);
        }


        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(REVIEWS_KEY, mReviews);
        outState.putParcelableArrayList(TRAILERS_KEY, mTrailers);
        outState.putParcelable(MOVIE_KEY,movie);
        super.onSaveInstanceState(outState);
    }

    void updateTrailers(Movie movie) {

        RestClient.get().requestTrailer(movie.getMovieId(), BuildConfig.MOVIE_API_KEY, new Callback<TrailerResult>() {
            @Override
            public void success(TrailerResult trailerResult, Response response) {
                mTrailers = trailerResult.getTrailers();
//                Toast.makeText(getActivity(), "trailers size " + trailers.size(), Toast.LENGTH_SHORT).show();

                if (mTrailers.size() > 0) {
                    TrailerAdapter trailerAdapter = new TrailerAdapter(getActivity(), mTrailers);

                    lvTrailer.setAdapter(trailerAdapter);


                    tvTrailers.setText(getString(R.string.tvTrailers));
                    lvTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mTrailers.get(position).getKey())));
                        }
                    });
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareTrailerIntent());
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    void updateReviews(Movie movie) {

        RestClient.get().requestReviews(movie.getMovieId(), BuildConfig.MOVIE_API_KEY, new Callback<ReviewResult>() {
            @Override
            public void success(ReviewResult reviewResult, Response response) {
                 mReviews = reviewResult.getReviews();

                if (mReviews.size() > 0) {
                    ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), mReviews);

                    lvReview.setAdapter(reviewAdapter);

                    tvReviews.setText(getString(R.string.tvReviews));


                }


            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mTrailers != null) {
            //Attach the intent to the Share Action Provider
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        } else {
            Log.d(LOG_TAG, "Forecast is null");
        }
    }

    private Intent createShareTrailerIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //Prevents the new opened activity we are sharing to, to be on the activity stack
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v="+mTrailers.get(0).getKey());
        return shareIntent;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
