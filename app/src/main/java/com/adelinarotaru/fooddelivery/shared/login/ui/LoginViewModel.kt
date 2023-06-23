package com.adelinarotaru.fooddelivery.shared.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.shared.login.data.UserCredentials
import com.adelinarotaru.fooddelivery.shared.login.domain.LoginRepository
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginSuccess = MutableSharedFlow<Boolean?>(replay = 1)
    val loginSuccess = _loginSuccess.asSharedFlow()
    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        val userCredentials = UserCredentials(email, password)
        coRunCatching {
            loginRepository.login(userCredentials)
        }.onSuccess {
            _loginSuccess.tryEmit(true)
        }.onFailure {
            _loginSuccess.tryEmit(false)
        }
    }
}