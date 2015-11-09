package com.hendalqett.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hendalqett.popularmovies.data.MovieContract;
import com.hendalqett.popularmovies.models.Movie;

public class MainActivity extends AppCompatActivity  implements MoviesFragment.Callback{

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    static boolean mTwoPane;

    String mSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSort= Utils.getCurrentSortPereference(this);


        if (findViewById(R.id.movies_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movies_detail_container, new DetailsFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }

            //I don't need to inflate the fragment again if savedInstanceState in not null because the system saved the fragment on orientation.
        } else {
            mTwoPane = false;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sort = Utils.getCurrentSortPereference(this);
        // update the location in our second pane using the fragment manager
        if (sort != null && !sort.equals(mSort)) {
            MoviesFragment moviesFragment = (MoviesFragment) getFragmentManager().findFragmentById(R.id.fragment);
            if (null != moviesFragment) {
                moviesFragment.onSortChanged();
            }
            DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if (null != detailsFragment && sort.equals(getString(R.string.pref_sort_favorites))) {
//                detailsFragment.onSortChanged(sort);
                Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
                if (cursor.getCount() ==0) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movies_detail_container, new DetailsFragment(), DETAILFRAGMENT_TAG)
                            .commit();
                }



            }
            mSort = sort;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailsFragment.DETAIL_MOVIE, movie);
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        } else {
            Intent intent = new Intent(this, DetailsActivity.class);

            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailsFragment.DETAIL_MOVIE, movie);
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }


}
