package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://127.0.0.0:5000"

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private val restaurantApi: RestaurantApi = retrofit.create(RestaurantApi::class.java)

interface RestaurantApi {
    @GET("restaurants")
    suspend fun getRestaurants(): List<Restaurant>
}