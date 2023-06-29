package com.adelinarotaru.fooddelivery.admin.ui.statistics.orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.admin.data.StatisticsRepositoryImpl
import com.adelinarotaru.fooddelivery.admin.models.OrderStatistic
import com.adelinarotaru.fooddelivery.databinding.FragmentOrdersStatisticsBinding
import com.adelinarotaru.fooddelivery.databinding.ItemOrderBinding
import com.adelinarotaru.fooddelivery.shared.BaseRVAdapter
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersStatisticsFragment :
    BaseFragment<FragmentOrdersStatisticsBinding, OrderStatisticsViewModel>(
        FragmentOrdersStatisticsBinding::inflate
    ) {
    override val viewModel: OrderStatisticsViewModel by lazy {
        OrderStatisticsViewModel(
            DependencyProvider.provideDispatcher(),
            StatisticsRepositoryImpl(DependencyProvider.provideStatisticsApi())
        )
    }

    private lateinit var statisticsJob: Job
    private lateinit var statisticsAdapter: OrderStatisticsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticsAdapter = OrderStatisticsAdapter()
        binding?.orders?.adapter = statisticsAdapter
    }

    override fun onResume() {
        super.onResume()
        statisticsJob =
            viewLifecycleOwner.lifecycleScope.launch(DependencyProvider.provideDispatcher()) {
                launch {
                    viewModel.statistics.collectLatest { statistic ->
                        withContext(Dispatchers.Main) {
                            statisticsAdapter.differ.submitList(statistic)
                        }
                    }
                }
                launch {
                    viewModel.fetchStatistics()
                }
            }
    }

    override fun onPause() {
        super.onPause()
        statisticsJob.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance(): OrdersStatisticsFragment = OrdersStatisticsFragment()
    }

}

class OrderStatisticsAdapter : BaseRVAdapter<ItemOrderBinding, OrderStatistic>() {
    @SuppressLint("SetTextI18n")
    override val refreshUi: (ItemOrderBinding, OrderStatistic) -> Unit = { binding, statistics ->
        with(binding) {
            orderDate.text = statistics.orderDate
            customerName.text = statistics.firstName.plus(" ").plus(statistics.lastName)
            orderAmount.text = "+ $".plus(statistics.amount)
            if (statistics.isNewOrder) newOrderStatus.show()
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemOrderBinding =
        ItemOrderBinding::inflate
}
