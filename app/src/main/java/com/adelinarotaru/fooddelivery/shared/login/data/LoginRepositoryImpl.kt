package com.adelinarotaru.fooddelivery.shared.login.data

import com.adelinarotaru.fooddelivery.shared.login.domain.LoginRepository
import com.adelinarotaru.fooddelivery.shared.networking.LoginApi

class LoginRepositoryImpl(private val loginApi: LoginApi) : LoginRepository {
    override suspend fun login(userCredentials: UserCredentials): LoginResponse =
        loginApi.login(userCredentials)
}