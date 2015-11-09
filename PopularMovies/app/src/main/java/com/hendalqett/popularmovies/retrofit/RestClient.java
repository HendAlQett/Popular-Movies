package com.hendalqett.popularmovies.retrofit;

/**
 * Created by hend on 11/7/15.
 */

import com.google.gson.Gson;




        import com.google.gson.FieldNamingPolicy;

        import com.google.gson.GsonBuilder;

        import com.squareup.okhttp.OkHttpClient;



        import retrofit.RestAdapter;
        import retrofit.client.OkClient;
        import retrofit.converter.GsonConverter;


/**
 * Created by Pro on 1/29/15.
 */
public class RestClient {

    //static Api REST_CLIENT;
    static Api REST_CLIENT;
    //static String ENDPOINT = "http://nazzelha.com";
    static String ENDPOINT = "http://api.themoviedb.org/3";


    static {
        //  setupRestClient();
        setupRestClient();

    }


    RestClient() {
    }

    public static Api get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).disableHtmlEscaping()

                .create();
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setClient(new OkClient(new OkHttpClient()))
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL);


        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(Api.class);
    }


}
