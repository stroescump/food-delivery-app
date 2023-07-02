package com.adelinarotaru.fooddelivery.customer.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adelinarotaru.fooddelivery.customer.models.CartMenuItem
import com.adelinarotaru.fooddelivery.databinding.ItemCartBinding
import com.adelinarotaru.fooddelivery.shared.BaseRVAdapter

class CartAdapter : BaseRVAdapter<ItemCartBinding, CartMenuItem>() {

    override val refreshUi: (ItemCartBinding, CartMenuItem) -> Unit = { binding, cartItem ->
        with(binding) {
            quantity.text = cartItem.quantity.toString()
            foodName.text = cartItem.menuItem.name
            foodCategory.text = cartItem.menuItem.foodCategory
            price.text = cartItem.quantity.times(cartItem.menuItem.price).toString()
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemCartBinding =
        ItemCartBinding::inflate

}
