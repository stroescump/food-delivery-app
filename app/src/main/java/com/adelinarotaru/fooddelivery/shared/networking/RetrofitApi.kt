package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RestaurantApi {
    @GET("restaurants")
    suspend fun getRestaurants(): List<Restaurant>
}