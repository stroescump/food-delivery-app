package com.adelinarotaru.fooddelivery.customer.ui.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentCartBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment :
    BaseFragment<FragmentCartBinding, CartViewModel>(FragmentCartBinding::inflate) {

    companion object {
        fun newInstance() = CartFragment()
    }

    override val viewModel by lazy {
        CartViewModel(
            DependencyProvider.provideDispatcher(), DependencyProvider.provideCartApi()
        )
    }

    private val cartAdapter by lazy { CartAdapter() }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            cartItems.adapter = cartAdapter
            close.setOnClickListener { findNavController().popBackStack() }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartProducts.collectLatest { cartState ->
                cartAdapter.differ.submitList(cartState?.orderItems)
                val total = cartAdapter.getTotal()
                val deliveryFee = 5.99
                binding?.apply {
                    subtotalAmount.text = getString(R.string.priceFormatter, total)
                    deliveryAmount.text = getString(R.string.priceFormatter, deliveryFee)
                    totalAmount.text = getString(R.string.priceFormatter, total.plus(deliveryFee))
                }
            }
        }

        viewModel.fetchCartState()
    }

}