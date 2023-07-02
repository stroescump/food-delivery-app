package com.adelinarotaru.fooddelivery.customer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentTrackOrderBinding
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TrackOrderFragment :
    BaseFragment<FragmentTrackOrderBinding, TrackOrderViewModel>(FragmentTrackOrderBinding::inflate),
    OnMapReadyCallback {

    companion object {
        fun newInstance() = TrackOrderFragment()
    }

    override val viewModel by lazy { TrackOrderViewModel() }
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST) {
            when (it) {
                MapsInitializer.Renderer.LATEST -> Log.d(
                    "MapsDemo", "The latest version of the renderer is used."
                )

                MapsInitializer.Renderer.LEGACY -> Log.d(
                    "MapsDemo", "The legacy version of the renderer is used."
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderStatus.collectLatest { orderStatus ->
                binding?.apply {
                    orderStatusTitle.text = orderStatus.formattedName
                    orderStatusColor.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(), orderStatus.colorInt
                        )
                    )
                }
            }
        }

        (childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment).getMapAsync(
            this
        )

        viewModel.trackOrder(orderId = "")
    }

    override fun onMapReady(map: GoogleMap) {
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(
            MarkerOptions().position(sydney).title("Marker in Sydney")
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

}