package com.adelinarotaru.fooddelivery.driver.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.databinding.FragmentCourierDashboardBinding
import com.adelinarotaru.fooddelivery.driver.data.CourierRepositoryImpl
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CourierDashboardFragment :
    BaseFragment<FragmentCourierDashboardBinding, CourierDashboardViewModel>(
        FragmentCourierDashboardBinding::inflate
    ) {
    override var binding: FragmentCourierDashboardBinding? = null
    private lateinit var courierTasksAdapter: CourierItemTaskAdapter
    private lateinit var courierTaskFilterAdapter: CourierItemTaskAdapter

    override val viewModel = CourierDashboardViewModel(
        CourierRepositoryImpl(
            DependencyProvider.provideCourierApi()
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        courierTasksAdapter = CourierItemTaskAdapter().also { it.differ.submitList(emptyList()) }
        courierTaskFilterAdapter =
            CourierItemTaskAdapter().also { it.differ.submitList(emptyList()) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding?.courierTasksLayout?.apply {
            tasksList.adapter = courierTasksAdapter
            tasksFilter.adapter = courierTaskFilterAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courierOrders.collectLatest {
                courierTasksAdapter.differ.submitList(it)
            }
        }

        viewModel.fetchNearbyOrders()
    }

}
