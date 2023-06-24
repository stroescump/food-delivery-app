package com.adelinarotaru.fooddelivery.driver.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.databinding.FragmentCourierDashboardBinding
import com.adelinarotaru.fooddelivery.driver.data.CourierRepositoryImpl
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.setHorizontalCorners
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CourierDashboardFragment :
    BaseFragment<FragmentCourierDashboardBinding, CourierDashboardViewModel>(
        FragmentCourierDashboardBinding::inflate
    ) {
    override var binding: FragmentCourierDashboardBinding? = null
    private lateinit var courierTasksAdapter: CourierItemTaskAdapter
    private lateinit var courierTaskFilterAdapter: CourierTaskFilterAdapter

    override val viewModel by lazy {
        CourierDashboardViewModel(
            CourierRepositoryImpl(
                DependencyProvider.provideCourierApi()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courierTasksAdapter = CourierItemTaskAdapter().also { it.differ.submitList(emptyList()) }
        courierTaskFilterAdapter = CourierTaskFilterAdapter { filterClicked ->
            val updatedList = updateSelectedState(filterClicked)

            fallbackToDefaultIfNoneSelected(updatedList)

            val updatedOrderList = getFilteredOrderListByStatus(filterClicked)

            courierTaskFilterAdapter.differ.submitList(updatedList)
            courierTasksAdapter.differ.submitList(if (updatedList.none { it.isSelected }) viewModel.courierOrders.value else updatedOrderList)
        }.also {
            initTasksAdapter(it)
        }

        binding?.courierTasksLayout?.apply {
            tasksList.apply {
                adapter = courierTasksAdapter
            }
            tasksFilter.apply {
                setHorizontalCorners()
                adapter = courierTaskFilterAdapter
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.courierOrders.collectLatest {
                    courierTasksAdapter.differ.submitList(it)
                }
            }
            launch {
                viewModel.courierProfile.collectLatest { userProfile ->
                    binding?.apply {
                        userProfile?.let {
                            courierStatusLayout.courierName.text = it.name
                            courierStatusLayout.courierId.text = "PX 23445"
                            courierStatusLayout.courierStatusBubble.text = "ONLINE"
                            courierStatusLayout.dayOfWeekTitle.text = "Monday"
                            courierStatusLayout.dayOfWeekSubtitle.text = "Jun 23, 2023"
                            
                        }
                    }
                }
            }
        }

        viewModel.fetchNearbyOrders()
    }

    private fun updateSelectedState(filterClicked: ItemTaskFilter) =
        courierTaskFilterAdapter.differ.currentList.toMutableList().map {
            it.copy(isSelected = if (it.id == filterClicked.id) it.isSelected.not() else false)
        }

    private fun fallbackToDefaultIfNoneSelected(updatedList: List<ItemTaskFilter>) {
        if (updatedList.none { it.isSelected }) updatedList.toMutableList().map {
            it.copy(isSelected = it.id == "0")
        }
    }

    private fun getFilteredOrderListByStatus(filterClicked: ItemTaskFilter) =
        if (filterClicked.taskStatus == TaskStatus.ALL && filterClicked.isSelected.not()) viewModel.courierOrders.value else viewModel.courierOrders.value.toMutableList()
            .filter {
                when (filterClicked.taskStatus) {
                    TaskStatus.ACCEPTED -> it.orderStatus == OrderStatus.PREPARING
                    TaskStatus.REJECTED -> it.orderStatus == OrderStatus.REJECTED
                    TaskStatus.PENDING -> it.orderStatus == OrderStatus.ORDER_RECEIVED
                    TaskStatus.DONE -> it.orderStatus == OrderStatus.DELIVERED
                    else -> false
                }
            }

    private fun initTasksAdapter(it: CourierTaskFilterAdapter) {
        it.differ.submitList(
            mutableListOf(
                ItemTaskFilter(TaskStatus.ALL, "0", isSelected = true),
                ItemTaskFilter(TaskStatus.PENDING, "1"),
                ItemTaskFilter(TaskStatus.ACCEPTED, "2"),
                ItemTaskFilter(TaskStatus.REJECTED, "3"),
            )
        )
    }

}
