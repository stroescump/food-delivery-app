package com.adelinarotaru.fooddelivery.customer.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.cart.models.CartMenuItem
import com.adelinarotaru.fooddelivery.databinding.ItemCartBinding
import com.adelinarotaru.fooddelivery.shared.base.BaseRVAdapter

class CartAdapter(private val updateTotal: (List<CartMenuItem>) -> Unit) :
    BaseRVAdapter<ItemCartBinding, CartMenuItem>() {

    override val refreshUi: (ItemCartBinding, CartMenuItem) -> Unit = { binding, cartItem ->
        with(binding) {
            val ctx = root.context
            quantity.text = cartItem.quantity.toString()
            foodName.text = cartItem.menuItem.name
            foodCategory.text = cartItem.menuItem.foodCategory
            price.text = ctx.getString(
                R.string.priceFormatter,
                cartItem.quantity.times(cartItem.menuItem.price)
            )

            addQuantity.setOnClickListener { increaseQuantity(cartItem) }
            subtractQuantity.setOnClickListener { decreaseQuantity(cartItem) }
        }
    }

    private fun increaseQuantity(
        cartItem: CartMenuItem
    ) = differ.currentList.map { if (it == cartItem) it.copy(quantity = it.quantity.inc()) else it }
        .also { updateTotal(it) }

    private fun decreaseQuantity(
        cartItem: CartMenuItem
    ) = differ.currentList.map { if (it == cartItem) it.copy(quantity = it.quantity.dec()) else it }
        .also { updateTotal(it) }

    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemCartBinding =
        ItemCartBinding::inflate

}
