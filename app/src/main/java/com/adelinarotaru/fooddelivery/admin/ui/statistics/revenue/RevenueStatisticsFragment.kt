package com.adelinarotaru.fooddelivery.admin.ui.statistics.revenue

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.admin.data.StatisticsRepositoryImpl
import com.adelinarotaru.fooddelivery.databinding.FragmentRevenueStatisticsBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.patrykandpatrick.vico.core.axis.Axis
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
            DependencyProvider.provideDispatcher(),
            StatisticsRepositoryImpl(DependencyProvider.provideStatisticsApi())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.statistics.collectLatest { statistics ->
                    val statisticsMapped =
                        statistics?.map { it.totalRevenue }?.toTypedArray() ?: return@collectLatest
                    chartView.setModel(entryModelOf(entriesOf(*statisticsMapped)))
                }
            }
            (chartView.startAxis as Axis).guideline = null
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchStatistics()
    }
}