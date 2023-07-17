package com.adelinarotaru.fooddelivery.customer.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.cart.models.CartMenuItem
import com.adelinarotaru.fooddelivery.customer.checkout.models.CheckoutArgs
import com.adelinarotaru.fooddelivery.databinding.FragmentCartBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.Cart
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment :
    BaseFragment<FragmentCartBinding, CartViewModel>(FragmentCartBinding::inflate) {

    override val viewModel by lazy {
        CartViewModel(
            DependencyProvider.provideDispatcher(), DependencyProvider.provideCartApi()
        )
    }

    private val cartAdapter by lazy {
        CartAdapter { updatedCartItems ->
            val updatedCart = sharedViewModel.getCartState()?.copy(orderItems = updatedCartItems)
                ?: return@CartAdapter
            sharedViewModel.updateCart(updatedCart)
        }
    }
    private lateinit var checkoutArgs: CheckoutArgs

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            cartItems.adapter = cartAdapter
            goBack.setOnClickListener { findNavController().popBackStack() }
            goToCheckout.setOnClickListener { goToCheckout() }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.sessionState.collectLatest {
                updateCart(it.cartState)
            }
        }
    }

    private fun goToCheckout() =
        findNavController().navigate(CartFragmentDirections.cartToCheckout(checkoutArgs))

    private fun updateCart(cartState: Cart?) {
        val items = cartState?.orderItems ?: return
        cartAdapter.differ.submitList(items)
        val subtotal = getTotal(items)
        val deliveryFee = 5.99
        checkoutArgs = CheckoutArgs(
            subtotal = subtotal,
            delivery = deliveryFee,
            total = subtotal.plus(deliveryFee)
        )
        binding?.apply {
            subtotalAmount.text = getString(R.string.priceFormatter, subtotal)
            deliveryAmount.text = getString(R.string.priceFormatter, deliveryFee)
            totalAmount.text = getString(R.string.priceFormatter, subtotal.plus(deliveryFee))
        }
    }

    private fun getTotal(items: List<CartMenuItem>): Double = run {
        var total = 0.0
        items.forEach { total += it.quantity * it.menuItem.price }
        total
    }

}