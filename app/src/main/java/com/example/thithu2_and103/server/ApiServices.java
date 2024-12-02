package com.example.thithu2_and103.server;


import com.example.thithu2_and103.XeMay;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {
    public static String BASE_URL = "http://192.168.0.100:3000/api/";

    @GET("list")
    Call<Response<ArrayList<XeMay>>> getList();

    @GET("search")
    Call<Response<ArrayList<XeMay>>> search(@Query("key") String key);

    @DELETE("delete/{id}")
    Call<Response<ArrayList<XeMay>>> delete(@Path("id") String id);



    @Multipart
    @POST("add")
    Call<Response<XeMay>> add(@PartMap Map<String, RequestBody> requestBodyMap,
                                                @Part MultipartBody.Part ds_hinh
    );

    @Multipart
    @PUT("update/{id}")
    Call<Response<XeMay>> update(@PartMap Map<String, RequestBody> requestBodyMap,
                                                   @Path("id") String id,
                                                   @Part MultipartBody.Part ds_hinh
    );



}


