package com.aou.ss.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class MainAPIManager {
    companion object  {
         var BASE_URL: String = "https://smartandsecureapplication.towarddevelopment.org/api/"
    }

    fun provideRetrofitInterface(): Retrofit {
        val logging =  HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient =  OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(300, TimeUnit.SECONDS)
        httpClient.readTimeout(300, TimeUnit.SECONDS)
        val gson = GsonBuilder()
                .setLenient()
                .create()
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()
    }
}