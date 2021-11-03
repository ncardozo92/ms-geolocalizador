package com.ncardozo.msgeolocalizador.config;

import com.ncardozo.msgeolocalizador.client.RestIntefaceGeolocation;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfiguration {

    @Value("${services.ipapi.url}")
    private String ipApiBaseUrl;

    @Bean
    public RestIntefaceGeolocation restIntefaceGeolocation(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(5, TimeUnit.SECONDS);
        httpClient.readTimeout(5, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ipApiBaseUrl)
                .client(httpClient.build())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit.create(RestIntefaceGeolocation.class);
    }


}
