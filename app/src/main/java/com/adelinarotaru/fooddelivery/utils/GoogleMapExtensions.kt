package com.adelinarotaru.fooddelivery.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
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

fun Context.launchGoogleMapsNavigation(
    stops: List<Pair<String?, String?>>,
    currentLocation: Pair<String, String>,
    clientLocation: String
) = runCatching {
    val intent = Intent(Intent.ACTION_VIEW)
    val uriBuilder = StringBuilder("https://www.google.com/maps/dir/?api=1")

    // Set the origin
    val origin = "${currentLocation.first},${currentLocation.second}"
    uriBuilder.append("&origin=$origin")

    // Set the waypoints
    val waypoints = stops.joinToString(separator = "%7C") { "${it.first},${it.second}" }
    uriBuilder.append("&waypoints=$waypoints")

    // Set the destination
    uriBuilder.append("&destination=$clientLocation")

    val uri = Uri.parse(uriBuilder.toString())
    intent.data = uri
    intent.setPackage("com.google.android.apps.maps")
    intent.resolveActivity(packageManager)?.let {
        startActivity(intent)
    }
}

fun Context.launchGoogleMapsNavigationWithAddress(
    currentLocation: Pair<String, String>,
    clientLocation: String,
    appFoundCallback: () -> Unit = {}
) = runCatching {
    val intent = Intent(Intent.ACTION_VIEW)
    val uriBuilder =
        StringBuilder("https://www.google.com/maps/dir/?api=1&origin=${currentLocation.first},${currentLocation.second}")
    uriBuilder.append("&destination=$clientLocation")
    val uri = Uri.parse(uriBuilder.toString())
    intent.data = uri
    intent.setPackage("com.google.android.apps.maps")
    intent.resolveActivity(packageManager)?.let {
        appFoundCallback()
        startActivity(intent)
    }
}

fun formatStops(stops: List<Pair<Double, Double>>): String {
    val formattedStops = StringBuilder()
    for (stop in stops) {
        formattedStops.append("${stop.first},${stop.second}:")
    }
    return formattedStops.removeSuffix(":").toString()
}
