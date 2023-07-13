package com.adelinarotaru.fooddelivery.shared.login.data

import com.adelinarotaru.fooddelivery.shared.login.domain.ILoginResponse

data class LoginResponse(
    override val userId: Int,
    override val userType: Int,
    @Transient override val message: String,
    override val userName: String
) : ILoginResponse
