package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.shared.login.data.LoginResponse
import com.adelinarotaru.fooddelivery.shared.login.data.UserCredentials
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("login")
    suspend fun login(@Body userCredentials: UserCredentials): LoginResponse
}