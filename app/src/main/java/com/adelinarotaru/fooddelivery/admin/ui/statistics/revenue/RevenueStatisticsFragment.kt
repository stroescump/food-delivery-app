package com.adelinarotaru.fooddelivery.admin.ui.statistics.revenue

import com.adelinarotaru.fooddelivery.databinding.FragmentRevenueStatisticsBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment

class RevenueStatisticsFragment :
    BaseFragment<FragmentRevenueStatisticsBinding, RevenueStatisticsViewModel>(
        FragmentRevenueStatisticsBinding::inflate
    ) {


    companion object {
        @JvmStatic
        fun newInstance() = RevenueStatisticsFragment()
    }

    override val viewModel: RevenueStatisticsViewModel by lazy {
        RevenueStatisticsViewModel(
            DependencyProvider.provideDispatcher()
        )
    }
}