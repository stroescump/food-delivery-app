package com.adelinarotaru.fooddelivery.shared.login.domain

interface ILoginResponse {
    val userId: Int
    val userType: Int
    val userName: String
    val message: String
}