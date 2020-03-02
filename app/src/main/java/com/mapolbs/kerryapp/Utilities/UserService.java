package com.mapolbs.kerryapp.Utilities;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    /*@GET("login/{username}/{password}")*/
    @FormUrlEncoded
    @POST("login")
    Call<Void> login(@Field("username") String username, @Field("password") String password);

}
