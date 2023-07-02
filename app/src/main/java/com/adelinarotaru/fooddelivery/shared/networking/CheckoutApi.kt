package com.adelinarotaru.fooddelivery.shared.networking

import retrofit2.http.POST

interface CheckoutApi {
    @POST
    fun placeOrder()
}
