package com.adelinarotaru.fooddelivery.customer.checkout

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.checkout.data.CheckoutRepositoryImpl
import com.adelinarotaru.fooddelivery.customer.checkout.models.OrderItem
import com.adelinarotaru.fooddelivery.customer.checkout.models.OrderRequest
import com.adelinarotaru.fooddelivery.customer.trackorder.models.TrackOrderArgs
import com.adelinarotaru.fooddelivery.databinding.FragmentCheckoutBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.showError
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
            subtotalAmount.text = getString(R.string.priceFormatter, navArgs.checkoutArgs.subtotal)
            deliveryAmount.text = getString(R.string.priceFormatter, navArgs.checkoutArgs.delivery)
            totalAmount.text = getString(R.string.priceFormatter, navArgs.checkoutArgs.total)

            payOrder.setOnClickListener {
                runCatching {
                    val allDataValid =
                        provideViewsForValidation().map { it.validateRule(listOf(EmptyTextRule())) }
                            .all { it }
                    if (allDataValid) {
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
                    } else showError(Throwable("Please make sure you entered correct data."))

                }.onFailure { showError(it) }
            }

            provideViewsForValidation().onEach { view ->
                view.doAfterTextChanged {
                    view.validateRule(listOf(EmptyTextRule())) { editTextUIModel ->
                        showError(Throwable(editTextUIModel.errorMessage))
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderPlaced.collectLatest { orderId ->
                orderId?.let { navigateToTrackingOrder(orderId).also { sharedViewModel.clearCartState() } }
            }
        }
    }

    private fun FragmentCheckoutBinding.provideViewsForValidation() =
        listOf(
            street,
            city,
            zipCode,
            layoutCardDetails.cardNumber,
            layoutCardDetails.cvv,
            layoutCardDetails.expiryMonth,
            layoutCardDetails.firstName
        )

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