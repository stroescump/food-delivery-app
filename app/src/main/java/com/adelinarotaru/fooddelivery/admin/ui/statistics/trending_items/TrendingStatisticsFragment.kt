package com.adelinarotaru.fooddelivery.admin.ui.statistics.trending_items

import com.adelinarotaru.fooddelivery.databinding.FragmentTrendingStatisticsBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment

class TrendingStatisticsFragment :
    BaseFragment<FragmentTrendingStatisticsBinding, TrendingStatisticsViewModel>(
        FragmentTrendingStatisticsBinding::inflate
    ) {

    companion object {
        @JvmStatic
        fun newInstance() = TrendingStatisticsFragment()
    }

    override val viewModel: TrendingStatisticsViewModel by lazy {
        TrendingStatisticsViewModel(
            DependencyProvider.provideDispatcher()
        )
    }
}