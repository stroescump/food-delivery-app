package com.adelinarotaru.fooddelivery.admin.ui.statistics.revenue

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.color
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.admin.data.StatisticsRepositoryImpl
import com.adelinarotaru.fooddelivery.databinding.FragmentRevenueStatisticsBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.patrykandpatrick.vico.core.axis.Axis
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.chart.segment.SegmentProperties
import com.patrykandpatrick.vico.core.component.OverlayingComponent
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.extension.sumOf
import com.patrykandpatrick.vico.core.extension.transformToSpannable
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.views.chart.ChartView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RevenueStatisticsFragment :
    BaseFragment<FragmentRevenueStatisticsBinding, RevenueStatisticsViewModel>(
        FragmentRevenueStatisticsBinding::inflate
    ) {

    companion object {
        @JvmStatic
        fun newInstance() = RevenueStatisticsFragment()
        private val DAYS_OF_WEEK_HASHMAP = mapOf(
            0 to "Mon", 1 to "Tue", 2 to "Wed", 3 to "Thu", 4 to "Fri", 5 to "Sat", 6 to "Sun"
        )
        private val indicatorInnerComponent = ShapeComponent(Shapes.pillShape, R.color.dark_blue_bg)
        private val indicatorCenterComponent =
            ShapeComponent(Shapes.pillShape, R.color.dark_blue_bg)
        private val indicatorOuterComponent = ShapeComponent(Shapes.pillShape, R.color.crazy_green)
        private val indicator = OverlayingComponent(
            outer = indicatorOuterComponent, inner = OverlayingComponent(
                outer = indicatorCenterComponent, inner = indicatorInnerComponent, 2f
            ), innerPaddingAllDp = 1f
        )
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
            chartView.provideMarkers(
                marker = createMarker()
            )

            chartView.chart

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.statistics.collectLatest { statistics ->
                    val statisticsMapped =
                        statistics?.map { it.totalRevenue }?.toTypedArray() ?: return@collectLatest
                    chartView.setModel(entryModelOf(entriesOf(*statisticsMapped)))
                }
            }
            (chartView.startAxis as Axis).guideline = null
            (chartView.startAxis as VerticalAxis).apply {
                maxLabelCount = 3
                valueFormatter = AxisValueFormatter { value, chartValues ->
                    "$ ${value.toInt()}"
                }
            }
            (chartView.bottomAxis as HorizontalAxis).valueFormatter =
                AxisValueFormatter { value: Float, _ ->
                    DAYS_OF_WEEK_HASHMAP[value.toInt()]
                        ?: throw IllegalArgumentException("Day is not mapped!")
                }
        }
    }

    private fun createMarker() = object : MarkerComponent(
        label = TextComponent.Builder().apply {
            typeface = ResourcesCompat.getFont(
                requireContext(), R.font.creato_display_bold
            )
            margins = MutableDimensions(0f, 0f, 0f, 20f)
        }.build(), guideline = null, indicator = indicator
    ) {
        init {
            labelFormatter = provideCustomLabelMarkerFormatter()
            indicatorSizeDp = 15f
            onApplyEntryColor = { entryColor ->
                indicatorOuterComponent.color = entryColor.copyColor(32)
                with(indicatorCenterComponent) {
                    color = entryColor
                    setShadow(radius = 12f, color = entryColor)
                }
            }
        }

        override fun getInsets(
            context: MeasureContext, outInsets: Insets, segmentProperties: SegmentProperties
        ) = with(context) {
            outInsets.top =
                label.getHeight(context) + MarkerCorneredShape(Corner.FullyRounded).tickSizeDp.pixels + 4f.pixels * 1.3f - 2f.pixels
        }
    }

    private fun ChartView.provideMarkers(marker: Marker) = chart?.setPersistentMarkers(
        mapOf(
            0f to marker,
            1f to marker,
            2f to marker,
            3f to marker,
            4f to marker,
            5f to marker,
            6f to marker,
        )
    )

    private fun provideCustomLabelMarkerFormatter() = object : MarkerLabelFormatter {
        private val PATTERN = "$%5.0f"
        override fun getLabel(
            markedEntries: List<Marker.EntryModel>,
        ): CharSequence = markedEntries.transformToSpannable(
            prefix = if (markedEntries.size > 1) PATTERN.format(markedEntries.sumOf { it.entry.y }) + " (" else "",
            postfix = if (markedEntries.size > 1) ")" else "",
            separator = "; ",
        ) { model ->
            color(requireContext().getColor(R.color.dark_gray_3)) {
                append(
                    PATTERN.format(model.entry.y)
                )
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchStatistics()
    }
}