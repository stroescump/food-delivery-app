package com.adelinarotaru.fooddelivery.customer.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.databinding.FragmentCheckoutBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckoutFragment :
    BaseFragment<FragmentCheckoutBinding, CheckoutViewModel>(FragmentCheckoutBinding::inflate) {

    companion object {
        fun newInstance() = CheckoutFragment()
    }

    override val viewModel: CheckoutViewModel by lazy {
        CheckoutViewModel(
            DependencyProvider.provideDispatcher(), DependencyProvider.provideCheckoutApi()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderPlaced.collectLatest { orderSuccess ->
                orderSuccess?.let { navigateToTrackingOrder() }
            }
        }

        viewModel.placeOrder()
    }

    private fun navigateToTrackingOrder() {
        TODO("Not yet implemented")
    }

}