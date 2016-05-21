package com.example.dk.mapsviabt;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.GET;
import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Field;
/**
 * Created by DK on 5/20/2016.
 */
public interface HttpRequest {
    @FormUrlEncoded
    @POST("/api/?format=json")
    void putData(@Field("initial_point[]")List<Double> initialPoint,
                 @Field("final_point[]")List<Double> finalPoint ,Callback<ServerData> uscb);
    @GET("/api/?format=json")
    void getData(Callback<ServerData> cb);


}
