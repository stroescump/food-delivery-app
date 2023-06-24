package com.adelinarotaru.fooddelivery.driver.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.adelinarotaru.fooddelivery.databinding.ItemCourierTaskBinding
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus

data class CourierItemTask(
    val orderId: String,
    val orderStatus: OrderStatus,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val restaurantName: String,
    val restaurantAddress: String
)

class CourierItemTaskAdapter : RecyclerView.Adapter<CourierItemTaskAdapter.CourierItemTaskVH>() {
    inner class CourierItemTaskVH(private val binding: ItemCourierTaskBinding) :
        ViewHolder(binding.root) {
        fun refreshUi(currentTask: CourierItemTask) {
            with(binding) {
                orderId.text = currentTask.orderId
                courierStatusBubble.text = "PICK UP"
                customerName.text = currentTask.customerName
                customerAddress.text = currentTask.customerAddress
                customerPhone.text = currentTask.customerPhone
                restaurantAddress.text = currentTask.restaurantAddress
                restaurantName.text = currentTask.restaurantName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourierItemTaskVH =
        CourierItemTaskVH(
            ItemCourierTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CourierItemTaskVH, position: Int) {
        val currentTask = differ.currentList[position]
        holder.refreshUi(currentTask)
    }

    private val differCallback = object : DiffUtil.ItemCallback<CourierItemTask>() {
        override fun areItemsTheSame(oldItem: CourierItemTask, newItem: CourierItemTask): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(
            oldItem: CourierItemTask, newItem: CourierItemTask
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}