package com.adelinarotaru.fooddelivery

import com.adelinarotaru.fooddelivery.shared.networking.LoginApi
import com.adelinarotaru.fooddelivery.shared.networking.RestaurantApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DependencyProvider {
    companion object {
        // Create an instance of OkHttpClient with the logging interceptor and the BodyLevelInterceptor
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        private const val BASE_URL = "http://10.0.2.2:5000"

        private val retrofit: Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        fun provideRestaurantApi(): RestaurantApi = retrofit.create(RestaurantApi::class.java)
        fun provideLoginApi(): LoginApi = retrofit.create(LoginApi::class.java)
    }
}