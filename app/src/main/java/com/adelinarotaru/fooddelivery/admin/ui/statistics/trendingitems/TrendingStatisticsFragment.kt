package com.adelinarotaru.fooddelivery.admin.ui.statistics.trendingitems

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.admin.data.StatisticsRepositoryImpl
import com.adelinarotaru.fooddelivery.databinding.FragmentTrendingStatisticsBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TrendingStatisticsFragment :
    BaseFragment<FragmentTrendingStatisticsBinding, TrendingStatisticsViewModel>(
        FragmentTrendingStatisticsBinding::inflate
    ) {

    override val viewModel: TrendingStatisticsViewModel by lazy {
        TrendingStatisticsViewModel(
            DependencyProvider.provideDispatcher(),
            StatisticsRepositoryImpl(DependencyProvider.provideStatisticsApi())
        )
    }

    private val trendingAdapter by lazy { TrendingAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            trendingList.adapter = trendingAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.statistics.collectLatest { freshStatistics ->
                trendingAdapter.differ.submitList(freshStatistics)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchStatistics()
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrendingStatisticsFragment()
    }
}