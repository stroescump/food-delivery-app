package com.adelinarotaru.fooddelivery.shared.login.domain

import com.adelinarotaru.fooddelivery.shared.login.data.UserCredentials

interface LoginRepository {
    suspend fun login(userCredentials: UserCredentials): Any
}