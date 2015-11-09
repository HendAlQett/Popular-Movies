package com.hendalqett.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hendalqett.popularmovies.R;
import com.hendalqett.popularmovies.models.Trailer;

import java.util.ArrayList;

/**
 * Created by hend on 11/8/15.
 */
public class TrailerAdapter  extends BaseAdapter{

    ArrayList<Trailer> trailers;
    Context context;
    LayoutInflater inflater;
    ViewHolder mViewHolder;
    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
        inflater = LayoutInflater.from(this.context);

    }
    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Trailer getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.trailer_list_item, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.tvTrailerTitle = (TextView) convertView.findViewById(R.id.tvTrailerTitle);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }




        final Trailer trailer = getItem(position);
        mViewHolder.tvTrailerTitle.setText(trailer.getName());

        return convertView;
    }


    private static class ViewHolder {
        TextView tvTrailerTitle;
    }
}
