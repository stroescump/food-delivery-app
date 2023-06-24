package com.adelinarotaru.fooddelivery.shared.register.ui

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {

    fun registerUser() = viewModelScope.launch(Dispatchers.IO) {
        coRunCatching {

        }.onSuccess {

        }.onFailure {

        }
    }
}