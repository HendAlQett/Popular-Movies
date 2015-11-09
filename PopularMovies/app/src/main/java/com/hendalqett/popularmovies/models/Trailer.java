package com.hendalqett.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hend on 11/8/15.
 */
public class Trailer {

    @SerializedName("key")
    String key;
    @SerializedName("name")
    String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
