package com.hendalqett.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hend on 11/8/15.
 */
public class Review {

    @SerializedName("author")
    String author;
    @SerializedName("content")
    String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
