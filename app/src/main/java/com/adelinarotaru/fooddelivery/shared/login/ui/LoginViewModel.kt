package com.adelinarotaru.fooddelivery.shared.login.ui

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.login.data.UserCredentials
import com.adelinarotaru.fooddelivery.shared.login.domain.ILoginResponse
import com.adelinarotaru.fooddelivery.shared.login.domain.LoginRepository
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) :
    BaseViewModel() {

    private val _loginSuccess = MutableSharedFlow<ILoginResponse?>(replay = 1)
    val loginSuccess = _loginSuccess.asSharedFlow()
    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {

        setLoading(true)
        val userCredentials = UserCredentials(email, password)
        coRunCatching {
            loginRepository.login(userCredentials)
        }.onSuccess { res ->
            _loginSuccess.tryEmit(res)
            setLoading(false)
        }.onFailure {
            sendError(it)
            setLoading(false)
        }
    }
}