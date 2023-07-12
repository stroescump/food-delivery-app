package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.startActivity
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.ItemCourierOrderAcceptedBinding
import com.adelinarotaru.fooddelivery.driver.models.CourierMenuItem
import com.adelinarotaru.fooddelivery.shared.BaseRVAdapter

class OrderAcceptedAdapter() : BaseRVAdapter<ItemCourierOrderAcceptedBinding, CourierMenuItem>() {
    override val refreshUi: (ItemCourierOrderAcceptedBinding, CourierMenuItem) -> Unit =
        { binding, courierMenuItem ->
            with(binding) {
                if (courierMenuItem.isPickedUp) {
                    root.isEnabled = false
                    root.background =
                        AppCompatResources.getDrawable(root.context, R.drawable.card_dark_gray_bg)
                } else {
                    root.isEnabled = true
                    root.background =
                        AppCompatResources.getDrawable(root.context, R.drawable.card_dirty_white_bg)
                }
                restaurantName.text = courierMenuItem.menuItem.restaurant.name
                restaurantAddress.text = "Placeholder"
                navigateToRestaurant.setOnClickListener {
                    launchGoogleMapsUsingCoordinates(
                        courierMenuItem.menuItem.restaurant.lat,
                        courierMenuItem.menuItem.restaurant.long,
                        courierMenuItem.menuItem.restaurant.name
                    )
                }
                markAsComplete.setOnClickListener {
                    val indexOfCurrentItem = differ.currentList.indexOf(courierMenuItem)
                    val updatedList = differ.currentList.toMutableList().apply {
                        this[indexOfCurrentItem] = courierMenuItem.copy(isPickedUp = true)
                    }
                    differ.submitList(updatedList)
                }
                orderDetails.text = courierMenuItem.menuItem.name
            }
        }

    private fun ItemCourierOrderAcceptedBinding.launchGoogleMapsUsingCoordinates(
        lat: String, long: String, restaurantName: String
    ) {
        val ctx = root.context
        val gmmIntentUri = Uri.parse("geo:0,0?q=$lat,$long($restaurantName)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(ctx.packageManager)?.let {
            startActivity(ctx, mapIntent, null)
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemCourierOrderAcceptedBinding =
        ItemCourierOrderAcceptedBinding::inflate
}