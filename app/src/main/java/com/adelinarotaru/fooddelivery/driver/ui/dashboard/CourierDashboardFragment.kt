package com.adelinarotaru.fooddelivery.driver.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentCourierDashboardBinding
import com.adelinarotaru.fooddelivery.driver.data.CourierRepositoryImpl
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.setHorizontalCorners
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CourierDashboardFragment :
    BaseFragment<FragmentCourierDashboardBinding, CourierDashboardViewModel>(
        FragmentCourierDashboardBinding::inflate
    ) {
    private lateinit var courierTasksAdapter: CourierItemTaskAdapter
    private lateinit var courierTaskFilterAdapter: CourierTaskFilterAdapter

    override val viewModel by lazy {
        CourierDashboardViewModel(
            CourierRepositoryImpl(
                DependencyProvider.provideCourierApi()
            )
        )
    }

    private val args by navArgs<CourierDashboardFragmentArgs>()
    private val courierId by lazy { args.courierDashboardArgs.id }
    private val courierName by lazy { args.courierDashboardArgs.name }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courierTasksAdapter = CourierItemTaskAdapter { taskClicked ->
            sharedViewModel.courierItemTask = taskClicked
            findNavController().navigate(
                CourierDashboardFragmentDirections.dashboardToOrderAccepted()
            )
        }.also { it.differ.submitList(emptyList()) }

        courierTaskFilterAdapter = CourierTaskFilterAdapter { filterClicked ->
            val updatedList = updateSelectedState(filterClicked)
            val orderStatus =
                updatedList.find { it.isSelected }?.taskStatus?.toOrderStatus()
                    ?: OrderStatus.ALL
            fetchOrdersFrom(orderStatus)
            courierTaskFilterAdapter.differ.submitList(updatedList)
        }.also {
            initTasksAdapter(it)
        }

        binding?.apply {
            val currentDate = LocalDate.now()
            val dateFormat = DateTimeFormatter.ofPattern("dd-MMM, yyyy", Locale.ENGLISH)

            courierTasksLayout.apply {
                tasksList.apply {
                    adapter = courierTasksAdapter
                }
                tasksFilter.apply {
                    setHorizontalCorners()
                    adapter = courierTaskFilterAdapter
                }
            }

            courierStatusLayout.courierName.text = courierName
            courierStatusLayout.courierId.text = courierId
            courierStatusLayout.courierStatusBubble.text = getString(R.string.online)
            courierStatusLayout.dayOfWeekTitle.text = currentDate.dayOfWeek.name
            courierStatusLayout.dayOfWeekSubtitle.text = currentDate.format(dateFormat)

            pressToRefresh.setOnClickListener {
                fetchOrdersFrom(courierTaskFilterAdapter.getSelectedStatus())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courierOrders.collectLatest {
                courierTasksAdapter.differ.submitList(it)
            }
        }

        viewModel.fetchAllOrders(courierId)
    }

    private fun fetchOrdersFrom(orderStatus: OrderStatus) {
        when (orderStatus) {
            OrderStatus.ALL -> viewModel.fetchAllOrders(courierId)

            OrderStatus.PREPARING, OrderStatus.PICKED_UP -> {
                viewModel.fetchAcceptedOrders(courierId)
            }

            OrderStatus.DELIVERED, OrderStatus.ORDER_RECEIVED -> {
                viewModel.fetchNearbyOrders(orderStatus)
            }
        }
    }

    private fun updateSelectedState(filterClicked: ItemTaskFilter) =
        courierTaskFilterAdapter.differ.currentList.toMutableList().map {
            it.copy(isSelected = if (it.id == filterClicked.id) it.isSelected.not() else false)
        }.apply {
            if (none { it.isSelected }) {
                map { it.copy(isSelected = it.id == 0) }
            }
        }

    private fun initTasksAdapter(it: CourierTaskFilterAdapter) {
        it.differ.submitList(
            mutableListOf(
                ItemTaskFilter(TaskStatus.ALL, 0, isSelected = true),
                ItemTaskFilter(TaskStatus.PENDING, 1),
                ItemTaskFilter(TaskStatus.ACCEPTED, 2),
                ItemTaskFilter(TaskStatus.DONE, 3),
            )
        )
    }
}
