package com.adelinarotaru.fooddelivery.customer.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.ItemRestaurantBinding
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import com.adelinarotaru.fooddelivery.utils.show

class RestaurantsAdapter(
    val onItemClicked: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantsAdapter.RestaurantVh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantVh = RestaurantVh(
        ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RestaurantVh, position: Int) {
        val currentItem = differ.currentList[position]
        holder.refreshUi(currentItem)
    }

    inner class RestaurantVh(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun refreshUi(restaurant: Restaurant) {
            binding.apply {
                root.setOnClickListener { onItemClicked(restaurant) }
                val ctx = binding.root.context
                restaurantName.text = restaurant.name
                restaurantImage.setImageResource(R.drawable.ic_image_placeholder)
                estimatedTime.text = ctx.getString(R.string.estimated_time_30_50_min)
                rating.text = restaurant.rating.toString()
                if (restaurant.freeDelivery) freeDelivery.show()
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}