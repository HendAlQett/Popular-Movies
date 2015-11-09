package com.hendalqett.popularmovies.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hendalqett.popularmovies.R;
import com.hendalqett.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hend on 9/2/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Movie> movies;
    LayoutInflater inflater;
    ViewHolder mViewHolder;

    public ImageAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
        inflater = LayoutInflater.from(this.context);
    }
    public void updateMovies(ArrayList<Movie> movies) {
        this.movies = movies;
        //Triggers the list update
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.grid_view_item, viewGroup, false);
            mViewHolder = new ViewHolder();
            mViewHolder.posterImage = (ImageView) view.findViewById(R.id.imgPoster);
            view.setTag(mViewHolder);
            Log.d("Image","method called "+i);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }




        final Movie movie = getItem(i);
        Picasso mPicasso = Picasso.with(context);
        //I can use setIndicatorsEnabled(true) to show colored ribbons to indicate the image source
//        mPicasso.setIndicatorsEnabled(true);
        mPicasso.load(context.getString(R.string.poster_small_base_url) + movie.getPosterPath()).placeholder(R.mipmap.thumbnail).into(mViewHolder.posterImage);

        return view;
    }

    public void emptyList() {
       movies=null;
    }


    private static class ViewHolder {
        ImageView posterImage;
    }



}
