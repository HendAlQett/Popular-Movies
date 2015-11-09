package com.hendalqett.popularmovies.retrofit;

import com.hendalqett.popularmovies.models.MovieResult;
import com.hendalqett.popularmovies.models.ReviewResult;
import com.hendalqett.popularmovies.models.TrailerResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by hend on 11/8/15.
 */
public interface Api {
    //
//@GET("/test/voila/menu")
//    void getMenus(Callback<ArrayOfItems> callback);

//    @GET("/LunchList")
//    void getLunchMenu(Callback<ArrayList<Dish>> callback);

//    @GET("/DinnerList")
//    void getDinnerMenu(Callback<ArrayList<Dish>> callback);

//    @POST("/PlaceOrder")
//    void placeOrder(@Body TypedString body, Callback<Result> callback);
//
//
//    @POST("/PlaceOrder")
//    void placeOrder(@Body SendOrder body, Callback<Result> callback);
//

    @GET("/discover/movie")
    void requestMovies(@Query("sort_by") String sortBy, @Query("api_key") String apiKey, Callback<MovieResult> callback);




    @GET("/movie/{id}/videos")
    void requestTrailer(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<TrailerResult> callback);


    @GET("/movie/{id}/reviews")
    void requestReviews( @Path("id") int movieId, @Query("api_key") String apiKey, Callback<ReviewResult> callback);

//    @GET("/EditCustomer")
//    void editCustomerPassword( @Query("FN") String firstName,@Query("Pa") String password,@Query("CusID") String customerID, Callback<Result> callback);
//
//
//    @GET("/RetrievePass")
//    void retrievePassword( @Query("email") String email, Callback<Result> callback);
//
//
//    @GET("/fbCheck")
//    void loginUsingSocialMedia(@Query("fbID") String fbId, Callback<SignInResponse> callback);
//
//    @GET("/AddCustomer")
//    void signUp(@Query("FN") String firstName, @Query("LN") String lastName, @Query("MobN") String mobileNumber,
//                @Query("Em") String email, @Query("Pa") String password, Callback<SignInResponse> callback);
//
//    @GET("/AddCustomer")
//    void signUp(@Query("FN") String firstName, @Query("LN") String lastName, @Query("MobN") String mobileNumber,
//                @Query("Em") String email, @Query("Pa") String password, @Query("fbID") String fbID,@Query("PushID") String regId, Callback<SignInResponse> callback);
//
//    @GET("/Verify")
//    void verify(@Query("VerificationCode") String verificationCode, @Query("CusID") String customerId, Callback<SignInResponse> callback);
//
//    @GET("/ValidPoints")
//    void points(@Query("CID") String cusId, Callback<Result> callback);
//
//
//    @POST("/OrderPoints")
//    void orderPoints(@Body TypedString body, Callback<Result> callback);
//
//
//
//    //Address
//
//    @GET("/AddAddress")
//    void sendAddress(@Query("CID") String customerId, @Query("AddressName") String addressName, @Query("AddressLine1") String addressLine1, @Query("City") String city, @Query("PostalCode") String postalCode,@Query("Latitude") String latitude,@Query("Longitude") String longitude,Callback<Result> callback);
//
//    @GET("/AddressesList")
//    void getAddresses(@Query("CID") String customerId , Callback<ArrayList<AddressCustomer>> callback);

}
