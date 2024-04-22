package com.digitallending.loanservice.retrofit.generator;

import com.digitallending.loanservice.model.dto.ZonedDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.Duration;
import java.time.ZonedDateTime;

//@Component
public class ServiceGenerator {
    private final Retrofit retrofit;

    public ServiceGenerator(String baseUrl) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(Duration.ofMillis(30000));
        httpClient.connectTimeout(Duration.ofMillis(30000));

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build());

        retrofit = builder.build();
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
