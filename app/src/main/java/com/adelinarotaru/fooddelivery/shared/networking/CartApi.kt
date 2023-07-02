package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.shared.models.Cart
import retrofit2.http.GET

interface CartApi {
    @GET("/cart")
    fun fetchCart(): Cart
}
