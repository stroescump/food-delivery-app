package com.adelinarotaru.fooddelivery.customer.ui.cart

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            cartItems.adapter = cartAdapter
            close.setOnClickListener { findNavController().popBackStack() }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartProducts.collectLatest { cartState ->
                cartAdapter.differ.submitList(cartState?.orderItems)
            }
        }

        viewModel.fetchCartState()
    }

}