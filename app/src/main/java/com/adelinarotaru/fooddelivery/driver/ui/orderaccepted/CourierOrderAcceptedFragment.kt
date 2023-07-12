package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentCourierOrderAcceptedBinding
import com.adelinarotaru.fooddelivery.driver.data.CourierRepositoryImpl
import com.adelinarotaru.fooddelivery.driver.models.ClientDetails
import com.adelinarotaru.fooddelivery.driver.models.CourierMenuItem
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.utils.Constants
import com.adelinarotaru.fooddelivery.utils.launchGoogleMapsUsingCoordinates
import com.adelinarotaru.fooddelivery.utils.showMessageWindow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CourierOrderAcceptedFragment :
    BaseFragment<FragmentCourierOrderAcceptedBinding, CourierOrderAcceptedViewModel>(
        FragmentCourierOrderAcceptedBinding::inflate
    ) {

    override val viewModel: CourierOrderAcceptedViewModel by lazy {
        CourierOrderAcceptedViewModel(
            DependencyProvider.provideDispatcher(),
            CourierRepositoryImpl(DependencyProvider.provideCourierApi())
        )
    }

    private val argsLazy by navArgs<CourierOrderAcceptedFragmentArgs>()
    private val productsAdapter by lazy { OrderAcceptedAdapter() }
    private val orderId by lazy { "17826317SX" }
    private val clientDetails by lazy {
        ClientDetails(
            "Mark Manning", "Auwiesenstrasse 90", 47.413438, 8.570812
        )
    }

    fun doIfProductsPickedOrError(block: () -> Unit) {
        if (productsAdapter.areAllProductsPickedUp()) {
            block()
        } else showMessageWindow(
            requireContext(), getString(R.string.please_make_sure_to_pick_up_all_products)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            screenTitle.text = "Order #${orderId}"
            orderProducts.adapter = productsAdapter
            headToClientLocation.setOnClickListener {
                doIfProductsPickedOrError {
                    requireContext().launchGoogleMapsUsingCoordinates(
                        lat = clientDetails.latitude.toString(),
                        long = clientDetails.longitude.toString(),
                        locationName = clientDetails.name
                    )
                }
            }

            markOrderAsDelivered.setOnClickListener {
                doIfProductsPickedOrError {
                    viewModel.markOrderDelivered(
                        orderId
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.orderDetails.collectLatest { orderDetails ->
                    orderDetails?.updateUi()
                }
            }

            launch {
                viewModel.navigateToSuccess.collectLatest { orderDelivered ->
                    if (orderDelivered) navigateToSuccess()
                }
            }
        }
        viewModel.fetchOrderDetails(orderId)
    }

    private fun navigateToSuccess() =
        findNavController().navigate(CourierOrderAcceptedFragmentDirections.goToSuccess(Constants.COURIER))

    private fun List<CourierMenuItem>.updateUi() = productsAdapter.differ.submitList(this)
}

