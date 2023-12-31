package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.ItemCourierOrderAcceptedBinding
import com.adelinarotaru.fooddelivery.driver.models.CourierMenuItem
import com.adelinarotaru.fooddelivery.shared.base.BaseRVAdapter

class OrderAcceptedAdapter : BaseRVAdapter<ItemCourierOrderAcceptedBinding, CourierMenuItem>() {
    fun areAllProductsPickedUp() = differ.currentList.all { it.isPickedUp }

    override val refreshUi: (ItemCourierOrderAcceptedBinding, CourierMenuItem) -> Unit =
        { binding, courierMenuItem ->
            with(binding) {
                val ctx = root.context
                if (courierMenuItem.isPickedUp) {
                    with(root) {
                        isEnabled = false
                        markAsComplete.background = AppCompatResources.getDrawable(
                            ctx, R.drawable.card_dirty_white_bg
                        )
                        restaurantName.setTextColor(ctx.getColor(R.color.dirty_white))
                        restaurantPhone.setTextColor(ctx.getColor(R.color.dirty_white))
                        orderDetails.setTextColor(ctx.getColor(R.color.dirty_white))
                        background = AppCompatResources.getDrawable(
                            ctx, R.drawable.card_crazy_green_bg_20dp
                        )
                    }
                } else {
                    with(root) {
                        isEnabled = true
                        markAsComplete.background = AppCompatResources.getDrawable(
                            ctx, R.drawable.card_crazy_green_bg_20dp
                        )
                        background = AppCompatResources.getDrawable(
                            ctx, R.drawable.card_dirty_white_bg
                        )
                        restaurantName.setTextColor(ctx.getColor(R.color.dark_blue_bg))
                        restaurantPhone.setTextColor(ctx.getColor(R.color.dark_blue_bg))
                        orderDetails.setTextColor(ctx.getColor(R.color.dark_blue_bg))
                    }
                }
                restaurantName.text = courierMenuItem.restaurantInfo.name
                restaurantPhone.text = courierMenuItem.restaurantInfo.phoneNumber
                markAsComplete.setOnClickListener {
                    val indexOfCurrentItem = differ.currentList.indexOf(courierMenuItem)
                    val updatedList = differ.currentList.toMutableList().apply {
                        this[indexOfCurrentItem] = courierMenuItem.copy(isPickedUp = true)
                    }
                    differ.submitList(updatedList)
                }
                val dishBuilder = StringBuilder()
                courierMenuItem.restaurantInfo.dishes.onEach {
                    dishBuilder.appendLine(it)
                    orderDetails.text = dishBuilder.toString()
                }
            }
        }

    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemCourierOrderAcceptedBinding =
        ItemCourierOrderAcceptedBinding::inflate
}