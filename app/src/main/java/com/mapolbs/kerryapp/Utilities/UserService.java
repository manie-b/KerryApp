package com.mapolbs.kerryapp.Utilities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {

    @GET("login/{username}/{password}")
    Call<Void> login(@Path("username") String username, @Path("password") String password);

}
