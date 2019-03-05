package com.mohammadmirzakhani.android.challenge.network

import com.mohammadmirzakhani.android.challenge.configs.ApiConfig
import com.mohammadmirzakhani.android.challenge.interfases.ApiRequestInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class OkHttpHelper{
    companion object {
        fun getOkHttpClient(): ApiRequestInterface? {

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + ApiConfig.token)

                val request = requestBuilder.build()
                chain.proceed(request)
            }

            val client = httpClient.build()

            return Retrofit.Builder()
                    .baseUrl(ApiConfig.chunkListUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(ApiRequestInterface::class.java)
        }
    }



}