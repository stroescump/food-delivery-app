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

            fallbackToDefaultIfNoneSelected(updatedList)

            val updatedOrderList = getFilteredOrderListByStatus(filterClicked)

            courierTaskFilterAdapter.differ.submitList(updatedList)
            courierTasksAdapter.differ.submitList(if (updatedList.none { it.isSelected }) viewModel.courierOrders.value else updatedOrderList)
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

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courierOrders.collectLatest {
                courierTasksAdapter.differ.submitList(it)
            }
        }

        viewModel.fetchNearbyOrders(OrderStatus.ORDER_RECEIVED)
    }

    private fun updateSelectedState(filterClicked: ItemTaskFilter) =
        courierTaskFilterAdapter.differ.currentList.toMutableList().map {
            it.copy(isSelected = if (it.id == filterClicked.id) it.isSelected.not() else false)
        }

    private fun fallbackToDefaultIfNoneSelected(updatedList: List<ItemTaskFilter>) {
        if (updatedList.none { it.isSelected }) updatedList.toMutableList().map {
            it.copy(isSelected = it.id == 0)
        }
    }

    private fun getFilteredOrderListByStatus(filterClicked: ItemTaskFilter) =
        if (filterClicked.taskStatus == TaskStatus.ALL && filterClicked.isSelected.not()) viewModel.courierOrders.value else viewModel.courierOrders.value.toMutableList()
            .filter {
                when (filterClicked.taskStatus) {
                    TaskStatus.ACCEPTED -> it.orderStatus == OrderStatus.PREPARING.orderStep
                    TaskStatus.REJECTED -> it.orderStatus == OrderStatus.REJECTED.orderStep
                    TaskStatus.PENDING -> it.orderStatus == OrderStatus.ORDER_RECEIVED.orderStep
                    TaskStatus.DONE -> it.orderStatus == OrderStatus.DELIVERED.orderStep
                    else -> false
                }
            }

    private fun initTasksAdapter(it: CourierTaskFilterAdapter) {
        it.differ.submitList(
            mutableListOf(
                ItemTaskFilter(TaskStatus.ALL, 0, isSelected = true),
                ItemTaskFilter(TaskStatus.PENDING, 1),
                ItemTaskFilter(TaskStatus.ACCEPTED, 2),
                ItemTaskFilter(TaskStatus.REJECTED, 3),
            )
        )
    }

}
