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
                val ctx = root.context
                this.itemTrendingName.text = trendingItem.dishName
                this.itemTrendingCategory.text =
                    ctx.getString(trendingItem.dishCategory.nameRes)
                this.itemTrending.setImageDrawable(ctx.getDrawable(trendingItem.dishCategory.iconRes))
                this.orderAmount.text = trendingItem.howManyTimesWasOrdered.toString()
            }
        }
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemTrendingBinding =
        ItemTrendingBinding::inflate
}