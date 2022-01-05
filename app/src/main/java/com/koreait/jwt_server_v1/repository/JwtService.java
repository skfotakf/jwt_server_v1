package com.koreait.jwt_server_v1.repository;

import com.koreait.jwt_server_v1.BuildConfig;
import com.koreait.jwt_server_v1.repository.models.request.ReqLogin;
import com.koreait.jwt_server_v1.repository.models.response.RestLogin;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JwtService {

    @POST("login")
    Call<RestLogin> getLogin(@Body ReqLogin reqLogin);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://lalacoding.site/")
            .client(httpLoggingInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    static OkHttpClient httpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        if(BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)  //5초가 지나면 자동으로 끝남
                .addInterceptor(interceptor)
                .build();
    }
}
