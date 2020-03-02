package com.mapolbs.kerryapp.Utilities;

import retrofit2.Retrofit;

public class ApiUtils {

    public static final String BASE_URL="http://106.51.1.175/kerryftl/api/login/";

    public static UserService getUserService()
    {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

}
