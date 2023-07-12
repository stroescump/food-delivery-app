package com.adelinarotaru.fooddelivery.customer.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.models.CartMenuItem
import com.adelinarotaru.fooddelivery.databinding.ItemCartBinding
import com.adelinarotaru.fooddelivery.shared.BaseRVAdapter

class CartAdapter : BaseRVAdapter<ItemCartBinding, CartMenuItem>() {

    fun getTotal(): Double = run {
        var total = 0.0
        differ.currentList.forEach { total += it.quantity * it.menuItem.price }
        total
    }

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
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemCartBinding =
        ItemCartBinding::inflate

}
