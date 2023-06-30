package com.adelinarotaru.fooddelivery.admin.ui.statistics.trendingitems

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adelinarotaru.fooddelivery.admin.models.TrendingStatistics
import com.adelinarotaru.fooddelivery.databinding.ItemTrendingBinding
import com.adelinarotaru.fooddelivery.shared.BaseRVAdapter

class TrendingAdapter : BaseRVAdapter<ItemTrendingBinding, TrendingStatistics>() {
    override val refreshUi: (ItemTrendingBinding, TrendingStatistics) -> Unit =
        { binding, trendingItem ->
            with(binding) {
                this.itemTrendingName.text = trendingItem.dishName
                this.itemTrendingCategory.text =
                    root.context.getString(trendingItem.dishCategory.nameRes)
                this.orderAmount.text = trendingItem.howManyTimesWasOrdered.toString()
            }
        }
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemTrendingBinding =
        ItemTrendingBinding::inflate
}