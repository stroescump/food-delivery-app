package com.adelinarotaru.fooddelivery.customer.ui.productdetails

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.MenuItem
import com.adelinarotaru.fooddelivery.shared.networking.ProductDetailsApi
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val dispatcher: CoroutineDispatcher, private val productDetailsApi: ProductDetailsApi
) : BaseViewModel() {
    private val _productDetails = MutableStateFlow<MenuItem?>(null)
    val productDetails = _productDetails.asStateFlow()

    fun fetchProductDetails(productId: String) = viewModelScope.launch(dispatcher) {
        coRunCatching { productDetailsApi.fetchProductDetails(productId) }.onSuccess {
            _productDetails.value = it
        }.onFailure { sendError(it) }
    }
}