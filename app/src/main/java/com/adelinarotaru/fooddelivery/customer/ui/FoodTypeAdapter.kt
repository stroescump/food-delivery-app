package com.adelinarotaru.fooddelivery.customer.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.ItemFoodTypeVhBinding

data class FoodTypeItem(
    val id: Int,
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int,
    val isSelected: Boolean = false
)

class FoodTypeAdapter(
    val onItemClicked: (FoodTypeItem) -> Unit
) : RecyclerView.Adapter<FoodTypeAdapter.FoodTypeVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTypeVH = FoodTypeVH(
        ItemFoodTypeVhBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: FoodTypeVH, position: Int) {
        val currentItem = differ.currentList[position]
        holder.refreshUi(currentItem)
    }

    inner class FoodTypeVH(private val binding: ItemFoodTypeVhBinding) : ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun refreshUi(foodItem: FoodTypeItem) {
            binding.apply {
                root.setOnClickListener { onItemClicked(foodItem) }
                val ctx = binding.root.context
                foodName.apply {
                    text = ctx.getString(foodItem.nameRes)
                    val black = ctx.getColor(R.color.black)
                    val dirtyWhite = ctx.getColor(R.color.dirty_white)
                    setTextColor(if (foodItem.isSelected) black else dirtyWhite)
                }
                foodImage.setImageResource(foodItem.iconRes)
                foodImage.background = ctx.getDrawable(
                    if (foodItem.isSelected) R.drawable.food_type_item_shadow_selected else R.drawable.food_type_item_shadow_unselected
                )
                root.background = ctx.getDrawable(
                    if (foodItem.isSelected) R.drawable.food_type_item_bg_selected else R.drawable.food_type_item_bg_unselected
                )

            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<FoodTypeItem>() {
        override fun areItemsTheSame(oldItem: FoodTypeItem, newItem: FoodTypeItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodTypeItem, newItem: FoodTypeItem): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
}