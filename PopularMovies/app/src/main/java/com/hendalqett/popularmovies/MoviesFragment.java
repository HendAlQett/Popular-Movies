package com.hendalqett.popularmovies;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hendalqett.popularmovies.adapters.ImageAdapter;
import com.hendalqett.popularmovies.data.MovieContract;
import com.hendalqett.popularmovies.models.Movie;
import com.hendalqett.popularmovies.models.MovieResult;
import com.hendalqett.popularmovies.retrofit.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Hend on 9/1/2015.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    View rootView;
    final String LOG_TAG = MoviesFragment.class.getSimpleName();
    final String KEY_MOVIES_LIST = "movies_list";
    static final String KEY_SELECTED_ITEM_POSITION = "movie_item_position";
    static final String KEY_MOVIE_ITEM = "movie_item";
    GridView gridview;
    ImageAdapter adapter;
    ArrayList<Movie> moviesList;
    boolean flagInstanceNull = true;
    final static int MOVIE_LOADER = 0;
    private int mPosition = ListView.INVALID_POSITION;


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
    @Bind(R.id.llFragmentMovies)
    LinearLayout llFragmentMovies;


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MovieContract.MovieEntry._ID + " ASC";
        Uri movieFromUri = MovieContract.MovieEntry.buildMovieUri();

        Log.d(LOG_TAG, movieFromUri.toString());

        return new CursorLoader(getActivity(), movieFromUri, MOVIE_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        ArrayList<Movie> movies = new ArrayList<>();
        if (cursor != null && cursor.getCount() != 0) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                // The Cursor is now set to the right position
                movies.add(new Movie(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getDouble(6)));
            }

            adapter.updateMovies(movies);
            gridview.setAdapter(adapter);


        } else {
            Snackbar.make(rootView, "No favorites saved", Snackbar.LENGTH_LONG).show();

        }

        getLoaderManager().destroyLoader(MOVIE_LOADER);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.emptyList();
    }


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        //TODO: will be a URI instead
         void onItemSelected(Movie movie);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        gridview = (GridView) rootView.findViewById(R.id.gridViewMovies);
        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIES_LIST)) {
            moviesList = new ArrayList<>();
            updateMovies();

        } else {
            moviesList = savedInstanceState.getParcelableArrayList(KEY_MOVIES_LIST);
            flagInstanceNull = false;

        }


        adapter = new ImageAdapter(getActivity(), moviesList);
        gridview.setAdapter(adapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (Utils.isNetworkAvailable(getActivity()))
                {
                    Movie movie = (Movie) adapterView.getItemAtPosition(position);

                    ((Callback) getActivity()).onItemSelected(movie);
                    mPosition= position;
                } else {
                    String sort = Utils.getCurrentSortPereference(getActivity());
                    if (sort.equals(getString(R.string.pref_sort_favorites))) {
                        Movie movie = (Movie) adapterView.getItemAtPosition(position);

                        ((Callback) getActivity()).onItemSelected(movie);
                        mPosition= position;

                    } else {
                        showNetworkNotAvailable();
                    }
                }


            }
        });

        if (!flagInstanceNull) //Or instance not null
        {
            mPosition= savedInstanceState.getInt(KEY_SELECTED_ITEM_POSITION);

            ListAdapter listAdapter = gridview.getAdapter();
            if (MainActivity.mTwoPane) {
                gridview.performItemClick(gridview.getChildAt(mPosition), mPosition, listAdapter.getItemId(mPosition));
                gridview.smoothScrollToPosition(mPosition);
            }
        }

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void onSortChanged() {
        updateMovies();
//        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        if (flagInstanceNull) {
//            updateMovies();
//
//        }
//
//    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_MOVIES_LIST, moviesList);
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(KEY_SELECTED_ITEM_POSITION, mPosition);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void updateMovies() {
        String sort = Utils.getCurrentSortPereference(getActivity());
        if (sort.equals(getString(R.string.pref_sort_favorites))) {
            getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        } else {


            if (Utils.isNetworkAvailable(getActivity())) {


                RestClient.get().requestMovies(sort, BuildConfig.MOVIE_API_KEY, new retrofit.Callback<MovieResult>() {
                    @Override
                    public void success(MovieResult movieResult, Response response) {
                        ArrayList<Movie> movies = movieResult.getMovies();
//                        Toast.makeText(getActivity(), "Movies size " + movies.size(), Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "" + movies.size());
                        if (movies != null) {
                            moviesList = movies;
                            adapter.updateMovies(moviesList);

                            gridview.setAdapter(adapter);
//
                            ListAdapter listAdapter = gridview.getAdapter();
                            if (MainActivity.mTwoPane) {
                                gridview.performItemClick(gridview.getChildAt(0), 0, listAdapter.getItemId(0));
                            }

                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
//                        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "Fail");

                    }
                });

            } else {
                showNetworkNotAvailable();

            }
        }


    }


    class FetchMoviesData extends AsyncTask<String, Void, ArrayList<Movie>> {


        public ArrayList<Movie> getMoviesData(String moviesJsonStr) throws JSONException {


            final String KEY_ORIGINAL_TITLE = "original_title";
            final String KEY_OVERVIEW = "overview";
            final String KEY_RELEASE_DATE = "release_date";
            final String KEY_POSTER_PATH = "poster_path";
            final String KEY_VOTE_AVERAGE = "vote_average";
            JSONObject jsonObject;
            jsonObject = new JSONObject(moviesJsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            ArrayList<Movie> movies = new ArrayList<>();

            for (int movieIndex = 0; movieIndex < jsonArray.length(); movieIndex++) {
                JSONObject movieJsonObject = jsonArray.getJSONObject(movieIndex);
                Movie movie = new Movie();
                movie.setOriginalTitle(movieJsonObject.getString(KEY_ORIGINAL_TITLE));
                movie.setPosterPath(movieJsonObject.getString(KEY_POSTER_PATH));
                movie.setOverview(movieJsonObject.getString(KEY_OVERVIEW));
                movie.setReleaseDate(movieJsonObject.getString(KEY_RELEASE_DATE));
                movie.setVoteAverage(movieJsonObject.getDouble(KEY_VOTE_AVERAGE));

                movies.add(movie);
            }


            return movies;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            //Close these in finally block
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr;
            ArrayList<Movie> movies = null;


            //TODO: Generate API_KEY
            final String MOVIES_API_KEY = "1ffb1c24265390a7139e3099ae8b8e4b";
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
            try {

                Uri builder = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter("sort_by", strings[0])
                        .appendQueryParameter("api_key", MOVIES_API_KEY)
                        .build();

                URL url = new URL(builder.toString());

                Log.d(LOG_TAG, builder.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer buffer = new StringBuffer();

                if ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                moviesJsonStr = buffer.toString();
//                Log.d(LOG_TAG, moviesJsonStr);
                movies = getMoviesData(moviesJsonStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

//            Log.d(LOG_TAG, Integer.toString(movies.size()));
            //Update adapter


            if (movies != null) {
                moviesList = movies;
                adapter.updateMovies(moviesList);

                gridview.setAdapter(adapter);
            }


        }
    }

    public static int getVoteCountForItemInJson(String moviesJsonStr, int movieIndex) throws JSONException {
        //To test parsing
        int voteCount;
        JSONObject jsonObject;

        jsonObject = new JSONObject(moviesJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        JSONObject movieJsonObject = jsonArray.getJSONObject(movieIndex);
        voteCount = movieJsonObject.getInt("vote_count");

        return voteCount;
    }

//    private boolean isNetworkAvailable(Activity activity) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }


    void showNetworkNotAvailable() {
       final Snackbar snackbar= Snackbar
                .make(llFragmentMovies, R.string.snackbar_text, Snackbar.LENGTH_INDEFINITE);


        snackbar.setAction(R.string.snackbar_action, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateMovies();
                snackbar.dismiss();

            }
        }).show();


    }

}
