package com.futsalrecord.futsalApp.api;

import com.futsalrecord.futsalApp.model.CustomersUD;
import com.futsalrecord.futsalApp.model.Events;
import com.futsalrecord.futsalApp.model.Futsal;
import com.futsalrecord.futsalApp.model.FutsalDetails;
import com.futsalrecord.futsalApp.model.Users;
import com.futsalrecord.futsalApp.serverResponse.ImageResponse;
import com.futsalrecord.futsalApp.serverResponse.RegisterResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UsersAPI {
    //registration
    @POST("users/register")
    Call<RegisterResponse> registerUser(@Body Users users);

    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part image);

    //login
    @FormUrlEncoded
    @POST("users/login")
    Call<RegisterResponse> checkUser(@Field("username") String username, @Field("password") String password);

    //Show futsal detail
    @GET("users/futsalList")
    Call<List<FutsalDetails>> getFutsalDetails(@Header("Authorization") String token);

    //Show futsal event
    @GET("users/eventList")
    Call<List<Events>> getEventDetail(@Header("Authorization") String token);

    //get user profile
    @GET("users/profile")
    Call<Users> getUserDetails(@Header("Authorization") String token);
}
