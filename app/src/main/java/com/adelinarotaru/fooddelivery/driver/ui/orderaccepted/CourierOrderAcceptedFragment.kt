package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.databinding.FragmentCourierOrderAcceptedBinding
import com.adelinarotaru.fooddelivery.driver.data.CourierRepositoryImpl
import com.adelinarotaru.fooddelivery.driver.models.CourierMenuItem
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            screenTitle.text = "Order #${orderId}"
            orderProducts.adapter = productsAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderDetails.collectLatest { orderDetails ->
                orderDetails?.updateUi()
            }
        }
        viewModel.fetchOrderDetails("")
    }

    private fun List<CourierMenuItem>.updateUi() = with(binding) {
        productsAdapter.differ.submitList(this@updateUi)
    }

}

