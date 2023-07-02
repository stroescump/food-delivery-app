package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.shared.models.MenuItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductDetailsApi {
    @GET("/products")
    fun fetchProductDetails(@Query("productId") productId: String): MenuItem
}
