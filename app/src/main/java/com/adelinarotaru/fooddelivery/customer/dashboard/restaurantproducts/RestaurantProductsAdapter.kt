package com.adelinarotaru.fooddelivery.customer.dashboard.restaurantproducts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.LayoutItemMenuRestaurantBinding
import com.adelinarotaru.fooddelivery.shared.base.BaseRVAdapter
import com.adelinarotaru.fooddelivery.shared.models.MenuItem

class RestaurantProductsAdapter(private val onProductClicked: (MenuItem) -> Unit) :
    BaseRVAdapter<LayoutItemMenuRestaurantBinding, MenuItem>() {
    override val refreshUi: (LayoutItemMenuRestaurantBinding, MenuItem) -> Unit = { binding, item ->
        with(binding) {
            foodName.text = item.name
            price.text = item.price.toString()
            foodPicture.setImageDrawable(
                AppCompatResources.getDrawable(
                    root.context, R.drawable.salad
                )
            )
            root.setOnClickListener { onProductClicked(item) }
        }
    }
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> LayoutItemMenuRestaurantBinding =
        LayoutItemMenuRestaurantBinding::inflate
}