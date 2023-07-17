package com.adelinarotaru.fooddelivery.customer.trackorder

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentTrackOrderBinding
import com.adelinarotaru.fooddelivery.driver.data.CourierRepositoryImpl
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.Constants
import com.adelinarotaru.fooddelivery.utils.show
import com.adelinarotaru.fooddelivery.utils.zoomCameraTo
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val RESTAURANT_DETAILS = "RESTAURANT_DETAILS"
private const val ORDER_ID = "ORDER_ID"
private const val DELIVERY_ADDRESS = "DELIVERY_ADDRESS"

class TrackOrderFragment :
    BaseFragment<FragmentTrackOrderBinding, TrackOrderViewModel>(FragmentTrackOrderBinding::inflate) {
    private val navArgs by navArgs<TrackOrderFragmentArgs>()

    private val orderId by lazy { navArgs.trackOrderArgs.orderId }
    private val deliveryAddress by lazy { navArgs.trackOrderArgs.deliveryAddress }

    override val viewModel by lazy {
        TrackOrderViewModel(
            DependencyProvider.provideDispatcher(),
            CourierRepositoryImpl(DependencyProvider.provideCourierApi())
        )
    }

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchRestaurantCheckpoints(orderId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findMapFragment().getMapAsync {
            map = it

            viewLifecycleOwner.lifecycleScope.launch {
                launch {
                    viewModel.orderStatus.collectLatest { orderUpdates ->
                        binding?.apply { updateOrderStatus(orderUpdates) }
                        if (orderUpdates.orderStatus == OrderStatus.PICKED_UP) {
                            viewModel.startLiveTracking(orderId)
                        }
                    }
                }

                launch {
                    viewModel.navigateToSuccess.collectLatest { isOrderDelivered -> if (isOrderDelivered) navigateToOrderDelivered() }
                }
            }

            viewModel.trackOrder(orderId)
        }
    }

    private fun FragmentTrackOrderBinding.updateOrderStatus(orderUpdates: TrackingUiModel) =
        with(orderUpdates) {
            updateOrderStatusBubble(this)

            livePosition?.let { map.updateCourierLocation(it) }
            handleOrderPreparing(this)
        }

    private fun FragmentTrackOrderBinding.handleOrderPreparing(
        trackingUiModel: TrackingUiModel
    ) {
        if (trackingUiModel.orderStatus == OrderStatus.PREPARING || trackingUiModel.orderStatus == OrderStatus.ORDER_RECEIVED) {
            if (this@TrackOrderFragment::map.isInitialized) {
                map.apply {
                    val restaurantCheckpoints =
                        viewModel.restaurantCheckpoints.value ?: return@apply
                    addRestaurantMarkers(restaurantCheckpoints)
                    zoomCameraTo(restaurantCheckpoints.first())
                }
                updateDeliverySpecificInfo()
            }
        }
    }

    private fun FragmentTrackOrderBinding.updateOrderStatusBubble(
        orderUpdates: TrackingUiModel
    ) {
        orderStatusTitle.text = orderUpdates.orderStatus.formattedName
        orderStatusColor.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(), orderUpdates.orderStatus.colorInt
            )
        )
    }

    private fun FragmentTrackOrderBinding.updateDeliverySpecificInfo() = courierContainer.apply {
        deliveryAddress.text = this@TrackOrderFragment.deliveryAddress
        estimatedDeliveryTime.text = getString(R.string.eta_35_min)
        courierName.text = viewModel.courierName
        show()
    }

    private fun GoogleMap.addCourierMarker(courierCoordinates: LatLng, extra: () -> Unit) {
        clear()
        addMarker {
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_delivery_scooter))
            position(courierCoordinates)
        }
        extra()
        zoomCameraTo(courierCoordinates)
    }


    private fun GoogleMap.updateCourierLocation(courierCoordinates: LatLng) {
        addCourierMarker(courierCoordinates) {
            val restaurantCheckpoints =
                viewModel.restaurantCheckpoints.value ?: return@addCourierMarker
            addRestaurantMarkers(restaurantCheckpoints)
        }
    }

    private fun GoogleMap.addRestaurantMarkers(restaurantsCoordinates: List<LatLng>) {
        restaurantsCoordinates.forEach { restaurant ->
            addMarker {
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_tracking))
                position(restaurant)
            }
        }
    }

    private fun navigateToOrderDelivered() =
        findNavController().navigate(TrackOrderFragmentDirections.moveToSuccess(Constants.CUSTOMER))

    private fun findMapFragment() =
        (childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment)

    override fun onPause() {
        super.onPause()
        viewModel.clearJobs()
    }

}