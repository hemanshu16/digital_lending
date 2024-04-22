package com.digitallending.breservice.retrofit.generator;

import com.digitallending.breservice.exception.NotFoundException;
import com.digitallending.breservice.model.dto.ZonedDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class RetrofitServiceGenerator {
    private final DiscoveryClient discoveryClient;

    @Autowired
    public RetrofitServiceGenerator(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public <T> T createService(Class<T> serviceClass, String serviceName) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

        if (instances.isEmpty()) {
            throw new NotFoundException("No instances of " + serviceName + " found in the service registry");
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();

        ServiceInstance serviceInstance = instances.get(0);

        String baseUrl = serviceInstance.getUri().toString();


        Retrofit builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build()).build();

        return builder.create(serviceClass);
    }

}
