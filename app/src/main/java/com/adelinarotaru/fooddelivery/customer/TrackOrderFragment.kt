package com.adelinarotaru.fooddelivery.customer

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentTrackOrderBinding
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.show
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TrackOrderFragment :
    BaseFragment<FragmentTrackOrderBinding, TrackOrderViewModel>(FragmentTrackOrderBinding::inflate) {

    companion object {
        fun newInstance() = TrackOrderFragment()
    }

    override val viewModel by lazy { TrackOrderViewModel() }
    private lateinit var _map: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderStatus.collectLatest { newOrderStatus ->
                binding?.apply {
                    updateOrderStatus(newOrderStatus)
                }
            }
        }

        (childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment).getMapAsync {
            _map = it
            val sydney = LatLng(-34.0, 151.0)
            it.addMarker(
                MarkerOptions().position(sydney).title("Marker in Sydney")
            )
            it.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }

        viewModel.trackOrder(orderId = "")
    }

    private fun FragmentTrackOrderBinding.updateOrderStatus(orderStatus: OrderStatus) {
        if (orderStatus.orderStep > 0) {
            courierContainer.apply {
                deliveryAddress.text = "Canal St. 44W"
                estimatedDeliveryTime.text = "5:30 PM"
                courierName.text = "Mark Manson"
                show()
            }
        } else {
            // TODO Handle logic for Order Rejected
        }

        orderStatusTitle.text = orderStatus.formattedName
        orderStatusColor.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(), orderStatus.colorInt
            )
        )
    }

}