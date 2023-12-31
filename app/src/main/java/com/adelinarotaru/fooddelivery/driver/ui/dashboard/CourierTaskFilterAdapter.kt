package com.adelinarotaru.fooddelivery.driver.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.ItemPillShapeBinding
import com.adelinarotaru.fooddelivery.shared.base.BaseRVAdapter
import com.adelinarotaru.fooddelivery.shared.base.ItemAdapter
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus

enum class TaskStatus {
    ACCEPTED, PENDING, DONE, ALL
}

fun TaskStatus.toOrderStatus() = when (this) {
    TaskStatus.ACCEPTED -> OrderStatus.PREPARING
    TaskStatus.PENDING -> OrderStatus.ORDER_RECEIVED
    TaskStatus.DONE -> OrderStatus.DELIVERED
    TaskStatus.ALL -> OrderStatus.ALL
}

data class ItemTaskFilter(
    val taskStatus: TaskStatus = TaskStatus.PENDING,
    override val id: Int,
    val isSelected: Boolean = false
) : ItemAdapter

class CourierTaskFilterAdapter(onFilterClick: (ItemTaskFilter) -> Unit) :
    BaseRVAdapter<ItemPillShapeBinding, ItemTaskFilter>() {
    override val refreshUi: (ItemPillShapeBinding, ItemTaskFilter) -> Unit =
        { binding: ItemPillShapeBinding, itemFilter: ItemTaskFilter ->
            with(binding) {
                root.setOnClickListener { onFilterClick(itemFilter) }
                val ctx = root.context
                taskStatusBubble.background =
                    ResourcesCompat.getDrawable(
                        ctx.resources,
                        if (itemFilter.isSelected) R.drawable.food_type_item_bg_selected else R.drawable.card_dark_gray_bg,
                        ctx.theme
                    )
                taskStatusBubble.apply {
                    setTextColor(ctx.getColor(if (itemFilter.isSelected) R.color.black else R.color.dark_gray))
                    text =
                        itemFilter.taskStatus.name.lowercase().replaceFirstChar { it.uppercase() }
                }
            }
        }
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemPillShapeBinding =
        ItemPillShapeBinding::inflate

    fun getSelectedStatus() = differ.currentList.find { it.isSelected }?.taskStatus?.toOrderStatus() ?: OrderStatus.ALL
}