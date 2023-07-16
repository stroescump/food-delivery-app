package com.adelinarotaru.fooddelivery.customer.checkout

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.customer.checkout.data.CheckoutRepositoryImpl
import com.adelinarotaru.fooddelivery.customer.checkout.models.OrderItem
import com.adelinarotaru.fooddelivery.customer.checkout.models.OrderRequest
import com.adelinarotaru.fooddelivery.customer.trackorder.models.TrackOrderArgs
import com.adelinarotaru.fooddelivery.databinding.FragmentCheckoutBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckoutFragment :
    BaseFragment<FragmentCheckoutBinding, CheckoutViewModel>(FragmentCheckoutBinding::inflate) {

    override val viewModel: CheckoutViewModel by lazy {
        CheckoutViewModel(
            DependencyProvider.provideDispatcher(),
            CheckoutRepositoryImpl(DependencyProvider.provideCheckoutApi())
        )
    }

    private val navArgs by navArgs<CheckoutFragmentArgs>()
    private lateinit var deliveryAddress: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            goBack.setOnClickListener { findNavController().popBackStack() }
            subtotalAmount.text = navArgs.checkoutArgs.subtotal.toString()
            deliveryAmount.text = navArgs.checkoutArgs.delivery.toString()
            totalAmount.text = navArgs.checkoutArgs.total.toString()

            payOrder.setOnClickListener {
                deliveryAddress = getAddressFromUi()
                val cartItems = sharedViewModel.getCartItems() ?: return@setOnClickListener
                viewModel.placeOrder(
                    OrderRequest(
                        status = OrderStatus.ORDER_RECEIVED.orderStep,
                        orderItems = cartItems.map {
                            OrderItem(
                                menuItemId = it.menuItem.id,
                                restaurantId = it.menuItem.restaurantId,
                                quantity = it.quantity
                            )
                        },
                        address = deliveryAddress
                    ),
                    userId = sharedViewModel.getUserId(),
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderPlaced.collectLatest { orderId ->
                orderId?.let { navigateToTrackingOrder(orderId) }
            }
        }
    }

    private fun FragmentCheckoutBinding.getAddressFromUi() =
        street.text.toString().plus(", ").plus(city.text.toString()).plus(", ")
            .plus(zipCode.text.toString())

    private fun navigateToTrackingOrder(orderId: String) {
        findNavController().navigate(
            CheckoutFragmentDirections.checkoutToTrackOrder(
                TrackOrderArgs(
                    orderId, deliveryAddress
                )
            )
        )
    }

}