package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentCourierOrderAcceptedBinding
import com.adelinarotaru.fooddelivery.driver.data.CourierRepositoryImpl
import com.adelinarotaru.fooddelivery.driver.models.CourierMenuItem
import com.adelinarotaru.fooddelivery.driver.ui.dashboard.CourierItemTask
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.login.domain.ILocation
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.Constants
import com.adelinarotaru.fooddelivery.utils.hide
import com.adelinarotaru.fooddelivery.utils.launchGoogleMapsNavigation
import com.adelinarotaru.fooddelivery.utils.launchGoogleMapsNavigationWithAddress
import com.adelinarotaru.fooddelivery.utils.show
import com.adelinarotaru.fooddelivery.utils.showError
import com.adelinarotaru.fooddelivery.utils.showMessageWindow
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CourierOrderAcceptedFragment :
    BaseFragment<FragmentCourierOrderAcceptedBinding, CourierOrderAcceptedViewModel>(
        FragmentCourierOrderAcceptedBinding::inflate
    ) {

    private var sendLocationUpdatesJob: Job? = null

    override val viewModel: CourierOrderAcceptedViewModel by lazy {
        CourierOrderAcceptedViewModel(
            DependencyProvider.provideDispatcher(),
            CourierRepositoryImpl(DependencyProvider.provideCourierApi())
        )
    }

    private val productsAdapter by lazy { OrderAcceptedAdapter() }
    private lateinit var courierTask: CourierItemTask

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
    }

    private fun doIfProductsPickedOrError(block: () -> Unit) {
        if (productsAdapter.areAllProductsPickedUp()) {
            block()
        } else showMessageWindow(
            requireContext(), getString(R.string.please_make_sure_to_pick_up_all_products)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        courierTask = sharedViewModel.courierItemTask
    }

    @SuppressLint("SetTextI18n", "MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            if (courierTask.orderStatus == OrderStatus.ORDER_RECEIVED.orderStep) acceptOrder.show()
            screenTitle.text = "Order #${courierTask.orderId}"
            orderProducts.adapter = productsAdapter

            headToClientLocation.setOnClickListener {
                doIfProductsPickedOrError {
                    getLiveLocation().addOnSuccessListener { location ->
                        runCatching {
                            val currentLat = location.latitude.toString()
                            val currentLong = location.longitude.toString()
                            requireContext().launchGoogleMapsNavigationWithAddress(
                                currentLat to currentLong, courierTask.customerInfo.address
                            ) {
                                courierTask = courierTask.copy(orderStatus = 2)
                                viewModel.updateOrderStatus(
                                    courierTask.orderId, OrderStatus.PICKED_UP
                                )
                            }
                        }.onFailure { showError(Throwable(getString(R.string.there_was_an_issue_establishing_the_route_please_verify_location_permissions_and_retry))) }
                    }.addOnFailureListener {
                        showError(it)
                    }
                }
            }

            markOrderAsDelivered.setOnClickListener {
                doIfProductsPickedOrError {
                    viewModel.updateOrderStatus(
                        courierTask.orderId, OrderStatus.DELIVERED
                    )
                }
            }

            acceptOrder.setOnClickListener {
                runCatching {
                    viewModel.acceptOrder(
                        courierTask.orderId, sharedViewModel.getUserId()
                    )
                }.onFailure { showError(it) }
            }

            planRestaurantsRoute.setOnClickListener {
                if (courierTask.restaurantsInfo.size > 1) viewModel.optimizeRoute(courierTask.orderId) else {
                    val navCoordinates = listOf(courierTask.restaurantsInfo.first())
                    openNavigationWithCoordinates(navCoordinates)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.navigateToSuccess.collectLatest { orderDelivered ->
                    if (orderDelivered) navigateToSuccess()
                }
            }

            launch {
                viewModel.optimizedRoute.collectLatest { routes ->
                    routes ?: return@collectLatest
                    openNavigationWithCoordinates(routes)
                }
            }

            launch {
                viewModel.orderAccepted.collectLatest { accepted ->
                    accepted ?: return@collectLatest
                    courierTask = courierTask.copy(orderStatus = 1)
                    binding?.acceptOrder?.hide()
                }
            }
        }

        courierTask.restaurantsInfo.mapIndexed { index, item ->
            CourierMenuItem(
                restaurantInfo = item,
                courierTask.orderStatus == OrderStatus.PICKED_UP.orderStep,
                index
            )
        }.updateUi()
    }

    @SuppressLint("MissingPermission")
    private fun openNavigationWithCoordinates(routes: List<ILocation>) {
        getLiveLocation().addOnSuccessListener { location ->
            val currentLat = location.latitude
            val currentLong = location.longitude
            requireContext().launchGoogleMapsNavigation(
                stops = routes.map { it.latitude to it.longitude },
                currentLocation = currentLat.toString() to currentLong.toString(),
                clientLocation = courierTask.customerInfo.address
            )
                .onFailure { showError(Throwable(getString(R.string.there_was_an_issue_establishing_the_route_please_verify_location_permissions_and_retry))) }
        }.addOnFailureListener {
            showError(it)
        }
    }

    @SuppressLint(
        "MissingPermission"
    )
    override fun onResume() {
        super.onResume()
        doIfOrderPickedUp {
            sendLocationUpdatesJob = viewLifecycleOwner.lifecycleScope.launch {
                while (isActive) {
                    delay(Constants.REQUEST_DELAY)
                    getLiveLocation().addOnSuccessListener { location ->
                        val latitude = location.latitude
                        val longitude = location.longitude

                        viewModel.sendLocationUpdates(
                            sharedViewModel.getUserId(), latitude, longitude
                        )
                    }
                }
            }
        }
    }

    private fun getLiveLocation(): Task<Location> = fusedLocationClient.getCurrentLocation(
        LocationRequest.PRIORITY_HIGH_ACCURACY,
        object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                CancellationTokenSource().token

            override fun isCancellationRequested(): Boolean = false
        })

    private fun doIfOrderPickedUp(block: () -> Unit) {
        if (courierTask.orderStatus == OrderStatus.PICKED_UP.orderStep) {
            block()
        }
    }

    override fun onPause() {
        super.onPause()
        sendLocationUpdatesJob?.cancel()
    }

    private fun navigateToSuccess() =
        findNavController().navigate(CourierOrderAcceptedFragmentDirections.goToSuccess(Constants.COURIER))

    private fun List<CourierMenuItem>.updateUi() = productsAdapter.differ.submitList(this)
}

