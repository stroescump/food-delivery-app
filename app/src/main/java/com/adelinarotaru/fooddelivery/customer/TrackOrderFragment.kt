package com.adelinarotaru.fooddelivery.customer

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentTrackOrderBinding
import com.adelinarotaru.fooddelivery.driver.data.CourierRepositoryImpl
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.Constants
import com.adelinarotaru.fooddelivery.utils.show
import com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val RESTAURANT_DETAILS = "RESTAURANT_DETAILS"

class TrackOrderFragment :
    BaseFragment<FragmentTrackOrderBinding, TrackOrderViewModel>(FragmentTrackOrderBinding::inflate) {
    private var restaurantCoordinates: Pair<Double, Double>? = null

    override val viewModel by lazy {
        TrackOrderViewModel(
            DependencyProvider.provideDispatcher(),
            CourierRepositoryImpl(DependencyProvider.provideCourierApi())
        )
    }

    private lateinit var map: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {

            launch {
                viewModel.orderStatus.collectLatest { newOrderStatus ->
                    binding?.apply { updateOrderStatus(newOrderStatus) }
                    if (newOrderStatus == OrderStatus.PICKED_UP) viewModel.startLiveTracking()
                }
            }

            launch {
                viewModel.liveTracking.collectLatest { courierCoordinates ->
                    if (courierCoordinates == null) return@collectLatest
                    map.apply {
                        clear()
                        addMarker {
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_delivery_scooter))
                            position(courierCoordinates)
                        }
                        newCameraPosition(
                            CameraPosition(
                                courierCoordinates, 18f, 0f, 0f
                            )
                        ).also { moveCamera(it) }
                    }
                }
            }

            launch {
                viewModel.navigateToSuccess.collectLatest { isSuccess -> if (isSuccess) navigateToOrderDelivered() }
            }

        }

        findMapFragment().getMapAsync { map = it }

        viewModel.trackOrder(orderId = "")
    }

    private fun navigateToOrderDelivered() =
        findNavController().navigate(TrackOrderFragmentDirections.moveToSuccess(Constants.CUSTOMER))

    private fun findMapFragment() =
        (childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment)

    private fun FragmentTrackOrderBinding.updateOrderStatus(orderStatus: OrderStatus) {
        if (orderStatus == OrderStatus.PREPARING) {
            val restaurantLatLng = restaurantCoordinates?.run { LatLng(first, second) } ?: return
            map.apply {
                addMarker {
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_tracking))
                    position(restaurantLatLng)
                }
                newCameraPosition(
                    CameraPosition(
                        restaurantLatLng, 18f, 0f, 0f
                    )
                ).also { moveCamera(it) }
            }
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

    companion object {
        fun newInstance(lat: Double, long: Double) = TrackOrderFragment().apply {
            arguments = bundleOf(RESTAURANT_DETAILS to (lat to long))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        restaurantCoordinates = arguments?.get(RESTAURANT_DETAILS) as Pair<Double, Double>
        restaurantCoordinates = 47.414562 to 8.561312
    }

}