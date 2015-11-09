package com.hendalqett.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hendalqett.popularmovies.R;
import com.hendalqett.popularmovies.models.Review;

import java.util.ArrayList;

/**
 * Created by hend on 11/8/15.
 */
public class ReviewAdapter  extends BaseAdapter{

    ArrayList<Review> reviews;
    Context context;
    LayoutInflater inflater;
    ViewHolder mViewHolder;
    public ReviewAdapter(Context context,  ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        inflater = LayoutInflater.from(this.context);

    }
    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Review getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.review_list_item, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.tvReviewAuthor = (TextView) convertView.findViewById(R.id.tvReviewAuthor);
            mViewHolder.tvReviewContent = (TextView) convertView.findViewById(R.id.tvReviewContent);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }




        final Review review = getItem(position);
        mViewHolder.tvReviewAuthor.setText(review.getAuthor());
        mViewHolder.tvReviewContent.setText(review.getContent());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvReviewAuthor;
        TextView tvReviewContent;
    }
}
