package com.example.employee_app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //Retrofit Client: Implementation for Retrofit
    // Siguroha nga husto ang IP ug naay /api/ sa tumoy
    // Kinahanglan ingon ani gyud ang format:
    private static final String BASE_URL = "http://192.168.100.9:3000/";
    private static Retrofit retrofit;

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // 1. Declare ug Initialize ang Gson nga naay setLenient
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // 2. I-build ang Retrofit kausa lang
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}