package com.adelinarotaru.fooddelivery.customer.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.checkout.models.CheckoutArgs
import com.adelinarotaru.fooddelivery.databinding.FragmentCartBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.Cart

class CartFragment :
    BaseFragment<FragmentCartBinding, CartViewModel>(FragmentCartBinding::inflate) {

    override val viewModel by lazy {
        CartViewModel(
            DependencyProvider.provideDispatcher(), DependencyProvider.provideCartApi()
        )
    }

    private val cartAdapter by lazy { CartAdapter() }
    private lateinit var checkoutArgs: CheckoutArgs

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            cartItems.adapter = cartAdapter
            goBack.setOnClickListener { findNavController().popBackStack() }
            goToCheckout.setOnClickListener { goToCheckout() }
        }

        updateCart(sharedViewModel.getCartState())
    }

    private fun goToCheckout() =
        findNavController().navigate(CartFragmentDirections.cartToCheckout(checkoutArgs))

    private fun updateCart(cartState: Cart?) {
        cartAdapter.differ.submitList(cartState?.orderItems)
        val subtotal = cartAdapter.getTotal()
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

}