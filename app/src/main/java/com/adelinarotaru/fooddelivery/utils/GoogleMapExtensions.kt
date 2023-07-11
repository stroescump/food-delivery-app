package com.adelinarotaru.fooddelivery.utils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

fun GoogleMap.zoomCameraTo(coordinates: LatLng) {
    CameraUpdateFactory.newCameraPosition(
        CameraPosition(
            coordinates, 18f, 0f, 0f
        )
    ).also { moveCamera(it) }
}