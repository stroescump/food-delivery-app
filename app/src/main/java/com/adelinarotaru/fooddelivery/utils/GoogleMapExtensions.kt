package com.adelinarotaru.fooddelivery.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
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

fun Context.launchGoogleMapsUsingCoordinates(
    lat: String, long: String, locationName: String
) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=$lat,$long($locationName)")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    mapIntent.resolveActivity(packageManager)?.let {
        ContextCompat.startActivity(this, mapIntent, null)
    }
}