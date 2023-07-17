package com.adelinarotaru.fooddelivery.customer.checkout

import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.checkout.domain.CheckoutRepository
import com.adelinarotaru.fooddelivery.customer.checkout.models.OrderRequest
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.function.Predicate

class CheckoutViewModel(
    private val dispatcher: CoroutineDispatcher, private val checkoutRepository: CheckoutRepository
) : BaseViewModel() {

    private val _orderPlaced = MutableStateFlow<String?>(null)
    val orderPlaced = _orderPlaced.asStateFlow()

    fun placeOrder(orderRequest: OrderRequest, userId: String) = viewModelScope.launch(dispatcher) {
        coRunCatching {
            checkoutRepository.placeOrder(orderRequest, userId)
        }.onSuccess {
            _orderPlaced.value = it.orderId
        }.onFailure { sendError(it) }
    }
}

interface IValidation {
    val rule: String
}

data class EditTextUIModel(
    val errorMessage: String? = null
)

open class ValidationRule(
    @StringRes open val errorMessage: Int = R.string.this_field_is_required,
    val predicate: Predicate<String?>
) {
    companion object {
        const val PHONE_LENGTH = 10
    }
}

class EmptyTextRule : ValidationRule(predicate = { it.isNullOrEmpty() })

fun AppCompatEditText.validateRule(
    rules: List<ValidationRule>, callback: ((EditTextUIModel) -> Unit)? = null
): Boolean {
    val validateText = this.text.toString().trim()
    for (i in rules.indices) {
        val isNotValid = rules[i].predicate.test(validateText)
        val message = rules[i].errorMessage
        if (isNotValid) {
            callback?.invoke(EditTextUIModel(context.resources.getString(message)))
            return false
        } else {
            continue
        }
    }
    return true
}
