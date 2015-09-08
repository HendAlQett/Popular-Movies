package com.hendalqett.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.hendalqett.popularmovies.adapters.ImageAdapter;
import com.hendalqett.popularmovies.models.Movie;

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

/**
 * Created by Hend on 9/1/2015.
 */
public class MoviesFragment extends Fragment {

    View rootView;
    final String LOG_TAG = MoviesFragment.class.getSimpleName();
    final String KEY_MOVIES_LIST = "movies_list";
    static final String KEY_MOVIE_ITEM="movie_item";
    GridView gridview;
    ImageAdapter adapter;
    ArrayList<Movie> moviesList;


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
        } else {
            moviesList = savedInstanceState.getParcelableArrayList(KEY_MOVIES_LIST);

        }


        adapter = new ImageAdapter(getActivity(), moviesList);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = (Movie) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(KEY_MOVIE_ITEM, movie);
                startActivity(intent);

            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isNetworkAvailable()) {
            updateMovies();
        } else {

            Toast.makeText(getActivity(),"Please check your internet connection!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_MOVIES_LIST, moviesList);
    }

    private void updateMovies() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = pref.getString(getString(R.string.pref_key_sort), getString(R.string.pref_sort_default));
        FetchMoviesData weatherTask = new FetchMoviesData();
        weatherTask.execute(sort);
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
            final String MOVIES_API_KEY = "";
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
                Log.d(LOG_TAG, moviesJsonStr);
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

            Log.d(LOG_TAG, Integer.toString(movies.size()));
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
