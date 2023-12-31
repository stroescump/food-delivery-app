package com.adelinarotaru.fooddelivery.shared

import com.adelinarotaru.fooddelivery.shared.networking.CartApi
import com.adelinarotaru.fooddelivery.shared.networking.CheckoutApi
import com.adelinarotaru.fooddelivery.shared.networking.CourierApi
import com.adelinarotaru.fooddelivery.shared.networking.LoginApi
import com.adelinarotaru.fooddelivery.shared.networking.ProductDetailsApi
import com.adelinarotaru.fooddelivery.shared.networking.RestaurantApi
import com.adelinarotaru.fooddelivery.shared.networking.StatisticsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

        private val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        private const val BASE_URL = "http://10.0.2.2:5000"

        private val retrofit: Retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client).build()

        fun provideRestaurantApi(): RestaurantApi = retrofit.create(RestaurantApi::class.java)
        fun provideLoginApi(): LoginApi = retrofit.create(LoginApi::class.java)
        fun provideCourierApi(): CourierApi = retrofit.create(CourierApi::class.java)
        fun provideStatisticsApi(): StatisticsApi = retrofit.create(StatisticsApi::class.java)
        fun provideCartApi(): CartApi = retrofit.create(CartApi::class.java)
        fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
        fun provideTestDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
        fun provideProductDetailsApi(): ProductDetailsApi =
            retrofit.create(ProductDetailsApi::class.java)

        fun provideCheckoutApi(): CheckoutApi = retrofit.create(CheckoutApi::class.java)
    }
}