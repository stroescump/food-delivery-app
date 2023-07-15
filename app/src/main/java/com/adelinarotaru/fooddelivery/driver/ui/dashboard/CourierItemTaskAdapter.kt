package com.adelinarotaru.fooddelivery.driver.ui.dashboard

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.adelinarotaru.fooddelivery.databinding.ItemCourierTaskBinding
import com.adelinarotaru.fooddelivery.shared.login.domain.ILocation
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourierItemTask(
    val orderId: String,
    val orderStatus: Int,
    val customerInfo: CustomerInfo,
    val restaurantsInfo: List<RestaurantInfo>
) : Parcelable {
    @Parcelize
    data class RestaurantInfo(
        val name: String,
        val address: String? = "",
        val phoneNumber: String,
        val dishes: List<String>,
        override val latitude: String,
        override val longitude: String
    ) : ILocation

    @Parcelize
    data class CustomerInfo(
        val name: String,
        val address: String,
        val email: String,
        val customerPhone: String,
        val latitude: String? = null,
        val longitude: String? = null,
    ) : Parcelable
}

class CourierItemTaskAdapter(private val onItemClicked: (CourierItemTask) -> Unit) :
    RecyclerView.Adapter<CourierItemTaskAdapter.CourierItemTaskVH>() {
    inner class CourierItemTaskVH(private val binding: ItemCourierTaskBinding) :
        ViewHolder(binding.root) {
        fun refreshUi(currentTask: CourierItemTask) {
            with(binding) {
                root.setOnClickListener { onItemClicked(currentTask) }
                val ctx = binding.root.context
                orderId.text = currentTask.orderId
                courierStatusBubble.apply {
                    val orderStatus =
                        OrderStatus.values().first { it.orderStep == currentTask.orderStatus }
                    text = orderStatus.formattedName
                    background = ResourcesCompat.getDrawable(
                        ctx.resources, orderStatus.colorInt, ctx.theme
                    )
                }
                customerName.text = currentTask.customerInfo.name
                customerAddress.text = currentTask.customerInfo.address
                customerPhone.text = currentTask.customerInfo.customerPhone
                restaurantAddress.text = ""
                restaurantName.text = ""
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